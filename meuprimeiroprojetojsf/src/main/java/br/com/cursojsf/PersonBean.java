package br.com.cursojsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DAOGeneric;
import br.com.entity.Cidades;
import br.com.entity.Estados;
import br.com.entity.Person;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDAOPerson;
import net.bootsfaces.component.selectOneMenu.SelectOneMenu;

@Named(value="personBean")
@javax.faces.view.ViewScoped
public class PersonBean implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
	private JPAUtil jpaUtil;
	
	private Person person = new Person();
	
	@Inject
	private DAOGeneric<Person> daoGeneric;
	private List<Person> people = new ArrayList<Person>();

	@Inject
	private IDAOPerson idaoPerson;

	private List<SelectItem> estados;
	private List<SelectItem> cidades;
	
	private Part arquivoFoto;
	
	public Part getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	public List<SelectItem> getEstados() {
		estados = idaoPerson.listaEstados();
		return estados;
	}
	
	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public void loadCities(AjaxBehaviorEvent event) {

	Estados estado = (Estados) ((SelectOneMenu) event.getSource()).getValue();
	
		if (estado != null) {
			person.setEstados(estado);

			List<Cidades> cidades = jpaUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();
			
			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}
			
			setCidades(selectItemsCidade);	
		}
	}


	public List<SelectItem> getCidades() {
		return cidades;
	}

	public String save() throws IOException {
		
		if(arquivoFoto != null && arquivoFoto.getInputStream() != null) {
			
			byte[] imagemByte = getByte(arquivoFoto.getInputStream());
			
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
			
			if(bufferedImage != null) {
				
				person.setFotoIconBase64Original(imagemByte);
				int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
				
				int largura = 200;
				int altura = 200;
				
				BufferedImage resizedImage = new BufferedImage(largura, altura, type);
				Graphics2D  g = resizedImage.createGraphics();
				g.drawImage(bufferedImage, 0, 0, largura, altura, null);
				g.dispose();
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String extensao = arquivoFoto.getContentType().split("\\/")[1];
				ImageIO.write(resizedImage, extensao, baos);
				String miniImagem = "data:" + arquivoFoto.getContentType() + ";base64," + DatatypeConverter.printBase64Binary(baos.toByteArray()); 
				
				person.setFotoIconBase64(miniImagem);
				person.setExtensao(extensao);
				
			}
			
		}
		
		person = daoGeneric.merge(person);
		updatePeople();
		showMsg("Salvo com sucesso!");
		return "";
	}

	private void showMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public String newPerson() {

		person = new Person();

		return "";
	}

	public String clean() {

		person = new Person();

		return "";
	}

	public void searchCep(AjaxBehaviorEvent event) {

		try {

			URL url = new URL("https://viacep.com.br/ws/" + person.getCep() + "/json/");

			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {
				jsonCep.append(cep);
			}

			Person gson = new Gson().fromJson(jsonCep.toString(), Person.class);

			person.setCep(gson.getCep());
			person.setLogradouro(gson.getLogradouro());
			person.setLocalidade(gson.getLocalidade());
			person.setUf(gson.getUf());
			person.setBairro(gson.getBairro());

			System.out.println(jsonCep);
		} catch (Exception e) {
			e.printStackTrace();
			showMsg("Erro ao consultar CEP");
		}

	}

	public String remove() {

		daoGeneric.deleteId(person);
		person = new Person();
		updatePeople();
		showMsg("Excluido com sucesso!");
		return "";

	}

	@PostConstruct
	public void updatePeople() {

		people = daoGeneric.getListEntityLimit10(Person.class);

	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Person> getPeople() {
		return people;
	}

	public String logIn() {

		Person personUser = idaoPerson.SearchUser(person.getLogin(), person.getPassword());

		if (personUser != null) {

			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("userLogged", personUser);

			return "primeirapagina.jsf";

		} else {
			
			FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Usuario nao encontrado!"));
			
		}

		return "index.jsf";
	}

	public String logout() {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("userLogged");

		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
				.getRequest();

		httpServletRequest.getSession().invalidate();

		return "index.jsf";
	}

	public boolean allowAccess(String access) {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Person personUser = (Person) externalContext.getSessionMap().get("userLogged");

		return personUser.getUserPerfil().equals(access);

	}

	public String edit() {
		if(person.getCidades() != null) {
			Estados estado = person.getCidades().getEstados();
			person.setEstados(estado);
			
			List<Cidades> cidades = jpaUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();
			
			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}
			
			setCidades(selectItemsCidade);	
		}
		return "";
	}
	
	private byte[] getByte (InputStream is) throws IOException {
		
		int len;
		int size = 1024;
		byte[] buf = null;
		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			
			while ((len = is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, len);
			}
			
			buf = bos.toByteArray();

		}
		return buf;
	}
	
	public void download() throws IOException {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		String fileDownloadId = params.get("fileDownloadId");
		Person person = daoGeneric.consult(Person.class, fileDownloadId);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download." + person.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(person.getFotoIconBase64Original().length);
		response.getOutputStream().write(person.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
		
	}
}