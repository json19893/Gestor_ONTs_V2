package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "cat_configuracion")

public class catConfiguracionEntidad {
	@Id
	private String _id;
	private Integer id_configuracion;
	private String usuario;
	private String password;
	private String frase;
	private String nivel;
	private String version;
	private String prot_privado;
	private String prot_encriptado;
	private Integer estatus;
	private String fecha;
	private Integer descubrimientos;
	private Integer ejecucion;
	private String inicioBloqueo;
	private String finBloqueo;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public Integer getId_configuracion() {
		return id_configuracion;
	}
	public void setId_configuracion(Integer id_configuracion) {
		this.id_configuracion = id_configuracion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getProt_privado() {
		return prot_privado;
	}
	public void setProt_privado(String prot_privado) {
		this.prot_privado = prot_privado;
	}
	public String getProt_encriptado() {
		return prot_encriptado;
	}
	public void setProt_encriptado(String prot_encriptado) {
		this.prot_encriptado = prot_encriptado;
	}
	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Integer getDescubrimientos() {
		return descubrimientos;
	}
	public void setDescubrimientos(Integer descubrimientos) {
		this.descubrimientos = descubrimientos;
	}
	public Integer getEjecucion() {
		return ejecucion;
	}
	public void setEjecucion(Integer ejecucion) {
		this.ejecucion = ejecucion;
	}
	public String getInicioBloqueo() {
		return inicioBloqueo;
	}
	public void setInicioBloqueo(String inicioBloqueo) {
		this.inicioBloqueo = inicioBloqueo;
	}
	public String getFinBloqueo() {
		return finBloqueo;
	}
	public void setFinBloqueo(String finBloqueo) {
		this.finBloqueo = finBloqueo;
	}
  
  
	

}
