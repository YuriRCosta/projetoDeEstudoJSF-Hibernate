package br.com.project.bean.view;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.carregamento.lazy.CarregamentoLazyListForObject;
import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.geral.controller.SessionController;
import br.com.project.geral.controller.TituloController;
import br.com.project.model.classes.Cidade;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.Mensagem;
import br.com.project.model.classes.Titulo;
import br.com.project.util.all.Messages;

@Controller
@Scope("session")
@ManagedBean(name = "tituloBeanView")
public class TituloBeanView extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private String urlFind = "/cadastro/find_titulo.jsf?faces-redirect=true";
	private String urlNew = "/cadastro/cad_titulo.jsf?faces-redirect=true";

	private Titulo objetoSelecionado = new Titulo();
	
	private List<Titulo> lista = new ArrayList<Titulo>();
	
	@Autowired
	private EntidadeController entidadeController;
	
	@Autowired
	private TituloController tituloController;
	
	@Autowired
	private ContextoBean contextoBean;
	
	private CarregamentoLazyListForObject<Titulo> list = new CarregamentoLazyListForObject<Titulo>();
	
	public List<Titulo> getLista() throws Exception {
		lista = tituloController.findList(Titulo.class);
		return lista;
	}
	
	@Override
	protected Class<?> getClassImpl() {
		return Titulo.class;
	}

	@Override
	protected InterfaceCrud<?> getController() {
		return tituloController;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}

	public Titulo getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Titulo objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	public void setList(CarregamentoLazyListForObject<Titulo> list) {
		this.list = list;
	}
	
	public CarregamentoLazyListForObject<Titulo> getList() {
		return list;
	}
	
	@Override
	public void consultarEntidade() throws Exception {
		objetoSelecionado = new Titulo();
		list.clean();
		list.setTotalRegistroConsulta(super.totalRegistroConsulta(), super.getSqlLazyQuery());
	}

	@Override
	public String novo() throws Exception {
		objetoSelecionado = new Titulo();
		init();
		return urlNew;
	}
	
	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public TituloController gettituloController() {
		return tituloController;
	}

	public void settituloController(TituloController tituloController) {
		this.tituloController = tituloController;
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		novo();
		return urlFind;
	}
	
	@Override
	public String redirecionarNewEntidade() throws Exception {
		return urlNew;
	}
	
	@Override
	public String editar() throws Exception {
		return urlNew;
	}
	
	@Override
	public void excluir() throws Exception {
		if(objetoSelecionado.getTit_codigo() != null && objetoSelecionado.getTit_codigo() > 0) {
			tituloController.delete(objetoSelecionado);
			list.remove(objetoSelecionado);
			objetoSelecionado = new Titulo();
			sucesso();
		}
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		tituloController.merge(objetoSelecionado);
		objetoSelecionado = new Titulo();
		sucesso();
	}
	
	@Override
	public void saveEdit() throws Exception {
		list.getList().clear();
		objetoSelecionado = tituloController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		objetoSelecionado = new Titulo();
		sucesso();
	}
	
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_titulo");
		super.setNomeRelatorioSaida("report_titulo");
		super.setListDataBeanCollectionReport(tituloController.findList(Titulo.class));
		return super.getArquivoReport();
	}
	
	@PostConstruct
	public void init() throws Exception {
		objetoSelecionado.setEnt_codigoabertura(contextoBean.getEntidadeLogada());
	}
	
	public List<Entidade> pesquisarPagador(String nome) throws Exception {
		return entidadeController.pesquisarPorNome(nome);
	}
}
