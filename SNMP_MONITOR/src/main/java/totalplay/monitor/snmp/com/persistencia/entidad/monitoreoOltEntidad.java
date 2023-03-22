package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "tb_regisro_monitoreo_olt")
@Getter
@Setter
@ToString
public class monitoreoOltEntidad {
	@Id
	private String id;
	private String descripcion;
	private String id_region;
	private String fecha_inicio;
	private String fecha_fin;
	private Integer id_estatus;
	private Integer id_olt;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public String getId_region() {
		return id_region;
	}
	public void setId_region(String id_region) {
		this.id_region = id_region;
	}
	public Integer getId_estatus() {
		return id_estatus;
	}
	public void setId_estatus(Integer id_estatus) {
		this.id_estatus = id_estatus;
	}
	public Integer getId_olt() {
		return id_olt;
	}
	public void setId_olt(Integer id_olt) {
		this.id_olt = id_olt;
	}
}
