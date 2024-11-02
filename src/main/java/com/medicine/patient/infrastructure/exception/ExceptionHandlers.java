package com.medicine.patient.infrastructure.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandlers extends DataFetcherExceptionResolverAdapter {

    @Override
    protected @NonNull GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        if (ex instanceof PatientNotFoundException exception) {
            ErrorType errorType;
            errorType = ErrorType.NOT_FOUND;
            return graphQLError(errorType, exception, env);

        }
            return GraphqlErrorBuilder.newError().build();
        }


    private GraphQLError graphQLError(ErrorType errorType, Exception ex, DataFetchingEnvironment env){
        return GraphqlErrorBuilder.newError()
                .errorType(errorType)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}

