package br.com.project.bean.view;

import java.util.ArrayList;
import java.util.List;

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
import br.com.project.model.classes.Cidade;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.Mensagem;
import br.com.project.util.all.Messages;

@Controller
@Scope("session")
@ManagedBean(name = "funcionarioBeanView")
public class FuncionarioBeanView extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private String urlFind = "/cadastro/find_funcionario.jsf?faces-redirect=true";
	private String urlNew = "/cadastro/cad_funcionario.jsf?faces-redirect=true";

	private Entidade objetoSelecionado = new Entidade();
	
	private List<Entidade> lista = new ArrayList<Entidade>();
	
	@Autowired
	private ContextoBean contextoBean;
	
	@Autowired
	private EntidadeController entidadeController;
	
	private CarregamentoLazyListForObject<Entidade> list = new CarregamentoLazyListForObject<Entidade>();
	
	public List<Entidade> getLista() throws Exception {
		lista = entidadeController.findList(Entidade.class);
		return lista;
	}
	
	@Override
	protected Class<?> getClassImpl() {
		return Entidade.class;
	}

	@Override
	protected InterfaceCrud<?> getController() {
		return entidadeController;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return " and entity.tipoEntidade = 'FUNCIONARIO'";
	}

	public Entidade getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Entidade objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	public void setList(CarregamentoLazyListForObject<Entidade> list) {
		this.list = list;
	}
	
	public CarregamentoLazyListForObject<Entidade> getList() {
		return list;
	}
	
	@Override
	public void consultarEntidade() throws Exception {
		objetoSelecionado = new Entidade();
		list.clean();
		list.setTotalRegistroConsulta(super.totalRegistroConsulta(), super.getSqlLazyQuery());
	}

	@Override
	public String novo() throws Exception {
		objetoSelecionado = new Entidade();
		return urlNew;
	}
	
	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public EntidadeController getEntidadeController() {
		return entidadeController;
	}

	public void setEntidadeController(EntidadeController entidadeController) {
		this.entidadeController = entidadeController;
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
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
		if(objetoSelecionado.getEnt_codigo() != null && objetoSelecionado.getEnt_codigo() > 0) {
			entidadeController.delete(objetoSelecionado);
			list.remove(objetoSelecionado);
			objetoSelecionado = new Entidade();
			sucesso();
		}
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		if(objetoSelecionado.getAcessos().contains("USER")) {
			objetoSelecionado.getAcessos().add("USER");
		}
		
		if(entidadeController.existeCpf(objetoSelecionado.getCpf())) {
			Messages.msgSeverityError("CPF ja existe cadastrado");
			return;
		}
		entidadeController.merge(objetoSelecionado);
		objetoSelecionado = new Entidade();
		sucesso();
	}
	
	@Override
	public void saveEdit() throws Exception {
		list.getList().clear();
		objetoSelecionado = entidadeController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		objetoSelecionado = new Entidade();
		sucesso();
	}
	
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_funcionario");
		super.setNomeRelatorioSaida("report_funcionario");
		super.setListDataBeanCollectionReport(entidadeController.findList(Entidade.class));
		return super.getArquivoReport();
	}
}
