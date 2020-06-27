package com.toDoApp.ToDo.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.toDoApp.ToDo.resources.Todo;

public class TodoMapper implements ResultSetMapper<Todo>{
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";

	@Override
	public Todo map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
		return new Todo(resultSet.getLong(ID), resultSet.getString(NAME), resultSet.getString(DESCRIPTION));
	}

}
