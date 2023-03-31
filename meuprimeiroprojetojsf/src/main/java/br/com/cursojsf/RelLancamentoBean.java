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
import br.com.repository.IDAOLaunch;

@ViewScoped
@Named(value = "relLancamentoBean")
public class RelLancamentoBean implements Serializable{

	@Inject
	private IDAOLaunch idaoLaunch;
	
	private static final long serialVersionUID = 1L;

	private List<Launch> releases = new ArrayList<Launch>();

	@Inject
	private DAOGeneric<Launch> daoGeneric;
	
	private Date dataInicial;
	private Date dataFinal;
	private String numNota;
	
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

	public String getNumNota() {
		return numNota;
	}

	public void setNumNota(String numNota) {
		this.numNota = numNota;
	}

	public List<Launch> getReleases() {
		return releases;
	}

	public void setReleases(List<Launch> releases) {
		this.releases = releases;
	}
	
	public void searchLaunch() {
		if (dataInicial == null && dataFinal == null && numNota == null) {
			releases = daoGeneric.getListEntity(Launch.class);
		} else {
			releases = idaoLaunch.relLaunch(numNota, dataInicial, dataFinal);
		}
		
	}

	
}
