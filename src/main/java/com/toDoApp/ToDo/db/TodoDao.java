package com.toDoApp.ToDo.db;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.toDoApp.ToDo.resources.Task;
import com.toDoApp.ToDo.resources.Todo;

@RegisterMapper(TodoMapper.class)
public abstract class TodoDao {
	
	@CreateSqlObject
	abstract TaskDao taskDao();

	@SqlQuery("select * from todo;")
	public abstract List<Todo> getTodos();
	
	@SqlQuery("select * from todo where id = :id")
	public abstract Todo getTodo(@Bind("id") long id);
	
	@SqlQuery("select last_insert_id();")
	public abstract int lastInsertId();
	
	@SqlUpdate("insert into todo(name, description) values(:name, :description)")
	@GetGeneratedKeys
	protected abstract long insertTodo(@BindBean Todo todo);
	
	@Transaction
	public long createTodo(@NotNull @Valid Todo todo) {
		long id = insertTodo(todo);
		List<Task> tasks = todo.getTasks();
		if(tasks!= null && !tasks.isEmpty()) {
			for(Task task : tasks) {
				task.setTodoId(id);
				taskDao().insertTask(task);
			}
		}
		return id;
	}
	
	@SqlUpdate("update todo set name = :name, description = :description where id = :id")
	protected abstract void updateTodo(@NotNull @Valid Todo todo);
	
	@Transaction
	public void editTodo(@NotNull @Valid Todo todo) {
		//remove existing tasks
		taskDao().deleteTasks(todo.getId());
		
		List<Task> tasks = todo.getTasks();
		if(tasks != null && !tasks.isEmpty()) {
			for(Task task : tasks) {
				//insert new tasks
				task.setTodoId(todo.getId());
				taskDao().insertTask(task);
			}
		}
		
		updateTodo(todo);
	}
	
	@SqlUpdate("delete from todo where id = :id")
	protected abstract int deleteTodo(@Bind("id") long id);
	
	@Transaction
	public int removeTodo(long id) {
		Todo todo = getTodo(id);
		
		taskDao().deleteTasks(todo.getId());
		
		return deleteTodo(id);
	}

}
