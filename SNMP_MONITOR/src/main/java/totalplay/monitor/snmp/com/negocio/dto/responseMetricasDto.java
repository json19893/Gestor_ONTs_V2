package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class responseMetricasDto {
	private String lastUpTime;
	private String upBytes;
	private String DownBytes;
	private String timeOut;
	private String upPackets;
	private String downPackets;
	private String dropUpPackets;
	private String dropDownPackets;
	private String cpu;
	private String memoria;
	private String profName;
	
	public String getLastUpTime() {
		return lastUpTime;
	}
	public void setLastUpTime(String lastUpTime) {
		this.lastUpTime = lastUpTime;
	}
	public String getUpBytes() {
		return upBytes;
	}
	public void setUpBytes(String upBytes) {
		this.upBytes = upBytes;
	}
	public String getDownBytes() {
		return DownBytes;
	}
	public void setDownBytes(String downBytes) {
		DownBytes = downBytes;
	}
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	public String getUpPackets() {
		return upPackets;
	}
	public void setUpPackets(String upPackets) {
		this.upPackets = upPackets;
	}
	public String getDownPackets() {
		return downPackets;
	}
	public void setDownPackets(String downPackets) {
		this.downPackets = downPackets;
	}
	public String getDropUpPackets() {
		return dropUpPackets;
	}
	public void setDropUpPackets(String dropUpPackets) {
		this.dropUpPackets = dropUpPackets;
	}
	public String getDropDownPackets() {
		return dropDownPackets;
	}
	public void setDropDownPackets(String dropDownPackets) {
		this.dropDownPackets = dropDownPackets;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getMemoria() {
		return memoria;
	}
	public void setMemoria(String memoria) {
		this.memoria = memoria;
	}
	public String getProfName() {
		return profName;
	}
	public void setProfName(String profName) {
		this.profName = profName;
	}
}
