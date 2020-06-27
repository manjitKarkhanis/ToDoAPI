package com.toDoApp.ToDo.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.toDoApp.ToDo.resources.Task;

@RegisterMapper(TaskMapper.class)
public interface TaskDao {

	@SqlUpdate("insert into task(name, description, todo_id) values(:name, :description, :todoId)")
	public abstract void insertTask(@BindBean Task task);

	@SqlUpdate("delete from task where todo_id = :todoId")
	public abstract void deleteTasks(@Bind("todoId") long todoId);

	@SqlQuery("select * from task where todo_id = :todoId")
	public abstract List<Task> getTasks(@Bind("todoId") long todoId);

	@SqlUpdate("update task set name = :name, description = :description where id = :id")
	public abstract void updateTask(Task task);
	
}
