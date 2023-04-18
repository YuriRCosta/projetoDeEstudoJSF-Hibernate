package br.com.project.geral.controller;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.model.classes.Estado;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

@Controller
public class EstadoController extends ImplementacaoCrud<Estado> implements InterfaceCrud<Estado> {

	private static final long serialVersionUID = 1L;

	public List<SelectItem> getListEstado() throws Exception {
		List<SelectItem> list = new ArrayList<SelectItem>();
		
		List<Estado> estados = super.findListByQueryDinamica(" from Estado");
		
		for (Estado estado : estados) {
			list.add(new SelectItem(estado, estado.getEst_nome()));
		}
		return list;
	}
	
}
