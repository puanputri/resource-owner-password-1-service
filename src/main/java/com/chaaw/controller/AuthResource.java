package com.chaaw.controller;

import com.chaaw.dto.Credentials;
import com.chaaw.dto.RefreshRequest;
import com.chaaw.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService auth;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/login")
    public Response login(Credentials cred) {
        try {
            return Response.ok(auth.login(cred)).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/refresh")
    public Response refresh(RefreshRequest req) {
        try {
            return Response.ok(auth.refresh(req, jwt)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}