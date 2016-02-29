package com.redhat.demos.tasklist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Month;
import java.util.Set;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
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
public class LocalDateTimeTest {

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
	@UsingDataSet({"person.yml","task.yml"})
	public void testLocalDateTime() {
		Person person = personService.getPerson(1001L);
		Set<Task> tasks = person.getTasks();
		assertEquals(3,tasks.size());
		Task task = tasks.stream().filter(t -> t.getId() == 2001).findAny().get();
		assertNotNull(task);
		assertEquals(2016,task.getCreationDate().getYear());
		assertEquals(Month.FEBRUARY,task.getCreationDate().getMonth());
		assertEquals(11,task.getCreationDate().getDayOfMonth());
		assertEquals(11,task.getCreationDate().getHour());
		assertEquals(46,task.getCreationDate().getMinute());
		assertEquals(23,task.getCreationDate().getSecond());	
	}
}
