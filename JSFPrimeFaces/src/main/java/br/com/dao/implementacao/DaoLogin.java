package br.com.dao.implementacao;

import javax.persistence.EntityManager;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.repository.interfaces.RepositoryLogin;

public class DaoLogin extends ImplementacaoCrud<Object> implements RepositoryLogin {

	private static final long serialVersionUID = 1L;
	EntityManager entityManager;
	@Override
	public boolean autentico(String login, String senha) throws Exception {
		String sql = "SELECT count(1) as autentica FROM entidade where ent_login  = ? and ent_senha = ? ";
		Integer sqlRowSet = super.getJdbcTemplate().queryForInt(sql, new Object[] {login, senha});
		if(sqlRowSet == 1) {
			return true;
		}
		return false;
	}

}
