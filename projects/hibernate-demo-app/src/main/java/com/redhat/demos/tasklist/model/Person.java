package com.redhat.demos.tasklist.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.redhat.demos.tasklist.converters.PasswordConverter;

@Entity(name="person")
@Table(name="PERSON")
@SqlResultSetMappings({
	@SqlResultSetMapping(
            name = "PersonTaskCountMapping",
            entities = @EntityResult(
                    entityClass = Person.class,
                    fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "firstName", column = "firstName"),
                        @FieldResult(name = "lastName", column = "lastName"),
                        @FieldResult(name = "password", column = "password"),
                        @FieldResult(name = "email", column = "email")}),
            columns = @ColumnResult(name = "openTaskCount", type = Long.class))
})
public class Person {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	@Column
	@NotNull
	String firstName;
	
	@Column
	@NotNull
	String lastName;
	
	@Column
	String email;
	
	@Column
	@Convert(converter=PasswordConverter.class)
	String password;
	
	@OneToMany(mappedBy = "owner")
	Set<Task> tasks = new HashSet<Task>();
	
	public Long getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	
	
	
	
}
