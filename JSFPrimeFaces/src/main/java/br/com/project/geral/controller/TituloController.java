package br.com.project.geral.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.model.classes.Titulo;

@Controller
public class TituloController extends ImplementacaoCrud<Titulo> implements InterfaceCrud<Titulo> {

	private static final long serialVersionUID = 1L;
	
	@RequestMapping("**/gerarGraficoInicial")
	public @ResponseBody String gerarGraficoInicial (@RequestParam(value = "dias") int dias) {
		List<Map<String, Object>> titulosUltimosDias = getTituloUltimosDias(dias);
		int totalLinhas = titulosUltimosDias.size() + 1;
		boolean semDados = false;
		if(totalLinhas <= 1) {
			semDados = true;
		}
		
		String[] dados = new String[totalLinhas];
		int cont = 0;
		
		if(semDados) {
			dados[cont ++] = "[\"" + "Sem Dados" + "\"," + "\"" + "0" + "\"," + "\"" + "0" + "\"]";
		} else {
			dados[cont] = "[\"" + "Tipo" + "\"," + "\"" + "Quantidade" + "\"," + "\"" + "Media" + "\"]";
			cont++;
			for(Map<String, Object> objeto : titulosUltimosDias) {
				dados[cont] = "[\"" + objeto.get("situacao") + "\"," + "\"" + objeto.get("quantidade") + "\"," + "\"R$ " + objeto.get("media_valor") + "\"]";
				cont++;
			}
		
		}
		return Arrays.toString(dados);
	}

	private List<Map<String, Object>> getTituloUltimosDias(int dias) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) as quantidade, tit_origem as situacao, trunc(avg(COALESCE(tit_valor, 0.00)), 2) as media_valor from titulo ");
		sql.append("where cast(tit_datahora as date) >= (CURRENT_DATE - "+dias+") and cast (tit_datahora as date) <= CURRENT_DATE ");
		sql.append("group by tit_origem  ");
		sql.append("union ");
		sql.append("select count(1) as quantidade, case when tit_baixado then 'BAIXADO' else 'EM ABERTO' end as situacao, ");
		sql.append("trunc(avg(COALESCE(tit_valor, 0.00)), 2) as media_valor from titulo ");
		sql.append("where cast(tit_datahora as date) >= (CURRENT_DATE - "+dias+") and cast (tit_datahora as date) <= CURRENT_DATE ");
		sql.append("group by tit_baixado ");
		sql.append("union ");
		sql.append("select count(1) as quantidade, tit_tipo as situacao, trunc(avg(COALESCE(tit_valor, 0.00)), 2) as media_valor ");
		sql.append("from titulo ");
		sql.append("where cast(tit_datahora as date) >= (CURRENT_DATE - "+dias+") and cast (tit_datahora as date) <= CURRENT_DATE ");
		sql.append("group by tit_tipo order by quantidade, media_valor ");

		return getSimpleJdbcTemplate().queryForList(sql.toString());
	}
}
