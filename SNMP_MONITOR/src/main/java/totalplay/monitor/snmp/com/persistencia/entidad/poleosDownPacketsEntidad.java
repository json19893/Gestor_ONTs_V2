package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Document(collection = "tb_poleos_down_packets")
@Getter
@Setter
@ToString
public class poleosDownPacketsEntidad{
	@Id
	private String _id;
	private String oid;
	private String valor;
	private Integer id_olt;
	private Integer id_metrica;
	private String fecha_poleo;
	private Integer estatus;
	private String id_ejecucion;
	private Integer id_region;
	
	public Integer getId_region() {
		return id_region;
	}
	public void setId_region(Integer id_region) {
		this.id_region = id_region;
	}
	private Integer frame;
	private Integer slot;
	private Integer port;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Integer getId_olt() {
		return id_olt;
	}
	public void setId_olt(Integer id_olt) {
		this.id_olt = id_olt;
	}
	public Integer getId_metrica() {
		return id_metrica;
	}
	public void setId_metrica(Integer id_metrica) {
		this.id_metrica = id_metrica;
	}
	public String getFecha_poleo() {
		return fecha_poleo;
	}
	public void setFecha_poleo(String fecha_poleo) {
		this.fecha_poleo = fecha_poleo;
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
	
	

	
}

