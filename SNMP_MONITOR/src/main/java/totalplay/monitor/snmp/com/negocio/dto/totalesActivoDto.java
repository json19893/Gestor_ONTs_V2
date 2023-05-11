package totalplay.monitor.snmp.com.negocio.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class totalesActivoDto {
	public Integer totalHuawei;
	public Integer totalZte;
	public Integer totalFh;
	public Integer totalArribaZte;
	public Integer totalArribaHuawei;
	public Integer totalArribaFh;
	public Integer totalAbajoHuawei;
	public Integer totalAbajoZte;
	public Integer totalAbajoFh;
	public Date ultimaActualizacion;
	public Date proximoDescubrimiento;
	public Integer conteoPdmOnts;
	public Integer totalHuaweiEmp;
	public Integer totalZteEmp;
	public Integer totalFhEmp;
	public Integer totalArribaZteEmp;
	public Integer totalArribaHuaweiEmp;
	public Integer totalArribaFhEmp;
	public Integer totalAbajoHuaweiEmp;
	public Integer totalAbajoZteEmp;
	public Integer totalAbajoFhEmp;
	List<totalGraficaDto> grafica;
	
	
	
	
}
