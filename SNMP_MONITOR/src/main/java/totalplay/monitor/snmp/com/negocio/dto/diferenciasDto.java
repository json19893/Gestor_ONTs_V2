package totalplay.monitor.snmp.com.negocio.dto;

public class diferenciasDto {

private String oid;
private String fecha_descubrimiento;
private Integer id_olts;
private Integer estatus;
public String getOid() {
	return oid;
}
public void setOid(String oid) {
	this.oid = oid;
}
public String getFecha_descubrimiento() {
	return fecha_descubrimiento;
}
public void setFecha_descubrimiento(String fecha_descubrimiento) {
	this.fecha_descubrimiento = fecha_descubrimiento;
}
public Integer getId_olts() {
	return id_olts;
}
public void setId_olts(Integer id_olts) {
	this.id_olts = id_olts;
}
public Integer getEstatus() {
	return estatus;
}
public void setEstatus(Integer estatus) {
	this.estatus = estatus;
}




}
