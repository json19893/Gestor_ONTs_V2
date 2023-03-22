package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

public class monitorPoleoRegionHiloDto {
	
	private String _id;
	private String id_region;
	private String hilo;
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	private List<monitorPoleoOltDto> poleo_olt;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getId_region() {
		return id_region;
	}
	public void setId_region(String id_region) {
		this.id_region = id_region;
	}
	public String getHilo() {
		return hilo;
	}
	public void setHilo(String hilo) {
		this.hilo = hilo;
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
	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
	public List<monitorPoleoOltDto> getPoleo_olt() {
		return poleo_olt;
	}
	public void setPoleo_olt(List<monitorPoleoOltDto> poleo_olt) {
		this.poleo_olt = poleo_olt;
	}
	
}
