package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;

@Data
public class EnvoltorioTopLevelRegionAuxiliarDto {
    private int idRegion;
    private String nombreRegion;
    private String descripcion;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntTodoEstatus;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntEmpresarialesEstatus;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntVipsEstatus;
}
