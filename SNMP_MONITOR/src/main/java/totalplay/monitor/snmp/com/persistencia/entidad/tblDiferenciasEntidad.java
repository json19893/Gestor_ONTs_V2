package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "tb_diferencias")
@Getter
@Setter
@ToString
public class tblDiferenciasEntidad {
	
	private String _id;
	private Integer id_ont;
	private String numero_serie;
	private String oid;
	private String fecha_descubrimiento;
	private Integer id_olts;
	private String id_ejecucion;
	private Integer estatus;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Integer getId_ont() {
		return id_ont;
	}

	public void setId_ont(Integer id_ont) {
		this.id_ont = id_ont;
	}

	public String getNumero_serie() {
		return numero_serie;
	}

	public void setNumero_serie(String numero_serie) {
		this.numero_serie = numero_serie;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getFecha_descubrimiento() {
		return fecha_descubrimiento;
	}

	public void setFecha_descubrimiento(String fecha_descubrimiento) {
		this.fecha_descubrimiento = fecha_descubrimiento;
	}

	public Integer getId_olts() {
		return id_olts;
	}

	public void setId_olts(Integer id_olts) {
		this.id_olts = id_olts;
	}

	public String getId_ejecucion() {
		return id_ejecucion;
	}

	public void setId_ejecucion(String id_ejecucion) {
		this.id_ejecucion = id_ejecucion;
	}

	public Integer getEstatus() {
		return estatus;
	}

	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}

}
