package totalplay.monitor.snmp.com.persistencia.entidad;



import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import totalplay.monitor.snmp.com.negocio.dto.diferenciasDto;

@Document(collection = "tb_historico_diferencias")
@Getter
@Setter
public class tbHistoricoDiferenciasEntidad {

	@Id
	private String _id;
	private String numero_serie;
	private Integer id_olt;
	private String estatus;
	private String oid;
	private String fecha_descubrimiento;
	private String alias;
	private Integer id_region;
	private String slot;
	private String frame;
	private String port;
	private String tipo;
	private String descripcionAlarma;
	private String lastDownTime;
	private List<diferenciasDto> diferencias;
	
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
	public Integer getId_olt() {
		return id_olt;
	}
	public void setId_olt(Integer id_olt) {
		this.id_olt = id_olt;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
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


	public List<diferenciasDto> getDiferencias() {
		return diferencias;
	}
	public void setDiferencias(List<diferenciasDto> diferencias) {
		this.diferencias = diferencias;
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
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
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
	public String getLastDownTime() {
		return lastDownTime;
	}
	public void setLastDownTime(String lastDownTime) {
		this.lastDownTime = lastDownTime;
	}		
	
	

}
