package totalplay.snmpv2.com.persistencia.entidades;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_historico_conteo_olts")
@Data
@NoArgsConstructor
public class HistoricoConteosOltsEntity {
    @Id
    private String id;
    private Integer idOlt;
    private String fecha_ejecucion;
    private String idProceso;
    private Integer onts_exito;
	private Integer onts_error;

    public HistoricoConteosOltsEntity(Integer idOlt,String fecha_ejecucion, String idProceso,Integer onts_exito,Integer onts_error) {
        this.idOlt = idOlt;
        this.fecha_ejecucion = fecha_ejecucion;
        this.idProceso=idProceso;
        this.onts_exito=onts_exito;
        this.onts_error=onts_error;
        
    }
    
}
