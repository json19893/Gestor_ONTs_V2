package totalplay.services.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "cat_olts")
@Getter
@Setter
@ToString
public class catOltsEntidad {
	@Id
	private String id;
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String descricion;
	private String tecnologia;
	private Integer id_region;
	private Integer id_configuracion;
	private Integer estatus;

	
	public String get_id() {
		return id;
	}
	public void set_id(String _id) {
		this.id = _id;
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
	public String getDescricion() {
		return descricion;
	}
	public void setDescricion(String descricion) {
		this.descricion = descricion;
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
	public Integer getId_configuracion() {
		return id_configuracion;
	}
	public void setId_configuracion(Integer id_configuracion) {
		this.id_configuracion = id_configuracion;
	}
	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}


}
