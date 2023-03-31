package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

@Named
public class DAOGeneric<E> implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private JPAUtil jpaUtil;
	
	@Inject
	private EntityManager entityManager;
	
	public void save(E entity) {

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		entityManager.persist(entity);

		entityTransaction.commit();

	}

	public E merge(E entity) {

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		E ret = entityManager.merge(entity);

		entityTransaction.commit();

		return ret;
	}

	public void delete(E entity) {

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		entityManager.remove(entity);

		entityTransaction.commit();

	}
	
	public void deleteId(E entity) {

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		Object id =jpaUtil.getPrimaryKey(entity);
		entityManager.createQuery("delete from " + entity.getClass().getCanonicalName() + " where id= " + id).executeUpdate();

		entityTransaction.commit();

	}
	
	public List<E> getListEntityLimit10(Class<E> entity) {
		
		EntityManager entityManager = jpaUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> ret = entityManager.createQuery("from " + entity.getName() + " order by id desc ").setMaxResults(10).getResultList();
		
		entityTransaction.commit();
		
		return ret;
	}
	
	public List<E> getListEntity(Class<E> entity) {
		
		EntityManager entityManager = jpaUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> ret = entityManager.createQuery("from " + entity.getName()).getResultList();
		
		entityTransaction.commit();
		
		return ret;
	}

	public E consult(Class<E> entity, String code) {

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		E objeto = (E) entityManager.find(entity, Long.parseLong(code));
		entityTransaction.commit();

		return objeto;
	}
	
}
