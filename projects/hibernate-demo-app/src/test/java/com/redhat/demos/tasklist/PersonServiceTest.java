package com.redhat.demos.tasklist;

import static org.junit.Assert.assertEquals;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.demos.tasklist.model.Person;
import com.redhat.demos.tasklist.service.PersonService;

@RunWith(Arquillian.class)
public class PersonServiceTest {

	@EJB
	PersonService personService;
	
	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
				.addAsResource("META-INF/persistence.xml","META-INF/persistence.xml")
				.addPackages(true,"com.redhat.demos")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return archive;
	}

	@Test
	@UsingDataSet("person.yml")
	public void testInitialSize() {
		assertEquals(4, personService.getAll().size());
	}
	
	
	@Test
	@UsingDataSet("person.yml")
	@ShouldMatchDataSet(value = "person2.yml", excludeColumns = { "id" } , orderBy = { "id" })
	public void testAddPerson() {
		Person dummy = new Person();
		dummy.setFirstName("James");
		dummy.setLastName("Doe");
		dummy.setEmail("james@dummy.com");
		dummy.setPassword("1234567890");
		personService.registerPerson(dummy);
	}
	
}
