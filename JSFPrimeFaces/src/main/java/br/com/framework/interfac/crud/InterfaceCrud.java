package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public interface InterfaceCrud<T> extends Serializable {

	// Salva os dados
	void save(T obj) throws Exception;
	
	void persist(T obj) throws Exception;
	
	// Salva ou atualiza os dados
	void saveOrUpdate(T obj) throws Exception;
	
	// Faz o update dos dados
	void update(T obj) throws Exception;
	
	// Delete de dados
	void delete(T obj) throws Exception;
	
	// Salva ou atualiza e retorna o objeto em estado persistente
	T merge(T obj) throws Exception;
	
	// Carrega a lista  de dados de determinada classe
	List<T> findList(Class<T> objs) throws Exception;
	
	Object findByID(Class<T> entidade, Long id) throws Exception;
	
	T findById(Class<T> entidade, String id) throws Exception;

	List<T> findListByQuery(String s) throws Exception;
	
	// Executar update com HQL
	void executeUpdateQuery(String s) throws Exception;
	
	// Executar update com SQL
	void executeUpdateSQL(String s) throws Exception;
	
	// Limpa a sessao do Hibernate
	void clearSession() throws Exception;
	
	// Retira um objeto da sessao do Hibernate
	void evict(Object objs) throws Exception;
	
	Session getSession() throws Exception;
	
	List<?> getListSQL(String sql) throws Exception;
	
	// JDBC do Spring
	JdbcTemplate getJdbcTemplate();
	
	SimpleJdbcTemplate getSimpleJdbcTemplate();
	
	SimpleJdbcInsert getSimpleJdbcInsert();
	
	Long totalRegistro(String table) throws Exception;
	
	Query obterQuery(String query) throws Exception;
	
	// Paginacao
	List<T> findListByQuery(String query, int iniciaNoRegistro, int maximoResultado) throws Exception;
	
}
