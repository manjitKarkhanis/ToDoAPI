package com.toDoApp.ToDo.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.toDoApp.ToDo.resources.Task;

public class TaskMapper implements ResultSetMapper<Task>{
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";

	@Override
	public Task map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
		return new Task(resultSet.getLong(ID), resultSet.getString(NAME), resultSet.getString(DESCRIPTION));
	}

}
