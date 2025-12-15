# Script para configurar AWS Cognito con LocalStack
# Autor: Configuracion EPS Colombia

Write-Host "Iniciando configuracion de AWS Cognito con LocalStack..." -ForegroundColor Green

# Configurar variables de entorno AWS
Write-Host "Configurando variables de entorno..." -ForegroundColor Yellow
$env:AWS_ACCESS_KEY_ID = "test"
$env:AWS_SECRET_ACCESS_KEY = "test"
$env:AWS_DEFAULT_REGION = "us-east-1"

# Verificar que awslocal este disponible
try {
    $awslocalVersion = awslocal --version
    Write-Host "awslocal encontrado: $awslocalVersion" -ForegroundColor Green
}
catch {
    Write-Host "Error: awslocal no esta disponible. Asegurate de tener LocalStack instalado." -ForegroundColor Red
    exit 1
}

# Verificar que el archivo schema.json existe
if (-not (Test-Path "schema.json")) {
    Write-Host "Error: El archivo schema.json no existe en el directorio actual." -ForegroundColor Red
    Write-Host "Asegurate de que el archivo schema.json este en el mismo directorio que este script." -ForegroundColor Yellow
    exit 1
}

try {
    # 1. desplegar localstack
    localstack start -d

    #2. Crear User Pool
    Write-Host "Creando User Pool..." -ForegroundColor Cyan
    $userPoolOutput = awslocal cognito-idp create-user-pool --pool-name "eps-colombia-Pool" --auto-verified-attributes email --schema file://schema.json --username-attributes email --region us-east-1 | ConvertFrom-Json
    
    $USER_POOL_ID = $userPoolOutput.UserPool.Id
    Write-Host "User Pool creado exitosamente!" -ForegroundColor Green
    Write-Host "USER_POOL_ID: $USER_POOL_ID" -ForegroundColor White

    # 3. Configurar MFA
    Write-Host "Configurando MFA..." -ForegroundColor Cyan
    awslocal cognito-idp set-user-pool-mfa-config --user-pool-id $USER_POOL_ID --region us-east-1 --mfa-configuration ON --software-token-mfa-configuration Enabled=true
    
    Write-Host "MFA configurado exitosamente!" -ForegroundColor Green

    # 4. Crear User Pool Client
    Write-Host "Creando User Pool Client..." -ForegroundColor Cyan
    $clientOutput = awslocal cognito-idp create-user-pool-client --user-pool-id $USER_POOL_ID --client-name test-client --explicit-auth-flows ADMIN_NO_SRP_AUTH --access-token-validity 60 --id-token-validity 60 --refresh-token-validity 30 --auth-session-validity 3 | ConvertFrom-Json
    
    $CLIENT_ID = $clientOutput.UserPoolClient.ClientId
    Write-Host "User Pool Client creado exitosamente!" -ForegroundColor Green
    Write-Host "CLIENT_ID: $CLIENT_ID" -ForegroundColor White

    # 5. Describir User Pool Client
    Write-Host "Obteniendo informacion del User Pool Client..." -ForegroundColor Cyan
    $clientInfo = awslocal cognito-idp describe-user-pool-client --user-pool-id $USER_POOL_ID --client-id $CLIENT_ID | ConvertFrom-Json
    
    Write-Host "Informacion del cliente obtenida exitosamente!" -ForegroundColor Green
    Write-Host "Nombre del cliente: $($clientInfo.UserPoolClient.ClientName)" -ForegroundColor White

    # 6. Crear grupo de Pacientes
    Write-Host "Creando grupo de Pacientes..." -ForegroundColor Cyan
    awslocal cognito-idp create-group --user-pool-id $USER_POOL_ID --group-name Pacientes --description "Equipo_de_pacientes"
    
    Write-Host "Grupo de Pacientes creado exitosamente!" -ForegroundColor Green

    # Resumen final
    Write-Host ""
    Write-Host "Configuracion completada exitosamente!" -ForegroundColor Green
    Write-Host "=================================================" -ForegroundColor Yellow
    Write-Host "RESUMEN DE CONFIGURACION:" -ForegroundColor Yellow
    Write-Host "User Pool ID: $USER_POOL_ID" -ForegroundColor White
    Write-Host "Client ID: $CLIENT_ID" -ForegroundColor White
    Write-Host "Pool Name: eps-colombia-Pool" -ForegroundColor White
    Write-Host "Group Created: Pacientes" -ForegroundColor White
    Write-Host "MFA: Habilitado" -ForegroundColor White
    Write-Host "Region: us-east-1" -ForegroundColor White
    Write-Host "=================================================" -ForegroundColor Yellow

    # Guardar informacion en archivo de texto simple
    $configText = @"
CONFIGURACION AWS COGNITO - EPS COLOMBIA
========================================
User Pool ID: $USER_POOL_ID
Client ID: $CLIENT_ID
Pool Name: eps-colombia-Pool
Region: us-east-1
MFA: Enabled
Group: Pacientes
Created At: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
"@
    
    $configText | Out-File -FilePath "cognito-config.txt" -Encoding UTF8
    Write-Host "Configuracion guardada en cognito-config.txt" -ForegroundColor Green

}
catch {
    Write-Host "Error durante la configuracion:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host "Verifica que LocalStack este ejecutandose y que todos los archivos necesarios esten disponibles." -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "Script ejecutado correctamente. Listo para usar!" -ForegroundColor Green