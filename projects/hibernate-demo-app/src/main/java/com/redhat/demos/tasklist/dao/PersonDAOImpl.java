package com.redhat.demos.tasklist.dao;

import com.redhat.demos.tasklist.model.Person;

public class PersonDAOImpl extends GenericDAOImpl<Person, Long> implements PersonDAO {

	public PersonDAOImpl() {
		super(Person.class);
	}

}
