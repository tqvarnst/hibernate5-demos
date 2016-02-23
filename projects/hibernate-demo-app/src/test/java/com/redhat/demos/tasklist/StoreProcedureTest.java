package com.redhat.demos.tasklist;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class StoreProcedureTest {

	@PersistenceContext(name="primary")
	protected EntityManager em;
	
	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
				.addAsResource("sp.sql","import.sql")
				.addAsResource("META-INF/persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");;
		return archive;
	}

	
	@Test
	public void testCallStoreProcedure() {
		
		StoredProcedureQuery query = em.createStoredProcedureQuery("my_sum");
		query.registerStoredProcedureParameter("x",Integer.class,ParameterMode.IN);
		query.registerStoredProcedureParameter("y",Integer.class,ParameterMode.IN);
		query.registerStoredProcedureParameter("sum",Integer.class,ParameterMode.OUT);
		
		query.setParameter("x", 5);
		query.setParameter("y", 4);
		query.execute();
		Integer sum = (Integer) query.getOutputParameterValue("sum");
		assertEquals(sum, new Integer(9));
	}
	
	@Test
	public void testCallStoreProcedure2() {
		StoredProcedureQuery query = em.createStoredProcedureQuery("add_three_values");
		query.registerStoredProcedureParameter("v1",Integer.class,ParameterMode.IN);
		query.registerStoredProcedureParameter("v2",Integer.class,ParameterMode.IN);
		query.registerStoredProcedureParameter("v3",Integer.class,ParameterMode.IN);
		query.registerStoredProcedureParameter("sum",Integer.class,ParameterMode.OUT);
		
		query.setParameter("v1", 5);
		query.setParameter("v2", 4);
		query.setParameter("v3", 1);
		query.execute();
		Integer sum = (Integer) query.getOutputParameterValue("sum");
		assertEquals(sum, new Integer(10));
	}
	
	

}
