package br.com.project.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.carregamento.lazy.CarregamentoLazyListForObject;
import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.CidadeController;
import br.com.project.model.classes.Cidade;

@Controller
@Scope(value = "session")
@ManagedBean(name = "cidadeBeanView")
public class CidadeBeanView extends BeanManagedViewAbstract{

	private static final long serialVersionUID = 1L;

	private String url = "/cadastro/cad_cidade.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/find_cidade.jsf?faces-redirect=true";
	
	private Cidade objetoSelecionado = new Cidade();
	
	private List<Cidade> lista = new ArrayList<Cidade>();
 	
	private CarregamentoLazyListForObject<Cidade> list = new CarregamentoLazyListForObject<Cidade>();
	
	@Resource
	private CidadeController cidadeController;

	public List<Cidade> getLista() throws Exception {
		lista = cidadeController.findList(getClassImpl());
		return lista;
	}
	
	public CarregamentoLazyListForObject<Cidade> getList() throws Exception {
		return list;
	}
	
	@Override
	public void consultarEntidade() throws Exception {
		objetoSelecionado = new Cidade();
		list.getList().clear();
		list.setTotalRegistroConsulta(super.totalRegistroConsulta(), super.getSqlLazyQuery());
		super.consultarEntidade();
	}
	
	@Override
	public String editar() throws Exception {
		list.getList().clear();
		return url;
	}
	
	@Override
	public void excluir() throws Exception {
		objetoSelecionado = (Cidade) cidadeController.getSession().get(getClassImpl(), objetoSelecionado.getCid_codigo());
		cidadeController.delete(objetoSelecionado);
		list.remove(objetoSelecionado);
		novo();
		sucesso();
	}
	
	public Cidade getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Cidade objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}
	
	@Override
	public String save() throws Exception {
		objetoSelecionado = cidadeController.merge(objetoSelecionado);
		return "";
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		list.getList().clear();
		objetoSelecionado = cidadeController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		objetoSelecionado = new Cidade();
		sucesso();
	}
	
	@Override
	public void saveEdit() throws Exception {
		saveNotReturn();
	}
	
	@Override
	public String novo() throws Exception {
		setarVariaveisNulas();
		return url;
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		list.getList().clear();
		objetoSelecionado = new Cidade();
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return urlFind;
	}
	
	@Override
	public String redirecionarNewEntidade() throws Exception {
		setarVariaveisNulas();
		return url;
	}
	
	@Override
	protected Class<Cidade> getClassImpl() {
		return Cidade.class;
	}

	@Override
	protected InterfaceCrud<Cidade> getController() {
		return cidadeController;
	}
	
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_cidade");
		super.setNomeRelatorioSaida("report_cidade");
		super.setListDataBeanCollectionReport(cidadeController.findList(getClassImpl()));
		return super.getArquivoReport();
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}
	
}
