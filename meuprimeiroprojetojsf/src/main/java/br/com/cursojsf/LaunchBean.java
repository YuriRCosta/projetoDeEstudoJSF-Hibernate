package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.dao.DAOGeneric;
import br.com.entity.Launch;
import br.com.entity.Person;
import br.com.repository.IDAOLaunch;
import br.com.repository.IDAOLaunchImpl;

@javax.faces.view.ViewScoped
@Named(value = "launchBean")
public class LaunchBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Launch launch = new Launch();
	@Inject
	private DAOGeneric<Launch> daoGeneric;
	
	private List<Launch> releases = new ArrayList<Launch>();
	
	@Inject
	private IDAOLaunch idaoLaunch;
	
	public Launch getLaunch() {
		return launch;
	}
	public void setLaunch(Launch launch) {
		this.launch = launch;
	}
	public DAOGeneric<Launch> getDaoGeneric() {
		return daoGeneric;
	}
	public void setDaoGeneric(DAOGeneric<Launch> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	public List<Launch> getRealeses() {
		return releases;
	}
	public void setRealeses(List<Launch> realeses) {
		this.releases = realeses;
	}
	
	public String save() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Person personUser = (Person) externalContext.getSessionMap().get("userLogged");
		
		launch.setUsuario(personUser);
		daoGeneric.save(launch);
		
		updateReleases();
		launch = new Launch();
		FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Salvo com sucesso"));

		return "lancamento.jsf";
	}
	
	@PostConstruct
	private void updateReleases() {
	
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Person personUser = (Person) externalContext.getSessionMap().get("userLogged");
		
		releases = idaoLaunch.consultLimit10(personUser.getId());
		
	}
	
	public String remove() {
		
		System.out.println("chamou");
		daoGeneric.deleteId(launch);
		launch = new Launch();
		updateReleases();
		FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Excluido com sucesso"));
		return "";
	}
	
	public String newNotaFiscal() {

		launch = new Launch();
		
		return "";
	}
}
