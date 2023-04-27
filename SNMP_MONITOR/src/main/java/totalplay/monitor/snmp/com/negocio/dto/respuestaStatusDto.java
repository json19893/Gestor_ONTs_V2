package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.detalleActualizacionesEntidad;


@Data
@NoArgsConstructor
public class respuestaStatusDto {
	private String sms;
	private Integer cod;
	private Integer totalActualizadas;
	private Integer totalRecibidas;
	private List<detalleActualizacionesEntidad>  noActualizadas;
	private List<requestEstatusDto> data;







}