var arrayIdsElementsPage = new Array;
var idundefined = 'idundefined';
var classTypeString = 'java.lang.String';
var classTypeLong = 'java.lang.Long';
var classTypeDate = 'java.util.Date';
var classTypeBoolean = 'java.lang.Boolean';
var classTypeBigDecimal = 'java.math.BigDecimal';

function carregarIdElementosPagina() {
	 arrayIdsElementsPage = new Array;
	 for (form = 0 ; form <= document.forms.length; form++){
		 var formAtual = document.forms[form];
		 if (formAtual != undefined) {
			 for (i = 0; i< document.forms[form].elements.length; i++){
				 if(document.forms[form].elements[i].id != '') {
					 arrayIdsElementsPage[i] = document.forms[form].elements[i].id;
				 }
			 }	
		 }
	 }
}

function pesquisaUserDestinoPerderFocoDialog(codUser) {
	if(codUser != ""){
		$("#usr_destinoMsgDialog").val('');
		$.get("buscarUsuarioDestinoMsg?codEntidade=" + codUser , function(resposta){
			if(resposta.trim() != '') {
				var entidade = JSON.parse(resposta);
				$("#usr_destinoMsgDialog").val(entidade.ent_codigo);
				$("#loginDestinoMsgDialog").val(entidade.ent_login);
			}
		});
	}
}

function getValorElementPorIdJQuery(id) {
	var id = getValorElementPorId(id);
	if (id.trim() != idundefined) {
		 return PrimeFaces.escapeClientId(id);
	}
	
	 return idundefined;
}

function permitNumber(e) {
	var unicode = e.charCode ? e.charCode : e.keyCode;
	if (unicode != 8 && unicode != 9) {
		if (unicode < 48 || unicode > 57) {
			return false;
		}
	}
}

function getValorElementPorId(id) {
	 carregarIdElementosPagina();
	 for (i = 0; i< arrayIdsElementsPage.length; i++){
		 var valor =  ""+arrayIdsElementsPage[i];
		 if (valor.indexOf(id) > -1) {
			return valor;
	}
  }	
	 return idundefined;
}

function localeData_pt_br() {
	PrimeFaces.locales['pt'] = {
		closeText : 'Fechar',
		prevText : 'Anterior',
		nextText : 'Pr�ximo',
		currentText : 'Come�o',
		monthNames : [ 'Janeiro', 'Fevereiro', 'Marcio', 'Abril', 'Maio',
				'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro',
				'Dezembro' ],
		monthNamesShort : [ 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul',
				'Ago', 'Set', 'Out', 'Nov', 'Dez' ],
		dayNames : [ 'Domingo', 'Segunda', 'Ter�a', 'Quarta', 'Quinta',
				'Sexta', 'S�bado' ],
		dayNamesShort : [ 'Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'S�b' ],
		dayNamesMin : [ 'D', 'S', 'T', 'Q', 'Q', 'S', 'S' ],
		weekHeader : 'Semana',
		firstDay : 0,
		isRTL : false,
		showMonthAfterYear : false,
		yearSuffix : '',
		timeOnlyTitle : 'S�o Horas',
		timeText : 'Tempo',
		hourText : 'Hora',
		minuteText : 'Minuto',
		secondText : 'Segundo',
		ampm : false,
		month : 'M�s',
		week : 'Semana',
		day : 'Dia',
		allDayText : 'Todo o Dia'
	};

}

function invalidateSession(context, pagina) {
	document.location = (context + pagina + ".jsf");
}

function validarSenhaLogin() {

	j_username = document.getElementById("j_username").value;
	j_password = document.getElementById("j_password").value;
	
	if(j_username === null || j_username.trim() === '') {
		alert("Informe o login.");
		$('#j_username').focus();
		return false;
	} 
	
	if(j_password === null || j_password.trim() === '') {
		alert("Informe a senha.");
		$('#j_password').focus();
		return false;
	}

}

function addMascaraPesquisa(elemento) {
	var id = getValorElementPorIdJQuery('valorPesquisa');
	var vals = elemento.split("*");
	var campoBanco = vals[0];
	var typeCampo = vals[1];
	
	$(id).unmask();
	$(id).unbind("keypress"); 
	$(id).unbind("keyup");
	$(id).unbind("focus");
	$(id).val('');
	if (id != idundefined) {
		jQuery(function($) {
			if (typeCampo === classTypeLong) {
				$(id).keypress(permitNumber);
			}
			else if (typeCampo === classTypeBigDecimal) {	
				$(id).maskMoney({precision:4, decimal:",", thousands:"."}); 
			}
			else if (typeCampo === classTypeDate) {
				$(id).mask('99/99/9999');
			}
			else {
				$(id).unmask();
				$(id).unbind("keypress");
				$(id).unbind("keyup");
				$(id).unbind("focus");
				$(id).val('');
			}
			addFocoAoCampo("valorPesquisa");
		});
	}
}

function validarCampoPesquisa(valor) {
	if ( valor != undefined  &&  valor.value != undefined ) {
		if (valor.value.trim() === '') {
			valor.value = '';
		}else {
			valor.value = valor.value.trim();
		}
	}
}

function logout(contextPath) {
	document.location =	 contextPath + '/j_spring_security_logout';
	var post = 'invalidar_session';
	$.ajax(
		{ 
		  type: "POST", 
		  url: post
		});
}

function addFocoAoCampo(campo) {
	var id = getValorElementPorId(campo);
	if (id != undefined) {
		document.getElementById(id).focus();
	}
}
