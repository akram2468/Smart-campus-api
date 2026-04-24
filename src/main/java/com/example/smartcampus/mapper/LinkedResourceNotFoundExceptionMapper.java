package com.example.smartcampus.mapper;

import com.example.smartcampus.exception.LinkedResourceNotFoundException;
import com.example.smartcampus.model.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        ApiError error = new ApiError(
                422,
                "LinkedResourceNotFound",
                exception.getMessage()
        );

        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}