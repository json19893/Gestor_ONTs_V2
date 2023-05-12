package totalplay.services.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "tbl_procesada_configuracion")
@Getter
@Setter
@ToString
public class catOtsProcesadoEntidad {
	@Id
	private String id;
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String tecnologia;
	private Integer id_region;
	private String password;
	private String frase;
	private String protocolo;
	private String valor;
	

	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getId_olt() {
		return id_olt;
	}
	public void setId_olt(Integer id_olt) {
		this.id_olt = id_olt;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}
	public Integer getId_region() {
		return id_region;
	}
	public void setId_region(Integer id_region) {
		this.id_region = id_region;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFrase() {
		return frase;
	}
	public void setFrase(String frase) {
		this.frase = frase;
	}
	public String getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	
	



	



}
