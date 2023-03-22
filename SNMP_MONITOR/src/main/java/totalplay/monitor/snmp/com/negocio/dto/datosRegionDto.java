package totalplay.monitor.snmp.com.negocio.dto;

public class datosRegionDto {

	public Integer idRegion;
	public String region;
	public Integer totalOlt;
	public Integer totalRegion;
	public Integer totalOnt;
	public Integer arriba;
	public Integer abajo;
	public Integer cambios;
	public Integer estatus;
	public Integer sinInformacion;

	public Integer getIdRegion() {
		return idRegion;
	}

	public void setIdRegion(Integer idRegion) {
		this.idRegion = idRegion;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	
	public Integer getTotalOnt() {
		return totalOnt;
	}

	public void setTotalOnt(Integer totalOnt) {
		this.totalOnt = totalOnt;
	}

	public Integer getArriba() {
		return arriba;
	}

	public void setArriba(Integer arriba) {
		this.arriba = arriba;
	}

	public Integer getAbajo() {
		return abajo;
	}

	public void setAbajo(Integer abajo) {
		this.abajo = abajo;
	}

	public Integer getCambios() {
		return cambios;
	}

	public void setCambios(Integer cambios) {
		this.cambios = cambios;
	}

	public Integer getEstatus() {
		return estatus;
	}

	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}

	public Integer getTotalOlt() {
		return totalOlt;
	}

	public void setTotalOlt(Integer totalOlt) {
		this.totalOlt = totalOlt;
	}

	public Integer getTotalRegion() {
		return totalRegion;
	}

	public void setTotalRegion(Integer totalRegion) {
		this.totalRegion = totalRegion;
	}

	public Integer getSinInformacion() {
		return sinInformacion;
	}

	public void setSinInformacion(Integer sinInformacion) {
		this.sinInformacion = sinInformacion;
	}
	

}
