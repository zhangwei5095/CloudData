package com.tutu.clouddata.session;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import com.tutu.clouddata.auth.dao.SystemDatastore;
import com.tutu.clouddata.context.Context;
import com.tutu.clouddata.context.ContextHolder;
import com.tutu.clouddata.dto.auth.AccessToken;
import com.tutu.clouddata.dto.auth.User;

public class AuthFilter implements ContainerRequestFilter {
	private static final SystemDatastore systemDatastore = (SystemDatastore) GlobalContext.getBean("systemDatastore");

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
//		ContextHolder.setContext(new Context());
//		ContextHolder.getContext().setDatastore(systemDatastore);
		String authToken = requestContext.getHeaderString("X-Auth-Token");
		if (!requestContext.getUriInfo().getPath().endsWith("authenticate")) {
			boolean paas = false;
			if (authToken != null) {
				AccessToken accessToken = systemDatastore.get(AccessToken.class, authToken);
				if (accessToken != null) {
					Context context = new Context();
					context.setUser(systemDatastore.find(User.class).field("name").equal(accessToken.getUserId()).get());
					ContextHolder.setContext(context);
					paas = true;
				}
			}
			if (!paas) {
				requestContext.abortWith(Response.status(HttpServletResponse.SC_UNAUTHORIZED).build());
			}
		}
	}

}
