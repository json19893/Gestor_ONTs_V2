package totalplay.monitor.snmp.com.negocio.dto;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;

@Getter
@Setter
public class dataRegionResponseDto {
	@Id
	private String	id;	
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String 	descripcion;
	private Integer 	id_region;
	private Integer total_onts;
	private String tecnologia;
	private Integer estatus;
	
	
	
	public String get_id() {
		return id;
	}
	public void set_id(String id) {
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
	public String getDescricion() {
		return descripcion;
	}
	public void setDescricion(String descricion) {
		this.descripcion = descricion;
	}
	public Integer getId_region() {
		return id_region;
	}
	public void setId_region(Integer id_region) {
		this.id_region = id_region;
	}
	public Integer getTotal_onts() {
		return total_onts;
	}
	public void setTotal_onts(Integer total_onts) {
		this.total_onts = total_onts;
	}
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}
	

	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof vwTotalOntsEntidad) {
			return ((vwTotalOntsEntidad) obj).getIp().equals(this.getIp());
		}
		return false;
	}
}
