package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "tb_metrica_run_estatus")
@Getter
@Setter
@ToString
public class PoleosRunEstatusEntity {
	@Id
	private String _id;
	private String oid;
	private String valor;
	private Integer id_olt;
	private Integer id_metrica;
	private String fecha_poleo;
	private Integer estatus;
	private String id_ejecucion;
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

	
}
