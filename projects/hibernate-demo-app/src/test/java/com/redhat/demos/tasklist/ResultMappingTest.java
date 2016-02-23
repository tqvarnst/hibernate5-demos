package com.redhat.demos.tasklist;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

@RunWith(Arquillian.class)
public class ResultMappingTest {

	@PersistenceContext(name="primary")
	EntityManager em;
	
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
	public void testResultMapping() {
		 List<Object[]> results = this.em.createNativeQuery("SELECT p.id, p.firstName, p.lastName, p.password, p.email, count(t.id) as openTaskCount FROM Task t JOIN Person p ON t.owner_id = p.id WHERE t.done = false GROUP BY p.id, p.firstName, p.lastName, p.password, p.email", "PersonTaskCountMapping").getResultList();
	        results.stream().forEach((record) -> {
	            Person person = (Person) record[0];
	            Long openTaskCount = (Long) record[1];
	            if(person.getId()==1001L) { // Thomas 
	            	assertEquals(2,openTaskCount.longValue());
	            }
	            if(person.getId()==1002L) { // Annika 
	            	assertEquals(1,openTaskCount.longValue());
	            }
	            
	            System.out.println("Person: ID [" + person.getId() + "] firstName [" + person.getFirstName() + "] lastName [" + person.getLastName() + "] password [" + person.getPassword() + "] number of tasks [" + openTaskCount + "]");
	        });
    }
}
	

