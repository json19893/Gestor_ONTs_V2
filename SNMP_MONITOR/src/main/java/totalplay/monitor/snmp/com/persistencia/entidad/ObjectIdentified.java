package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ObjectIdentified {
    private String oid;
    private String split;
    private String type;

}
