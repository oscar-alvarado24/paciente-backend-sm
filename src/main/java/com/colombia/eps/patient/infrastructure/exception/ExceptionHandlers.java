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

            case PATIENT_ALREADY_EXISTS ->
                graphQLError = graphQLError(ErrorType.BAD_REQUEST, ex.getMessage(), env, exceptionName.name());

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

