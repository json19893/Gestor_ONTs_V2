package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEmpresarialesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsVipsEntidad;

@Getter
@Setter
@ToString
public class responseRegionDto {
	public List<vwTotalOntsEntidad> totalesRegion;
	public List<vwTotalOntsEmpresarialesEntidad> totalesRegionEmp;
	public List<vwTotalOntsVipsEntidad> totalesRegionVips;
	public Integer totalHuawei;
	public Integer totalZte;
	public Integer totalFh;
	public Integer totalArribaZte;
	public Integer totalArribaHuawei;
	public Integer totalArribaFh;
	public Integer totalAbajoHuawei;
	public Integer totalAbajoZte;
	public Integer totalAbajoFh;
	public Integer totalHuaweiEmp;
	public Integer totalZteEmp;
	public Integer totalFhEmp;
	public Integer totalArribaZteEmp;
	public Integer totalArribaHuaweiEmp;
	public Integer totalArribaFhEmp;
	public Integer totalAbajoHuaweiEmp;
	public Integer totalAbajoZteEmp;
	public Integer totalAbajoFhEmp;
	
	public List<vwTotalOntsEntidad> getTotalesRegion() {
		return totalesRegion;
	}
	public void setTotalesRegion(List<vwTotalOntsEntidad> totalesRegion) {
		this.totalesRegion = totalesRegion;
	}
	public Integer getTotalHuawei() {
		return totalHuawei;
	}
	public void setTotalHuawei(Integer totalHuawei) {
		this.totalHuawei = totalHuawei;
	}
	public Integer getTotalZte() {
		return totalZte;
	}
	public void setTotalZte(Integer totalZte) {
		this.totalZte = totalZte;
	}
	public Integer getTotalFh() {
		return totalFh;
	}
	public void setTotalFh(Integer totalFh) {
		this.totalFh = totalFh;
	}
	public Integer getTotalArribaZte() {
		return totalArribaZte;
	}
	public void setTotalArribaZte(Integer totalArribaZte) {
		this.totalArribaZte = totalArribaZte;
	}
	public Integer getTotalArribaHuawei() {
		return totalArribaHuawei;
	}
	public void setTotalArribaHuawei(Integer totalArribaHuawei) {
		this.totalArribaHuawei = totalArribaHuawei;
	}
	public Integer getTotalArribaFh() {
		return totalArribaFh;
	}
	public void setTotalArribaFh(Integer totalArribaFh) {
		this.totalArribaFh = totalArribaFh;
	}
	public Integer getTotalAbajoHuawei() {
		return totalAbajoHuawei;
	}
	public void setTotalAbajoHuawei(Integer totalAbajoHuawei) {
		this.totalAbajoHuawei = totalAbajoHuawei;
	}
	public Integer getTotalAbajoZte() {
		return totalAbajoZte;
	}
	public void setTotalAbajoZte(Integer totalAbajoZte) {
		this.totalAbajoZte = totalAbajoZte;
	}
	public Integer getTotalAbajoFh() {
		return totalAbajoFh;
	}
	public void setTotalAbajoFh(Integer totalAbajoFh) {
		this.totalAbajoFh = totalAbajoFh;
	}
	public Integer getTotalHuaweiEmp() {
		return totalHuaweiEmp;
	}
	public void setTotalHuaweiEmp(Integer totalHuaweiEmp) {
		this.totalHuaweiEmp = totalHuaweiEmp;
	}
	public Integer getTotalZteEmp() {
		return totalZteEmp;
	}
	public void setTotalZteEmp(Integer totalZteEmp) {
		this.totalZteEmp = totalZteEmp;
	}
	public Integer getTotalFhEmp() {
		return totalFhEmp;
	}
	public void setTotalFhEmp(Integer totalFhEmp) {
		this.totalFhEmp = totalFhEmp;
	}
	public Integer getTotalArribaZteEmp() {
		return totalArribaZteEmp;
	}
	public void setTotalArribaZteEmp(Integer totalArribaZteEmp) {
		this.totalArribaZteEmp = totalArribaZteEmp;
	}
	public Integer getTotalArribaHuaweiEmp() {
		return totalArribaHuaweiEmp;
	}
	public void setTotalArribaHuaweiEmp(Integer totalArribaHuaweiEmp) {
		this.totalArribaHuaweiEmp = totalArribaHuaweiEmp;
	}
	public Integer getTotalArribaFhEmp() {
		return totalArribaFhEmp;
	}
	public void setTotalArribaFhEmp(Integer totalArribaFhEmp) {
		this.totalArribaFhEmp = totalArribaFhEmp;
	}
	public Integer getTotalAbajoHuaweiEmp() {
		return totalAbajoHuaweiEmp;
	}
	public void setTotalAbajoHuaweiEmp(Integer totalAbajoHuaweiEmp) {
		this.totalAbajoHuaweiEmp = totalAbajoHuaweiEmp;
	}
	public Integer getTotalAbajoZteEmp() {
		return totalAbajoZteEmp;
	}
	public void setTotalAbajoZteEmp(Integer totalAbajoZteEmp) {
		this.totalAbajoZteEmp = totalAbajoZteEmp;
	}
	public Integer getTotalAbajoFhEmp() {
		return totalAbajoFhEmp;
	}
	public void setTotalAbajoFhEmp(Integer totalAbajoFhEmp) {
		this.totalAbajoFhEmp = totalAbajoFhEmp;
	}
	public List<vwTotalOntsEmpresarialesEntidad> getTotalesRegionEmp() {
		return totalesRegionEmp;
	}
	public void setTotalesRegionEmp(List<vwTotalOntsEmpresarialesEntidad> totalesRegionEmp) {
		this.totalesRegionEmp = totalesRegionEmp;
	}
	public List<vwTotalOntsVipsEntidad> getTotalesRegionVips() {
		return totalesRegionVips;
	}
	public void setTotalesRegionVips(List<vwTotalOntsVipsEntidad> totalesRegionVips) {
		this.totalesRegionVips = totalesRegionVips;
	}
	
	
}
