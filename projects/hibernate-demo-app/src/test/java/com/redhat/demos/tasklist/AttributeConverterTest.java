package com.redhat.demos.tasklist;

import static org.junit.Assert.assertEquals;

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
import com.redhat.demos.tasklist.service.PersonService;

@RunWith(Arquillian.class)
public class AttributeConverterTest {

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
	public void testPasswordConverter() {
		Person person = personService.getPerson(1001L);
		assertEquals("John", person.getFirstName());
		assertEquals("1234567890", person.getPassword()); //This value is converted from MTIzNDU2Nzg5MA== which is stored in the DB
	}
}
