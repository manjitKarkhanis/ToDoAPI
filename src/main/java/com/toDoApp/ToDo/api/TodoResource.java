package com.toDoApp.ToDo.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.http.HttpStatus;

import com.codahale.metrics.annotation.Timed;
import com.toDoApp.ToDo.resources.Todo;

@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class TodoResource {
	private final TodoService todoService;

	public TodoResource(TodoService todoService) {
		this.todoService = todoService;
	}

	@GET
	@Timed
	public Representation<List<Todo>> getTodos() {
		return new Representation<List<Todo>>(HttpStatus.OK_200, todoService.getTodos());
	}

	@GET
	@Timed
	@Path("{id}")
	public Representation<Todo> getTodo(@PathParam("id") final long id) {
		return new Representation<Todo>(HttpStatus.OK_200, todoService.getTodo(id));
	}

	@POST
	@Timed
	public Representation<Todo> createTodo(@NotNull @Valid final Todo todo) {
		//Return the existing Todo in case the ID is present
		if(todo.getId() >= 0) {
			return getTodo(todo.getId());
		}
		return new Representation<Todo>(HttpStatus.OK_200, todoService.createTodo(todo));
	}

	@PUT
	@Timed
	@Path("{id}")
	public Representation<Todo> editTodo(@NotNull @Valid final Todo todo, @PathParam("id") final long id) {
		todo.setId(id);
		return new Representation<Todo>(HttpStatus.OK_200, todoService.editTodo(todo, id));
	}

	@DELETE
	@Timed
	@Path("{id}")
	public Representation<String> deleteTodo(@PathParam("id") final long id) {
		return new Representation<String>(HttpStatus.OK_200, todoService.deleteTodo(id));
	}
}
