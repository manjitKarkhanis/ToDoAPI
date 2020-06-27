package com.toDoApp.ToDo.resources;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("deprecation")
public class Todo {
	private long id;
	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	
	private List<Task> tasks;
	
	public Todo() {
		super();
		this.id = -1;
	}
	
	public Todo(long id, String name, String description, List<Task> tasks) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.tasks = tasks;
	}
	
	public Todo(long id, String name, String description) {
		this(id, name, description, null);
	}

	public long getId() {
		return id;
	}
	public void setId(long id2) {
		this.id = id2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
