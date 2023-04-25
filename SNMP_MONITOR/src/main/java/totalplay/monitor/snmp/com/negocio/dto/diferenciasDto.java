package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class diferenciasDto {

private String oid;
private String fecha_descubrimiento;
private Integer id_olt;
private Integer estatus;




}
