package com.redhat.demos.tasklist;

import static org.junit.Assert.assertEquals;

import java.time.Month;
import java.util.Set;

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
import com.redhat.demos.tasklist.model.Task;
import com.redhat.demos.tasklist.service.PersonService;

@RunWith(Arquillian.class)
public class PersonSerivceTest {

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
	public void testPasswordConverter() {
		Person person = personService.getPerson(1001L);
		assertEquals("Thomas", person.getFirstName());
		assertEquals("1234567890", person.getPassword());
	}
	
	@Test
	@UsingDataSet("person.yml")
	@ShouldMatchDataSet(value = "person_after_adding_erik.yml", excludeColumns = { "id" } , orderBy = { "id" })
	public void testAddPerson() {
		Person dummy = new Person();
		dummy.setFirstName("Erik");
		dummy.setLastName("Qvarnstrom");
		dummy.setEmail("erik@dummy.com");
		dummy.setPassword("1234567890");
		personService.registerPerson(dummy);
	}
	
	@Test
	@UsingDataSet({"person.yml","task.yml"})
	public void testPersonTask() {
		Person person = personService.getPerson(1001L);
		Set<Task> tasks = person.getTasks();
		assertEquals(3,tasks.size());
		tasks.stream().forEach(t -> {
			if(t.getId() == 2001) {				
				assertEquals(2016,t.getCreationDate().getYear());
				assertEquals(Month.FEBRUARY,t.getCreationDate().getMonth());
				assertEquals(11,t.getCreationDate().getDayOfMonth());
				assertEquals(11,t.getCreationDate().getHour());
				assertEquals(46,t.getCreationDate().getMinute());
				assertEquals(23,t.getCreationDate().getSecond());
			}
		});
		
	}
}
