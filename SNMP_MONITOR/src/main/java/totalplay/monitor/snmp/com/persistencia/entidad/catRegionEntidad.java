package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "cat_regiones")
@Data
@NoArgsConstructor
public class catRegionEntidad {
@Id
private String _id;
private Integer id_region;
private String nombre_region;
private Integer estatus;





}
