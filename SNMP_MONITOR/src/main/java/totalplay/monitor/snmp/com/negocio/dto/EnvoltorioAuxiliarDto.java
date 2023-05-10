package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;

@Data
public class EnvoltorioAuxiliarDto {
    private String tipo;
    private String descripcionCorta;
    private String descripcionLarga;
    private totalesActivoDto resumenStatusOnts;
}
