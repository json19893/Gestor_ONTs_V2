package totalplay.services.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class requestEstatusDto {

	private Integer frame;
	private Integer slot;
	private Integer port;
	private String uid;
	private String ip;
	private String estatus;
	private String descripcionAlarma;
	

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getDescripcionAlarma() {
		return descripcionAlarma;
	}

	public void setDescripcionAlarma(String descripcionAlarma) {
		this.descripcionAlarma = descripcionAlarma;
	}

}
