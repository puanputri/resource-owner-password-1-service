package com.chaaw.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/userinfo")
@Produces(MediaType.APPLICATION_JSON)
public class UserInfoResource {

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"user", "admin"})
    public Response info() {
        Map<String, Object> data = Map.of(
                "username", jwt.getSubject(),
                "email", jwt.getClaim("email")
        );
        return Response.ok(data).build();
    }
}