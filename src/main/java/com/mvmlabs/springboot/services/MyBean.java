package com.mvmlabs.springboot.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import com.mvmlabs.springboot.entity.Customer;
import com.mvmlabs.springboot.entity.MyUser;
import com.mvmlabs.springboot.entity.Person;
import com.mvmlabs.springboot.repository.PersonRepository;

@Component
public class MyBean {

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	@Qualifier("barEntityManagerFactory") 
	EntityManagerFactory barEntityManagerFactory;
	
	private static ThreadLocal<EntityManager> entityManagerThread = new ThreadLocal<EntityManager>();
	
	public EntityManager openSession() {
		EntityManager entityManager = barEntityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		return entityManager;
	}
	
	public EntityManager getEntityManager(){
		return entityManagerThread.get();
	}
	
	public void beginSession() {
		entityManagerThread.set(openSession());
	}
	
	public void commitSession() {
		try {
			entityManagerThread.get().getTransaction().commit();
			entityManagerThread.get().close();
			entityManagerThread.remove();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public <T> T find(Class<T> entityClass, String primaryKey){
		return find(entityClass, primaryKey, true);
	}
	
	public <T> T find(Class<T> entityClass, String primaryKey, boolean isAuto){
		try {
			if (isAuto) {
				beginSession();
			}
			EntityManager entityManager = getEntityManager();
			return (T) entityManager.find(entityClass, primaryKey);
		} finally{
			if (isAuto) {
				commitSession();
			}
		}
	}
	
	public <T> T find(Class<T> entityClass, Map<String, Object> restriction,boolean isAuto){
		try {
			if (isAuto) {
				beginSession();
			}
			EntityManager entityManager = getEntityManager();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
			Root<T> root = criteriaQuery.from(entityClass);
			List<Predicate> predicates =  new ArrayList<Predicate>();
			for(String key : restriction.keySet()){
				Predicate p = criteriaBuilder.conjunction();
		        p = criteriaBuilder.equal(root.get(key), restriction.get(key));
		        predicates.add(p);
			}
			
	        criteriaQuery.select(root);
	        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
	        T myUser = entityManager.createQuery(criteriaQuery).getSingleResult();
	        return myUser;
		} finally{
			if (isAuto) {
				commitSession();
			}
		}
	}
	
	public void test(){
//		EntityManager entityManager = barEntityManagerFactory.createEntityManager();
//		entityManager.getTransaction().begin();
//		Customer customer = entityManager.find(Customer.class, "1");
//		System.out.println("customer " + customer.getCustomerName());
//		entityManager.getTransaction().commit();
//		entityManager.close();
		beginSession();
		Customer customer = find(Customer.class, "1", false);
		System.out.println("customer " + customer.getCustomerName());
		System.out.println("customer " + customer.getOrders().get(0).getOrderDate());
		commitSession();

	}

	// ...

}