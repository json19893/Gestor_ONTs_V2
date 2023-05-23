package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnvoltorioTopLevelRegionAuxiliarDto {
    private int idRegion;
    private LocalDateTime fechaPoleo;
    private String nombreRegion;
    private String descripcion;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntTodoEstatus;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntEmpresarialesEstatus;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntVipsEstatus;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntSAEstatus;
}
