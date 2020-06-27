package com.toDoApp.ToDo.api;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

import com.toDoApp.ToDo.db.TaskDao;
import com.toDoApp.ToDo.db.TodoDao;
import com.toDoApp.ToDo.resources.Task;
import com.toDoApp.ToDo.resources.Todo;

public abstract class TodoService {

	private static final String TODO_NOT_FOUND = "ToDo id %s not found.";
	private static final String DATABASE_REACH_ERROR = "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
	private static final String DATABASE_CONNECTION_ERROR = "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
	private static final String DATABASE_UNEXPECTED_ERROR = "Unexpected error occurred while attempting to reach the database. Details: ";
	private static final String SUCCESS = "Success...";
	private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting part.";

	@CreateSqlObject
	abstract TodoDao todoDao();
	
	@CreateSqlObject
	abstract TaskDao taskDao();

	public List<Todo> getTodos() {
		List<Todo> todos = todoDao().getTodos();

		if (todos != null && !todos.isEmpty()) {
			for (Todo todo : todos) {
				List<Task> tasks = taskDao().getTasks(todo.getId());
				if (tasks != null && !tasks.isEmpty()) {
					todo.setTasks(tasks);
				}
			}
		}

		return todos;
	}

	public Todo getTodo(long id) {
		Todo todo = todoDao().getTodo(id);
		if (Objects.isNull(todo)) {
			throw new WebApplicationException(String.format(TODO_NOT_FOUND, id), Status.NOT_FOUND);
		}
		todo.setTasks(taskDao().getTasks(id));
		return todo;
	}
	
	
	public Todo createTodo(@NotNull @Valid Todo todo) {
		long id = todoDao().createTodo(todo);
		Todo newTodo = todoDao().getTodo(id);
		newTodo.setTasks(taskDao().getTasks(id));
		return newTodo;
	}

	public Todo editTodo(@NotNull @Valid Todo todo, long id) {
		if (Objects.isNull(todoDao().getTodo(id))) {
			throw new WebApplicationException(String.format(TODO_NOT_FOUND, todo.getId()), Status.NOT_FOUND);
		}
		todo.setId(id);
		todoDao().editTodo(todo);
		return todoDao().getTodo(todo.getId());
	}

	public String deleteTodo(long id) {
		
		if (Objects.isNull(todoDao().getTodo(id))) {
			throw new WebApplicationException(String.format(TODO_NOT_FOUND, id), Status.NOT_FOUND);
		}
		
		int result = todoDao().removeTodo(id);
		switch (result) {
		case 1:
			return SUCCESS;
		default:
			throw new WebApplicationException(UNEXPECTED_ERROR, Status.INTERNAL_SERVER_ERROR);
		}
	}

	public String performHealthCheck() {
		try {
			getTodos();
		} catch (UnableToObtainConnectionException ex) {
			return checkUnableToObtainConnectionException(ex);
		} catch (UnableToExecuteStatementException ex) {
			return checkUnableToExecuteStatementException(ex);
		} catch (Exception ex) {
			return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
		}
		return null;
	}

	private String checkUnableToObtainConnectionException(UnableToObtainConnectionException ex) {
		if (ex.getCause() instanceof java.sql.SQLNonTransientConnectionException) {
			return DATABASE_REACH_ERROR + ex.getCause().getLocalizedMessage();
		} else if (ex.getCause() instanceof java.sql.SQLException) {
			return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
		} else {
			return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
		}
	}

	private String checkUnableToExecuteStatementException(UnableToExecuteStatementException ex) {
		if (ex.getCause() instanceof java.sql.SQLSyntaxErrorException) {
			return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
		} else {
			return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
		}
	}

}
