package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EnvoltorioAuxiliarOntsByTecnologiaDto {
    private LocalDateTime dateTime;
    private String tipo;
    private String descripcionCorta;
    private String descripcionLarga;
    private List<datosRegionDto> resumenStatusOnts;
}
