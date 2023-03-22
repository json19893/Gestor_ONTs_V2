package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

public class monitorPoleoDto {
	private String _id;
	private String fecha_inicio;
	private String fecha_fin;
	private String descripcion;
	private Integer estatus;
	private List<monitorPoleoRegionDto> poleo_region;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	public String getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
	public List<monitorPoleoRegionDto> getPoleo_region() {
		return poleo_region;
	}
	public void setPoleo_region(List<monitorPoleoRegionDto> poleo_region) {
		this.poleo_region = poleo_region;
	}


	
}
