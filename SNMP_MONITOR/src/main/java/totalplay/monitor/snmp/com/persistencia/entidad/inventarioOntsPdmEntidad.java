package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "tb_inventario_onts_pdm")
@Getter
@Setter
@ToString
public class inventarioOntsPdmEntidad {
	@Id
	private String _id;
	private String numero_serie;
	private String oid;
	private String fecha_descubrimiento;
	private Integer id_olt;
	private Integer estatus;
	private String id_ejecucion;
	private String alias;
	private Integer id_region;
	private Integer slot;
	private Integer frame;
	private Integer port;
	private String tipo;
	private String uid;
	private String descripcionAlarma;
	private Integer actualizacion;
	private String id_puerto;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
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
	public Integer getid_olt() {
		return id_olt;
	}
	public void setid_olt(Integer id_olt) {
		this.id_olt = id_olt;
	}
	public void setFecha_descubrimiento(String fecha_descubrimiento) {
		this.fecha_descubrimiento = fecha_descubrimiento;
	}
	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
	public String getId_ejecucion() {
		return id_ejecucion;
	}
	public void setId_ejecucion(String id_ejecucion) {
		this.id_ejecucion = id_ejecucion;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Integer getId_region() {
		return id_region;
	}
	public void setId_region(Integer id_region) {
		this.id_region = id_region;
	}
	public Integer getSlot() {
		return slot;
	}
	public void setSlot(Integer slot) {
		this.slot = slot;
	}
	public Integer getFrame() {
		return frame;
	}
	public void setFrame(Integer frame) {
		this.frame = frame;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getDescripcionAlarma() {
		return descripcionAlarma;
	}
	public void setDescripcionAlarma(String descripcionAlarma) {
		this.descripcionAlarma = descripcionAlarma;
	}
	public Integer getActualizacion() {
		return actualizacion;
	}
	public void setActualizacion(Integer actualizacion) {
		this.actualizacion = actualizacion;
	}
	public String getId_puerto() {
		return id_puerto;
	}
	public void setId_puerto(String id_puerto) {
		this.id_puerto = id_puerto;
	}
	
}
