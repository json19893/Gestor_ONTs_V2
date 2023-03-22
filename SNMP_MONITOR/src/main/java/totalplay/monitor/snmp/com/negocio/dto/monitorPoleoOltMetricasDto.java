package totalplay.monitor.snmp.com.negocio.dto;

public class monitorPoleoOltMetricasDto {
	
	private String _id;
	private String id_poleo_olt;
	private String hilo;
	private Integer id_metrica;
	private String nombre_metrica; 
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getId_poleo_olt() {
		return id_poleo_olt;
	}
	public void setId_poleo_olt(String id_poleo_olt) {
		this.id_poleo_olt = id_poleo_olt;
	}
	public String getHilo() {
		return hilo;
	}
	public void setHilo(String hilo) {
		this.hilo = hilo;
	}
	public Integer getId_metrica() {
		return id_metrica;
	}
	public void setId_metrica(Integer id_metrica) {
		this.id_metrica = id_metrica;
	}
	public String getNombre_metrica() {
		return nombre_metrica;
	}
	public void setNombre_metrica(String nombre_metrica) {
		this.nombre_metrica = nombre_metrica;
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
}
