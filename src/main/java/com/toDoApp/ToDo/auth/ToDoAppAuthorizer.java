package com.toDoApp.ToDo.auth;

import java.util.Objects;

import io.dropwizard.auth.Authorizer;

public class ToDoAppAuthorizer implements Authorizer<User> {

	@Override
	public boolean authorize(User principal, String role) {
		// Allow any logged in user.
		if (Objects.nonNull(principal)) {
			return true;
		}
		return false;
	}

}
