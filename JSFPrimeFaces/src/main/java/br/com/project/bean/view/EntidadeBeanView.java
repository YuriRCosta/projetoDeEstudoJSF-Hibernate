package br.com.project.bean.view;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.bean.geral.EntidadeAtualizaSenhaBean;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.model.classes.Entidade;

@Controller
@Scope(value = "session")
@ManagedBean(name = "entidadeBeanView")
public class EntidadeBeanView extends BeanManagedViewAbstract{

	private static final long serialVersionUID = 1L;

	private EntidadeAtualizaSenhaBean entidadeAtualizaSenhaBean = new EntidadeAtualizaSenhaBean();
	
	@Resource
	private EntidadeController entidadeController;
	
	@Resource
	private ContextoBean contextoBean;
	
	public void updateSenha() throws Exception {
		Entidade entidadeLogada = contextoBean.getEntidadeLogada();
		if(!entidadeAtualizaSenhaBean.getSenhaAtual().equals(entidadeLogada.getEnt_senha())) {
			addMsg("A senha atual nao e valida.");
			return;
		} else if(entidadeAtualizaSenhaBean.getSenhaAtual().equals(entidadeAtualizaSenhaBean.getNovaSenha())) {
			addMsg("A nova senha nao pode ser igual a senha atual.");
			return;
		} else if(!entidadeAtualizaSenhaBean.getNovaSenha().equals(entidadeAtualizaSenhaBean.getConfirmaSenha())) {
			addMsg("A nova senha e a confirmacao nao conferem.");
			return;
		} else {
			entidadeLogada.setEnt_senha(entidadeAtualizaSenhaBean.getNovaSenha());
			entidadeController.saveOrUpdate(entidadeLogada);
			entidadeLogada = (Entidade) entidadeController.findByID(Entidade.class, entidadeLogada.getEnt_codigo());
			if(entidadeLogada.getEnt_senha().equals(entidadeAtualizaSenhaBean.getNovaSenha())) {
				sucesso();
			} else {
				addMsg("A nova senha nao foi atualizada");
				error();
			}
		}
	}
	
	public String getUsuarioLogadoSecurity() {
		contextoBean.getAuthentication().getName();
		return null;
	}
	
	public EntidadeAtualizaSenhaBean getEntidadeAtualizaSenhaBean() {
		return entidadeAtualizaSenhaBean;
	}

	public void setEntidadeAtualizaSenhaBean(EntidadeAtualizaSenhaBean entidadeAtualizaSenhaBean) {
		this.entidadeAtualizaSenhaBean = entidadeAtualizaSenhaBean;
	}

	@Override
	protected Class<Entidade> getClassImpl() {
		return Entidade.class;
	}

	@Override
	protected InterfaceCrud<Entidade> getController() {
		return entidadeController;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
