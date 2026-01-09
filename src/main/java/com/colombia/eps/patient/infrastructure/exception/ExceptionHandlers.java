package com.colombia.eps.patient.infrastructure.exception;

import com.colombia.eps.patient.infrastructure.helper.ConvertNameToConstants;
import com.colombia.eps.patient.infrastructure.helper.ExceptionName;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
public class ExceptionHandlers extends DataFetcherExceptionResolverAdapter {

    @Override
    protected @NonNull GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        ExceptionName exceptionName = ExceptionName.valueOf(ConvertNameToConstants.exceptionToConstant(ex.getClass().getSimpleName()));
        GraphQLError graphQLError;
        switch (exceptionName) {
            case PATIENT_NOT_FOUND ->
                    graphQLError = graphQLError(ErrorType.NOT_FOUND, ex.getMessage(), env, exceptionName.name());

            case PATIENT_ALREADY_EXIST ->
                graphQLError = graphQLError(ErrorType.BAD_REQUEST, ex.getMessage(), env, exceptionName.name());

            case CREATE_SES_IDENTITY ->
                graphQLError = graphQLError(ErrorType.INTERNAL_ERROR, ExceptionResponse.CREATE_SES_IDENTITY.getMessage(), env, exceptionName.name());

            case IDENTITY_NOT_FOUND ->
                graphQLError = graphQLError(ErrorType.FORBIDDEN, ExceptionResponse.IDENTITY_NOT_FOUND.getMessage(), env, exceptionName.name());

            case GET_VERIFICATION_STATUS_IN_SES ->
                graphQLError = graphQLError(ErrorType.INTERNAL_ERROR, ExceptionResponse.GET_VERIFICATION_STATUS_IN_SES.getMessage(), env, exceptionName.name());

            case GET_PATIENT ->
                graphQLError = graphQLError(ErrorType.INTERNAL_ERROR, ExceptionResponse.GET_PATIENT.getMessage(), env, exceptionName.name());

            case CHANGE_PATIENT_STATUS, UPDATE_PATIENT, CREATE_USER_IN_USER_POOL, ADD_USER_TO_GROUP ->
                graphQLError = graphQLError(ErrorType.INTERNAL_ERROR, ex.getMessage(), env, exceptionName.name());

            case SAVE_PHOTO_TO_PATIENT ->
                graphQLError = graphQLError(ErrorType.INTERNAL_ERROR, ExceptionResponse.SAVE_PHOTO_TO_PATIENT.getMessage(), env, exceptionName.name());

            default -> graphQLError = GraphqlErrorBuilder.newError().build();
        }
        return graphQLError;
    }


    private GraphQLError graphQLError(ErrorType errorType, String message, DataFetchingEnvironment env, String code) {
        return GraphqlErrorBuilder.newError()
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(Map.of(
                        "code", code,
                        "classification", errorType.toString(),
                        "timestamp", Instant.now().toString()

                )).build();
    }
}

