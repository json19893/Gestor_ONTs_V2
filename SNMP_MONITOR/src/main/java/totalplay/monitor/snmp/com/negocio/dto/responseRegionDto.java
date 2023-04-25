package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEmpresarialesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsVipsEntidad;

@Data
@NoArgsConstructor

public class responseRegionDto {
	private List<dataRegionResponseDto> totalesRegion;
	private List<dataRegionResponseDto> totalesRegionEmp;
	private List<dataRegionResponseDto> totalesRegionVips;
	private Integer totalHuawei;
	private Integer totalZte;
	private Integer totalFh;
	private Integer totalArribaZte;
	private Integer totalArribaHuawei;
	private Integer totalArribaFh;
	private Integer totalAbajoHuawei;
	private Integer totalAbajoZte;
	private Integer totalAbajoFh;
	private Integer totalHuaweiEmp;
	private Integer totalZteEmp;
	private Integer totalFhEmp;
	private Integer totalArribaZteEmp;
	private Integer totalArribaHuaweiEmp;
	private Integer totalArribaFhEmp;
	private Integer totalAbajoHuaweiEmp;
	private Integer totalAbajoZteEmp;
	private Integer totalAbajoFhEmp;
	
	
	
}
