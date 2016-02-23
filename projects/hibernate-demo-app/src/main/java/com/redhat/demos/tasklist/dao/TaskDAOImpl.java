package com.redhat.demos.tasklist.dao;

import com.redhat.demos.tasklist.model.Task;

public class TaskDAOImpl extends GenericDAOImpl<Task, Long> implements TaskDAO {

	public TaskDAOImpl() {
		super(Task.class);
	}

}
