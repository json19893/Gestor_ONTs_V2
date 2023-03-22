package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Getter
@Setter
@ToString
public class datosOntsDto {
	private String _id;
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String descricion;
	private String tecnologia;
	private String id_region;
	private String id_configuracion;
	private Integer estatus;
	public List<inventarioOntsEntidad> onts;
	public Integer TotalHist;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
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

	public String getId_region() {
		return id_region;
	}

	public void setId_region(String id_region) {
		this.id_region = id_region;
	}

	public String getId_configuracion() {
		return id_configuracion;
	}

	public void setId_configuracion(String id_configuracion) {
		this.id_configuracion = id_configuracion;
	}

	public Integer getEstatus() {
		return estatus;
	}

	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}

	public List<inventarioOntsEntidad> getOnts() {
		return onts;
	}

	public void setOnts(List<inventarioOntsEntidad> onts) {
		this.onts = onts;
	}

	public Integer getTotalHist() {
		return TotalHist;
	}

	public void setTotalHist(Integer totalHist) {
		TotalHist = totalHist;
	}
	

}
