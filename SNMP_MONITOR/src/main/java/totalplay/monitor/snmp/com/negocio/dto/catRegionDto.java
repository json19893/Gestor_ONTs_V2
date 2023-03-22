package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class catRegionDto {
	private String _id;
	private Integer id_region;
	private String nombre_region;
	private Integer estatus;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Integer getId_region() {
		return id_region;
	}

	public void setId_region(Integer id_region) {
		this.id_region = id_region;
	}

	public String getNombre_region() {
		return nombre_region;
	}

	public void setNombre_region(String nombre_region) {
		this.nombre_region = nombre_region;
	}

	public Integer getEstatus() {
		return estatus;
	}

	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}

}
