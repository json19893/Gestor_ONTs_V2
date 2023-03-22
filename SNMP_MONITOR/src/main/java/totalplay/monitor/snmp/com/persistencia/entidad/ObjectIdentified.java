package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ObjectIdentified {
    private String oid;
    private String split;
    private String type;

}
