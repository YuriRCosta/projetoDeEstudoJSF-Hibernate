package br.com.project.model.classes;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.envers.Audited;
import org.primefaces.json.JSONObject;

import br.com.project.annotation.IdentificaCampoPesquisa;

@Entity
@Audited
public class Entidade implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ent_codigo;

	private boolean ent_inativo = false;
	
	private String ent_login = null;
	private String ent_senha;
	private String tipoEntidade = "";
	
	@Column(unique = true)
	private String cpf = "";
	
	@CollectionOfElements
	@ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
	@JoinTable(name = "entidadeacesso", uniqueConstraints = { @UniqueConstraint(name = "unique_acesso_entidade_key", columnNames = {
			"ent_codigo", "esa_codigo" }) }, joinColumns = { @JoinColumn(name = "ent_codigo") })
	@Column(name = "esa_codigo", length = 20)
	private Set<String> acessos = new HashSet<String>();
	
	@IdentificaCampoPesquisa(campoConsulta = "ent_nomefantasia", descricaoCampo = "Nome", principal = 1)
	private String ent_nomefantasia;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ent_ultimoacesso;	
	
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<String> getAcessos() {
		return acessos;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public void setAcessos(Set<String> acessos) {
		this.acessos = acessos;
	}
	public String getTipoEntidade() {
		return tipoEntidade;
	}
	public void setTipoEntidade(String tipoEntidade) {
		this.tipoEntidade = tipoEntidade;
	}
	public String getEnt_nomefantasia() {
		return ent_nomefantasia;
	}
	public void setEnt_nomefantasia(String ent_nomefantasia) {
		this.ent_nomefantasia = ent_nomefantasia;
	}
	public Date getEnt_ultimoacesso() {
		return ent_ultimoacesso;
	}
	public void setEnt_ultimoacesso(Date ent_ultimoacesso) {
		this.ent_ultimoacesso = ent_ultimoacesso;
	}
	public boolean isEnt_inativo() {
		return ent_inativo;
	}
	public void setEnt_inativo(boolean ent_inativo) {
		this.ent_inativo = ent_inativo;
	}
	public String getEnt_login() {
		return ent_login;
	}
	public void setEnt_login(String ent_login) {
		this.ent_login = ent_login;
	}
	public String getEnt_senha() {
		return ent_senha;
	}
	public void setEnt_senha(String ent_senha) {
		this.ent_senha = ent_senha;
	}
	public Long getEnt_codigo() {
		return ent_codigo;
	}
	public void setEnt_codigo(Long ent_codigo) {
		this.ent_codigo = ent_codigo;
	}
	@Override
	public int hashCode() {
		return Objects.hash(ent_codigo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entidade other = (Entidade) obj;
		return Objects.equals(ent_codigo, other.ent_codigo);
	}
	
	public JSONObject getJson() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("ent_codigo", ent_codigo);
		map.put("ent_login", ent_login);
		map.put("ent_nomefantasia", ent_nomefantasia);
		return new JSONObject(map);
	}
	
	
}
