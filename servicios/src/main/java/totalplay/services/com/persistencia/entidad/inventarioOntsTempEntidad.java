package totalplay.services.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "tb_inventario_onts_pdm")
@Getter
@Setter
@ToString
public class inventarioOntsTempEntidad {
	
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
	private Integer frame;
	private Integer slot;
	private Integer port;
	private String uid;
	private String tipo;
	private String descripcionAlarma;
	private String tecnologia;
	private String lastDownTime;
	private Integer vip;
	
	
	
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
	public void setFecha_descubrimiento(String fecha_descubrimiento) {
		this.fecha_descubrimiento = fecha_descubrimiento;
	}
	public Integer getid_olt() {
		return id_olt;
	}
	public void setid_olt(Integer id_olt) {
		this.id_olt = id_olt;
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
	public Integer getFrame() {
		return frame;
	}
	public void setFrame(Integer frame) {
		this.frame = frame;
	}
	public Integer getSlot() {
		return slot;
	}
	public void setSlot(Integer slot) {
		this.slot = slot;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescripcionAlarma() {
		return descripcionAlarma;
	}
	public void setDescripcionAlarma(String descripcionAlarma) {
		this.descripcionAlarma = descripcionAlarma;
	}
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}
	public String getLastDownTime() {
		return lastDownTime;
	}
	public void setLastDownTime(String lastDownTime) {
		this.lastDownTime = lastDownTime;
	}
	public Integer getVip() {
		return vip;
	}
	public void setVip(Integer vip) {
		this.vip = vip;
	}
	
	
	

}
