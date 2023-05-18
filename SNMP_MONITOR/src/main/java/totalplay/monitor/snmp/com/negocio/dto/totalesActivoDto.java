package totalplay.monitor.snmp.com.negocio.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class totalesActivoDto {
	public Integer totalHuawei= new Integer(0);
	public Integer totalZte= new Integer(0);
	public Integer totalFh= new Integer(0);
	public Integer totalArribaZte= new Integer(0);
	public Integer totalArribaHuawei= new Integer(0);
	public Integer totalArribaFh= new Integer(0);
	public Integer totalAbajoHuawei= new Integer(0);
	public Integer totalAbajoZte= new Integer(0);
	public Integer totalAbajoFh= new Integer(0);
	public Date ultimaActualizacion;
	public Date proximoDescubrimiento;
	public Integer conteoPdmOnts = new Integer(0);
	public Integer totalHuaweiEmp = new Integer(0);
	public Integer totalZteEmp = new Integer(0);
	public Integer totalFhEmp = new Integer(0);
	public Integer totalArribaZteEmp = new Integer(0);
	public Integer totalArribaHuaweiEmp = new Integer(0);
	public Integer totalArribaFhEmp = new Integer(0);
	public Integer totalAbajoHuaweiEmp = new Integer(0);
	public Integer totalAbajoZteEmp = new Integer(0);
	public Integer totalAbajoFhEmp = new Integer(0);
	List<totalGraficaDto> grafica = new ArrayList<>();
	
	
	
	
}
