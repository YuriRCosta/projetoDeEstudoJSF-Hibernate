package br.com.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.faces.model.SelectItem;

import java.util.Date;
import java.util.List;
import br.com.entity.Person;
import br.com.jpautil.JPAUtil;

public interface IDAOPerson {

	public Person SearchUser(String login, String password);
	
	List<SelectItem> listaEstados();

	public List<Person> relPerson(String name, Date dataInicial, Date dataFinal);
}
