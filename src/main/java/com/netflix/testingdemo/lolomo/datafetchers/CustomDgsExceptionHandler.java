package com.netflix.testingdemo.lolomo.datafetchers;

import graphql.ErrorType;
import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomDgsExceptionHandler {
    @GraphQlExceptionHandler
    public GraphQLError handle(Top10NotAvailableException ex) {
        return GraphQLError.newError()
                .errorType(ErrorType.DataFetchingException)
                .message("Top 10 unavailable").build();
    }
}
