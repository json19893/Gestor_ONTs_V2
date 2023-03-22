package totalplay.snmpv2.com.negocio.services;
import totalplay.snmpv2.com.negocio.dto.CadenasMetricasDto;
import totalplay.snmpv2.com.negocio.dto.EjecucionDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import java.io.IOException;
import java.util.List;
public interface IlimpiezaCadena {
	<T extends GenericPoleosDto> List<T> getMetricasBypoleo(EjecucionDto proces, Integer idmetrica,
            Integer idOlt, Integer idRegion, String IdEjecucion, String tecnologia, Class<T> entidad, CadenasMetricasDto cadenas, boolean saveErroneos, int intentos ) throws IOException;
    
}
