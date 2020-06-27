package com.toDoApp.ToDo;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class ToDoConfiguration extends Configuration {
	
	private static final String DATABASE = "database";

	@Valid
	@NotNull
	private DataSourceFactory dataSourceFactory = new DataSourceFactory();

	@JsonProperty(DATABASE)
	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	@JsonProperty(DATABASE)
	public void setDataSourceFactory(final DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}
}
