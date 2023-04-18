package br.com.framework.implementacao.crud;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.record.formula.functions.T;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.model.classes.Entidade;

@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud<T> {

	private static final long serialVersionUID = 1L;

	public static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	@Resource
	private JdbcTemplateImpl jdbcTemplate;

	@Resource
	private SimpleJdbcTemplateImpl simpleJdbcTemplate;

	@Resource
	private SimpleJdbcInsertImpl simpleJdbcInsert;

	@Resource
	private SimpleJdbcClassImpl simpleJdbcClass;

	public SimpleJdbcClassImpl getSimpleJdbcClass() {
		return simpleJdbcClass;
	}

	@Override
	public void save(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlushSession();
	}

	@Override
	public void persist(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFlushSession();
	}

	@Override
	public void saveOrUpdate(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFlushSession();
	}

	@Override
	public void update(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().update(obj);
		executeFlushSession();
	}

	@Override
	public void delete(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlushSession();
	}

	@Override
	public T merge(T obj) throws Exception {
		validarSessionFactory();
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFlushSession();
		return obj;
	}

	@Override
	public List<T> findList(Class<T> entidade) throws Exception {
		validarSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append(" select distinct(entity) from ").append(entidade.getSimpleName()).append(" entity ");
		List<T> list = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		return list;
	}

	@Override
	public Object findByID(Class<T> entidade, Long id) throws Exception {
		validarSessionFactory();
		Object obj = sessionFactory.getCurrentSession().load(entidade, id);
		return obj;
	}

	private void validaTransaction() {
		if (!sessionFactory.getCurrentSession().getTransaction().isActive())
			sessionFactory.getCurrentSession().beginTransaction();
	}

	private void validaSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}

		validaTransaction();
	}

	public T findUninqueByProperty(Class<T> entidade, Object valor, String atributo, String condicao) throws Exception {
		validaSessionFactory();
		StringBuilder query = new StringBuilder();

		query.append("select entity from ").append(entidade.getSimpleName()).append(" entity where entity.")
				.append(atributo).append(" = '").append(valor).append("' ").append(condicao);

		T obj = (T) this.findUniqueByQueryDinamica(query.toString());

		return obj;

	}

	@Override
	public T findById(Class<T> entidade, String id) throws Exception {
		validarSessionFactory();
		T obj = (T) sessionFactory.getCurrentSession().load(entidade, Long.parseLong(id));
		return obj;
	}

	@Override
	public List<T> findListByQuery(String s) throws Exception {
		validarSessionFactory();
		List<T> list = new ArrayList<T>();
		list = sessionFactory.getCurrentSession().createQuery(s).list();
		return list;
	}

	@Override
	public void executeUpdateQuery(String s) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void executeUpdateSQL(String s) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void clearSession() throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().clear();
		executeFlushSession();
	}

	@Override
	public void evict(Object objs) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().evict(objs);
		executeFlushSession();
	}

	@Override
	public Session getSession() throws Exception {
		validarSessionFactory();
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<?> getListSQL(String sql) throws Exception {
		validarSessionFactory();
		List<?> list = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return list;
	}

	public List<Object[]> getListSQLArray(String sql) throws Exception {
		validarSessionFactory();
		List<Object[]> list = (List<Object[]>) sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return list;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcInsert;
	}

	@Override
	public Long totalRegistro(String table) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(1) from ").append(table);
		return jdbcTemplate.queryForLong(sql.toString());
	}

	@Override
	public Query obterQuery(String query) throws Exception {
		validarSessionFactory();
		Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());
		return queryReturn;
	}

	@Override
	public List<T> findListByQuery(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		validarSessionFactory();
		List<T> list = new ArrayList<T>();
		list = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(iniciaNoRegistro)
				.setMaxResults(maximoResultado).list();
		return list;
	}

	private void validarTransaction() {
		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}

	public void commitProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}

	public void rollBackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();
	}

	private void validarSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}

		validarTransaction();
	}

	private void executeFlushSession() {
		sessionFactory.getCurrentSession().flush();
	}

	public T findUniqueByQueryDinamica(String query) throws Exception {
		validarSessionFactory();
		T obj = (T) sessionFactory.getCurrentSession().createQuery(query.toString()).uniqueResult();
		return obj;
	}

	public List<T> findListByQueryDinamica(String query) throws Exception {
		validarSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		return lista;

	}

	public List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		validarSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(iniciaNoRegistro).setMaxResults(maximoResultado).list();
		return lista;

	}

	public T findInuqueByProperty(Class<T> entidade, Object valor, String atributo, String condicao) throws Exception {
		validarSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append(" select entity from ").append(entidade.getSimpleName()).append(" entity where entity.")
				.append(atributo).append(" = '").append(valor).append("' ").append(condicao);

		T obj = (T) this.findUniqueByQueryDinamica(query.toString());
		return obj;
	}

}
