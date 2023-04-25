package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class requestEstatusUserDto {

	private List<requestEstatusDto> lista;
	private String usuario;
	
	public List<requestEstatusDto> getLista() {
		return lista;
	}
	public void setLista(List<requestEstatusDto> lista) {
		this.lista = lista;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	


}