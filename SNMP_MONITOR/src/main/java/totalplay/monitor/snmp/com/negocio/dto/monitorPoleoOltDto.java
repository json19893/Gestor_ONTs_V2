package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;


public class monitorPoleoOltDto {

	private String _id;
	private String id_region_hilo;
	private Integer id_olt;
	private String nombre_olt;
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	private List<monitorPoleoOltMetricasDto> metricas;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getId_region_hilo() {
		return id_region_hilo;
	}
	public void setId_region_hilo(String id_region_hilo) {
		this.id_region_hilo = id_region_hilo;
	}
	public Integer getId_olt() {
		return id_olt;
	}
	public void setId_olt(Integer id_olt) {
		this.id_olt = id_olt;
	}
	public String getNombre_olt() {
		return nombre_olt;
	}
	public void setNombre_olt(String nombre_olt) {
		this.nombre_olt = nombre_olt;
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
	public List<monitorPoleoOltMetricasDto> getMetricas() {
		return metricas;
	}
	public void setMetricas(List<monitorPoleoOltMetricasDto> metricas) {
		this.metricas = metricas;
	}	
	
		
}
