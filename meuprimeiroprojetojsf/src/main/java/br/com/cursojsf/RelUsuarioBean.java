package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DAOGeneric;
import br.com.entity.Launch;
import br.com.entity.Person;
import br.com.repository.IDAOPerson;

@ViewScoped
@Named(value = "relUsuarioBean")
public class RelUsuarioBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Date dataInicial;
	private Date dataFinal;
	private String name;
	private List<Person> people = new ArrayList<Person>();
	
	@Inject
	private DAOGeneric<Person> daoGeneric;
	
	@Inject
	private IDAOPerson idaoPerson;

	public List<Person> getPeople() {
		return people;
	}

	public void setPeople(List<Person> people) {
		this.people = people;
	}
	
	public void relPerson() {
		if (dataInicial == null && dataFinal == null && name == null) {
			people = daoGeneric.getListEntity(Person.class);
		} else {
			people = idaoPerson.relPerson(name, dataInicial, dataFinal);
		}
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

	
}
