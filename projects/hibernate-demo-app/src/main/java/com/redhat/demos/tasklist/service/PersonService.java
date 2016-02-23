package com.redhat.demos.tasklist.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.redhat.demos.tasklist.dao.PersonDAO;
import com.redhat.demos.tasklist.model.Person;

@Stateless
public class PersonService {
	
	@Inject
	PersonDAO personDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Person getPerson(Long id) {
		return personDAO.findById(id);
	}
	
	public void registerPerson(Person p) {
		personDAO.makePersistent(p);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletePerson(Long id) {
		Person p = personDAO.findById(id);
		personDAO.makeTransient(p);
	}
	
	public List<Person> getAll() {
		return personDAO.findAll();
	}
}
