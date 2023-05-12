package totalplay.services.com.negocio.dto;

import java.util.List;

import totalplay.services.com.persistencia.entidad.inventarioOntsEntidad;

public class respuestaListaInventarioOnts {
	
	private List<inventarioOntsEntidad> dispositivos;

	public List<inventarioOntsEntidad> getDispositivos() {
		return dispositivos;
	}

	public void setDispositivos(List<inventarioOntsEntidad> dispositivos) {
		this.dispositivos = dispositivos;
	}

}
