package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;

import java.time.LocalDateTime;

@Data
public class EnvoltorioAuxiliarDto {
    private LocalDateTime date;
    private String tipo;
    private String descripcionCorta;
    private String descripcionLarga;
    private totalesActivoDto resumenStatusOnts;
}
