package com.toDoApp.ToDo.health;

import com.codahale.metrics.health.HealthCheck;
import com.toDoApp.ToDo.api.TodoService;

public class ToDoAppHealthCheck extends HealthCheck {

	private static final String HEALTHY = "The ToDo App Service is healthy for read and write";
	private static final String UNHEALTHY = "The ToDo App Service is not healthy.";
	private static final String MESSAGE_PLACEHOLDER = "{}";
	private final TodoService tasksService;

	public ToDoAppHealthCheck(TodoService tasksService) {
		this.tasksService = tasksService;
	}

	@Override
	protected Result check() throws Exception {
		String mySqlHealthStatus = tasksService.performHealthCheck();

	    if (mySqlHealthStatus == null) {
	      return Result.healthy(HEALTHY);
	    } else {
	      return Result.unhealthy(UNHEALTHY + MESSAGE_PLACEHOLDER, mySqlHealthStatus);
	    }
	}
}
