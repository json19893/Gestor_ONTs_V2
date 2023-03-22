package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

public class monitorPoleoRegionDto {
	
	private String _id;
	private String id_poleo;
	private Integer id_region;
	private String nombre;
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	private List<monitorPoleoRegionHiloDto> poleo_region_hilo;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getId_poleo() {
		return id_poleo;
	}
	public void setId_poleo(String id_poleo) {
		this.id_poleo = id_poleo;
	}
	public Integer getId_region() {
		return id_region;
	}
	public void setId_region(Integer id_region) {
		this.id_region = id_region;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	public List<monitorPoleoRegionHiloDto> getPoleo_region_hilo() {
		return poleo_region_hilo;
	}
	public void setPoleo_region_hilo(List<monitorPoleoRegionHiloDto> poleo_region_hilo) {
		this.poleo_region_hilo = poleo_region_hilo;
	}
	
	
	
	
}
