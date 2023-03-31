package br.com.repository;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entity.Launch;

@Named
public class IDAOLaunchImpl implements IDAOLaunch, Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager entityManager;

	@Override
	public List<Launch> consultLimit10(Long codUser) {
		List<Launch> list = null;

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		list = entityManager.createQuery(" from Launch where usuario.id = " + codUser + " order by id desc")
				.setMaxResults(10).getResultList();

		transaction.commit();

		return list;
	}

	@Override
	public List<Launch> consult(Long codUser) {
		List<Launch> list = null;

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		list = entityManager.createQuery(" from Launch where usuario.id = " + codUser).getResultList();

		transaction.commit();

		return list;
	}

	@Override
	public List<Launch> relLaunch(String numNota, Date dataInicial, Date dataFinal) {

		List<Launch> releases = new ArrayList<Launch>();

		StringBuilder sql = new StringBuilder();
		sql.append(" select l from Launch l ");

		if (dataInicial == null && dataFinal == null && numNota != null && !numNota.isEmpty()) {
			sql.append(" where l.numeroNotaFiscal = '").append(numNota.trim()).append("'");
		} else if (numNota == null
				|| (numNota != null && numNota.isEmpty()) && dataInicial != null && dataFinal == null) {
			String dataInicialString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			sql.append(" where l.dataInicial >= '").append(dataInicialString).append("'");
		} else if (numNota == null
				|| (numNota != null && numNota.isEmpty()) && dataInicial == null && dataFinal != null) {
			String dataFinalString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
			sql.append(" where l.dataFinal <= '").append(dataFinalString).append("'");
		} else if (numNota == null
				|| (numNota != null && numNota.isEmpty()) && dataInicial != null && dataFinal != null) {
			String dataFinalString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
			String dataInicialString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			sql.append(" where l.dataInicial >= '").append(dataInicialString).append("' ")
					.append(" and l.dataFinal <= '").append(dataFinalString).append("'");
		} else if (numNota != null && !numNota.isEmpty() && dataInicial != null && dataFinal != null) {
			String dataFinalString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
			String dataInicialString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			sql.append(" where l.dataInicial >= '").append(dataInicialString).append("' ")
					.append(" and l.dataFinal <= '").append(dataFinalString).append("' ").append(" and l.numeroNotaFiscal = '").append(numNota.trim()).append("'");
		}

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		releases = entityManager.createQuery(sql.toString()).getResultList();

		transaction.commit();
		return releases;
	}

}
