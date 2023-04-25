package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_regisro_monitoreo_region")
@Data
@NoArgsConstructor

public class monitoreoRegionEntidad {
	@Id
	private String id_registro;
	private String descripcion;
	private Integer id_region;
	private String fecha_inicio;
	private String fecha_fin;
	private Integer id_estatus;

	public String getId_registro() {
		return id_registro;
	}

	public void setId_registro(String id_registro) {
		this.id_registro = id_registro;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getId_region() {
		return id_region;
	}

	public void setId_region(Integer id_region) {
		this.id_region = id_region;
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

	public Integer getId_estatus() {
		return id_estatus;
	}

	public void setId_estatus(Integer id_estatus) {
		this.id_estatus = id_estatus;
	}


}
