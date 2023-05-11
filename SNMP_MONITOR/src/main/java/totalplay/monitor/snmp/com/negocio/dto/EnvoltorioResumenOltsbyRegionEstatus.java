package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;

@Data
public class EnvoltorioResumenOltsbyRegionEstatus {
    private String tipo;
    private String descripcionCorta;
    private String descripcionLarga;
    private responseRegionDto resumenStatusOnts;
}
