package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Getter
@Setter
@ToString
public class responseFindOntDto {
	
	private boolean success;
	private Integer page;
	private Integer idRegion;
	private String numeroSerie;
	private String message;
	private Integer idOlt;
	private List<inventarioOntsEntidad> listOnts;
	private totalesOltsDto totales;
	private String nombre;
	private String ip;
	
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getIdRegion() {
		return idRegion;
	}
	public void setIdRegion(Integer idRegion) {
		this.idRegion = idRegion;
	}
	public String getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getIdOlt() {
		return idOlt;
	}
	public void setIdOlt(Integer idOlt) {
		this.idOlt = idOlt;
	}
	public List<inventarioOntsEntidad> getListOnts() {
		return listOnts;
	}
	public void setListOnts(List<inventarioOntsEntidad> listOnts) {
		this.listOnts = listOnts;
	}
	public totalesOltsDto getTotales() {
		return totales;
	}
	public void setTotales(totalesOltsDto totales) {
		this.totales = totales;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}	
	
}
