FROM openjdk:17-jdk-slim

LABEL authors="oscar"

# Crear un usuario no privilegiado
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

WORKDIR /app

# Copiar archivos con permisos de solo lectura
COPY --chown=root:root --chmod=0555 build/libs/patient-0.0.1-SNAPSHOT.jar /app/patient-0.0.1-SNAPSHOT.jar
COPY --chown=root:root --chmod=0555 src/main/resources /app/resources

# Cambiar al usuario no privilegiado solo para ejecutar la aplicación
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/patient-0.0.1-SNAPSHOT.jar"]
