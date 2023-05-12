package totalplay.services.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "olts_historico")
@Getter
@Setter
@ToString
public class oltsHistorico {
	
	private Integer id_olt;
	private String nombre;
	private String ipAnterior;
	private String ipNueva;
	private String fechaCambio;
	
	public Integer getId_olt() {
		return id_olt;
	}
	public void setId_olt(Integer id_olt) {
		this.id_olt = id_olt;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIpAnterior() {
		return ipAnterior;
	}
	public void setIpAnterior(String ipAnterior) {
		this.ipAnterior = ipAnterior;
	}
	public String getIpNueva() {
		return ipNueva;
	}
	public void setIpNueva(String ipNueva) {
		this.ipNueva = ipNueva;
	}
	public String getFechaCambio() {
		return fechaCambio;
	}
	public void setFechaCambio(String fechaCambio) {
		this.fechaCambio = fechaCambio;
	}
	

}
