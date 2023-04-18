package br.com.project.util.all;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

public abstract class Messages extends FacesContext implements Serializable{

	private static final long serialVersionUID = 1L;

	public Messages() {
	}
	
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	private static boolean facesContextValidation() {
		return getFacesContext() != null;
	}
	
	public static void msgSeverityWarn(String msg) {
		if(facesContextValidation()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
		}
	}
	
	public static void msgSeverityFatal(String msg) {
		if(facesContextValidation()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg));
		}
	}
	
	public static void msgSeverityError(String msg) {
		if(facesContextValidation()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public static void msgSeverityInfo(String msg) {
		if(facesContextValidation()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
		}
	}
	
	public static void msg(String msg) {
		if(facesContextValidation()) {
			getFacesContext().addMessage("msg", new FacesMessage(msg));
		}
	}
	
	public static void erroNaOperacao() {
		if(facesContextValidation()) {
			msgSeverityFatal(Constante.ERRO_NA_OPERACAO);
		}
	}
	
	public static void responseOperator (StatusPersistencia statusPersistencia) {
		if(statusPersistencia != null && statusPersistencia.equals(statusPersistencia.SUCESSO)) {
			sucesso();
		} else if(statusPersistencia != null && statusPersistencia.equals(statusPersistencia.OBJETO_REFERENCIADO)){
			msgSeverityFatal(statusPersistencia.OBJETO_REFERENCIADO.toString());
		} else {
			erroNaOperacao();
		}
	}
	
	public static void sucesso() {
		msgSeverityInfo(Constante.OPERACAO_REALIZADA_COM_SUCESSO);
	}
}
