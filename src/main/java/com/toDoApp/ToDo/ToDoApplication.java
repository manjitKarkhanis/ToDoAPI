package com.toDoApp.ToDo;

import javax.sql.DataSource;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.toDoApp.ToDo.api.TodoResource;
import com.toDoApp.ToDo.api.TodoService;
import com.toDoApp.ToDo.auth.ToDoAppAuthenticator;
import com.toDoApp.ToDo.auth.ToDoAppAuthorizer;
import com.toDoApp.ToDo.auth.User;
import com.toDoApp.ToDo.health.ToDoAppHealthCheck;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Environment;

public class ToDoApplication extends Application<ToDoConfiguration> {

	  private static final String SQL = "sql";
	  private static final String DROPWIZARD_TODO_SERVICE = "Dropwizard ToDo service";
	  private static final String BEARER = "Bearer";

	  public static void main(String[] args) throws Exception {
	    new ToDoApplication().run(args);
	  }

	  @Override
	  public void run(ToDoConfiguration configuration, Environment environment) {
	    // Datasource configuration
	    final DataSource dataSource = configuration.getDataSourceFactory().build(environment.metrics(), SQL);
	    DBI dbi = new DBI(dataSource);

	    // Register Health Check
	    ToDoAppHealthCheck healthCheck = new ToDoAppHealthCheck(dbi.onDemand(TodoService.class));
	    environment.healthChecks().register(DROPWIZARD_TODO_SERVICE, healthCheck);

	    // Register OAuth authentication
	    environment.jersey()
	        .register(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<User>()
	            .setAuthenticator(new ToDoAppAuthenticator())
	            .setAuthorizer(new ToDoAppAuthorizer()).setPrefix(BEARER).buildAuthFilter()));
	    environment.jersey().register(RolesAllowedDynamicFeature.class);

	    // Register resources
	    environment.jersey().register(new TodoResource(dbi.onDemand(TodoService.class)));
	    
	    //Don't include non null objects in the response
	    environment.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
	  }

}
