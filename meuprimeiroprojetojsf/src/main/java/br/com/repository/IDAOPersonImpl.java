package br.com.repository;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entity.Estados;
import br.com.entity.Launch;
import br.com.entity.Person;
import br.com.jpautil.JPAUtil;

@Named
public class IDAOPersonImpl implements IDAOPerson, Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager entityManager;

	@Override
	public Person SearchUser(String login, String password) {

		Person person = null;

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		try {
			person = (Person) entityManager.createQuery(
					"select p from Person p where p.login = '" + login + "' and p.password = '" + password + "'")
					.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
		}	

		entityTransaction.commit();

		return person;
	}

	@Override
	public List<SelectItem> listaEstados() {

		List<SelectItem> selectItems = new ArrayList<SelectItem>();

		List<Estados> estados = entityManager.createQuery("from Estados").getResultList();

		for (Estados estado : estados) {
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}

		return selectItems;
	}

	@Override
	public List<Person> relPerson(String name, Date dataInicial, Date dataFinal) {


		List<Person> people = new ArrayList<Person>();

		StringBuilder sql = new StringBuilder();
		sql.append(" select l from Person l ");

		if (dataInicial == null && dataFinal == null && name != null && !name.isEmpty()) {
			sql.append(" where upper(l.name) like '%").append(name.trim().toUpperCase()).append("%'");
		} else if (name == null
				|| (name != null && name.isEmpty()) && dataInicial != null && dataFinal == null) {
			String dataInicialString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			sql.append(" where l.dateBirth >= '").append(dataInicialString).append("'");
		} else if (name == null
				|| (name != null && name.isEmpty()) && dataInicial == null && dataFinal != null) {
			String dataFinalString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
			sql.append(" where l.dateBirth <= '").append(dataFinalString).append("'");
		} else if (name == null
				|| (name != null && name.isEmpty()) && dataInicial != null && dataFinal != null) {
			String dataFinalString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
			String dataInicialString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			sql.append(" where l.dateBirth >= '").append(dataInicialString).append("' ")
					.append(" and l.dateBirth <= '").append(dataFinalString).append("'");
		} else if (name != null && !name.isEmpty() && dataInicial != null && dataFinal != null) {
			String dataFinalString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
			String dataInicialString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			sql.append(" where l.dateBirth >= '").append(dataInicialString).append("' ")
					.append(" and l.dateBirth <= '").append(dataFinalString).append("' ").append(" and upper(l.name) like '%").append(name.trim().toUpperCase()).append("%'");
		}

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		people = entityManager.createQuery(sql.toString()).getResultList();

		transaction.commit();
		return people;
	}

}
