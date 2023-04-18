package br.com.project.bean.view;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.geral.controller.MensagemController;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.Mensagem;

@Controller
@Scope(value = "session")
@ManagedBean(name = "mensagemBeanView")
public class MensagemBeanView  extends BeanManagedViewAbstract{

	private static final long serialVersionUID = 1L;

	private Mensagem objetoSelecionado = new Mensagem();

	@Autowired
	private EntidadeController entidadeController;
	
	@Autowired
	private MensagemController mensagemController;
	
	@Autowired
	private ContextoBean contextoBean;
	
	@Override
	public void saveNotReturn() throws Exception {
		if(objetoSelecionado.getUsr_destino().getEnt_codigo().equals(objetoSelecionado.getUsr_origem().getEnt_codigo())) {
			addMsg("Origem nao pode ser igual ao destino.");
			return;
		}
		mensagemController.merge(objetoSelecionado);
		novo();
		addMsg("Mensagem enviada com sucesso!");
	}
	
	@Override
	public String novo() throws Exception {
		objetoSelecionado = new Mensagem();
		objetoSelecionado.setUsr_origem(contextoBean.getEntidadeLogada());
		return "";
	}

	public Mensagem getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Mensagem objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	@Override
	protected Class<?> getClassImpl() {
		return null;
	}

	@Override
	protected InterfaceCrud<?> getController() {
		return null;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping("**/buscarUsuarioDestinoMsg")
	public void buscarUsuarioDestinoMsg(HttpServletResponse httpServletResponse, @RequestParam(value = "codEntidade") Long codEntidade) throws Exception {
		Entidade entidade = (Entidade) entidadeController.findByID(Entidade.class, codEntidade);
		if(entidade != null) {
			objetoSelecionado.setUsr_destino(entidade);
			httpServletResponse.getWriter().write(entidade.getJson().toString());
		}
	}
	
	@Override
	public List<SelectItem> getListaCampoPesquisa() {
		return super.getListaCampoPesquisa();
	}
}
