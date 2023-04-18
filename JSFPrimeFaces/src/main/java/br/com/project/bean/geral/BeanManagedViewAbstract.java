package br.com.project.bean.geral;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.Id;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.annotation.IdentificaCampoPesquisa;
import br.com.project.enums.CondicaoPesquisa;
import br.com.project.report.util.BeanReportView;
import br.com.project.util.all.UtilitariaRegex;

@Component
public abstract class BeanManagedViewAbstract extends BeanReportView{

	private static final long serialVersionUID = 1L;

	protected abstract Class<?> getClassImpl();
	
	protected abstract InterfaceCrud<?> getController();
	
	public List<SelectItem> listaCampoPesquisa;
	
	public List<SelectItem> listaCondicaoPesquisa;
	
	public CondicaoPesquisa condicaoPesquisaSelecionado;
	
	public String valorPesquisa;
	
	public abstract String condicaoAndParaPesquisa() throws Exception;
	
	public ObjetoCampoConsulta objetoCampoConsultaSelecionado;

	public CondicaoPesquisa getCondicaoPesquisaSelecionado() {
		return condicaoPesquisaSelecionado;
	}

	public String getValorPesquisa() {
		return valorPesquisa != null ? valorPesquisa : "";
	}

	public void setValorPesquisa(String valorPesquisa) {
		this.valorPesquisa = valorPesquisa;
	}

	public void setCondicaoPesquisaSelecionado(CondicaoPesquisa condicaoPesquisaSelecionado) {
		this.condicaoPesquisaSelecionado = condicaoPesquisaSelecionado;
	}

	public ObjetoCampoConsulta getObjetoCampoConsultaSelecionado() {
		return objetoCampoConsultaSelecionado;
	}
	
	public List<SelectItem> getListaCondicaoPesquisa() {
		listaCondicaoPesquisa = new ArrayList<SelectItem>();
		for (CondicaoPesquisa condicaoPesquisa : CondicaoPesquisa.values()) {
			listaCondicaoPesquisa.add(new SelectItem(condicaoPesquisa, condicaoPesquisa.toString()));
		}
		
		return listaCondicaoPesquisa;
	}

	public void setObjetoCampoConsultaSelecionado(ObjetoCampoConsulta objetoCampoConsultaSelecionado) {
		if(objetoCampoConsultaSelecionado != null) 
			for (Field field : getClassImpl().getDeclaredFields()) {
				if(field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {
					if(objetoCampoConsultaSelecionado.getCampoBanco().equalsIgnoreCase(field.getName())) {
						String descricaoCampo = field.getAnnotation(IdentificaCampoPesquisa.class).descricaoCampo();
						objetoCampoConsultaSelecionado.setDescricao(descricaoCampo);
						objetoCampoConsultaSelecionado.setTipoClass(field.getType().getCanonicalName());
						objetoCampoConsultaSelecionado.setPrincipal(field.getAnnotation(IdentificaCampoPesquisa.class).principal());
						break;
					}
				}
			}
		
		
		this.objetoCampoConsultaSelecionado = objetoCampoConsultaSelecionado;
	}
	
	public List<SelectItem> getListaCampoPesquisa() {
		listaCampoPesquisa = new ArrayList<SelectItem>();
		List<ObjetoCampoConsulta> listTemp = new ArrayList<ObjetoCampoConsulta>();
		
		for (Field field: getClassImpl().getDeclaredFields()) {
			if(field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {
				String descricao = field.getAnnotation(IdentificaCampoPesquisa.class).descricaoCampo();
				String descricaoCampoPesquisa = field.getAnnotation(IdentificaCampoPesquisa.class).campoConsulta();
				int isPrincipal = field.getAnnotation(IdentificaCampoPesquisa.class).principal();
				
				ObjetoCampoConsulta objetoCampoConsulta = new ObjetoCampoConsulta();
				objetoCampoConsulta.setCampoBanco(descricaoCampoPesquisa);
				objetoCampoConsulta.setDescricao(descricao);
				objetoCampoConsulta.setPrincipal(isPrincipal);
				objetoCampoConsulta.setTipoClass(field.getType().getCanonicalName());
				listTemp.add(objetoCampoConsulta);
			}
		}
		orderReverse(listTemp);
		for (ObjetoCampoConsulta objetoCampoConsulta : listTemp) {
			listaCampoPesquisa.add(new SelectItem(objetoCampoConsulta));
		}
		return listaCampoPesquisa;
	}

	private void orderReverse(List<ObjetoCampoConsulta> listTemp) {
		Collections.sort(listTemp, new Comparator<ObjetoCampoConsulta>() {

			@Override
			public int compare(ObjetoCampoConsulta obj1, ObjetoCampoConsulta obj2) {
				return obj1.getPrincipal().compareTo(obj2.getPrincipal());
			}
			
		});
	}

	public String getSqlLazyQuery() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select entity from ").append(getQueryConsulta()).append(" order by entity.").append(objetoCampoConsultaSelecionado.getCampoBanco());
		return sql.toString();
	}

	private Object getQueryConsulta() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getClassImpl().getSimpleName()).append(" entity where ").append(" upper(cast(entity.").append(objetoCampoConsultaSelecionado.getCampoBanco()).append(" as text))");
		if(condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.IGUAL_A.name())) {
			sql.append(" = retira_acentos(upper('").append(valorPesquisa).append("'))");
		} else if(condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.CONTEM.name())) {
			sql.append(" like retira_acentos(upper('%").append(valorPesquisa).append("%'))");
		} else if(condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.TERMINA_COM.name())) {
			sql.append(" like retira_acentos(upper('%").append(valorPesquisa).append("'))");
		} else if(condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.INICIA.name())) {
			sql.append(" like retira_acentos(upper('").append(valorPesquisa).append("%'))");
		}
		sql.append(" ").append(condicaoAndParaPesquisa());
		return sql.toString();
	}

	public int totalRegistroConsulta() throws Exception {
		Query query = getController().obterQuery(" select count(entity) from " + getQueryConsulta());
		Number result = (Number) query.uniqueResult(); 
		return result.intValue();
	}
	
	
}
