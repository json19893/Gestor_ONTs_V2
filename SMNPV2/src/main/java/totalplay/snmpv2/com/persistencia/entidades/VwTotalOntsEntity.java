package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vw_total_onts")
public class VwTotalOntsEntity {
    @Id
    private String _id;
    private Integer id_olt;
    private String nombre;
    private String ip;
    private String descripcion;
    private String tecnologia;
    private Integer id_region;
    private Integer id_configuracion;
    private Integer estatus;
    private Integer pin;
    private boolean descubrio;
    private Integer onts_exito;
    private Integer onts_error;
    private int total_onts;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getId_olt() {
        return id_olt;
    }

    public void setId_olt(Integer id_olt) {
        this.id_olt = id_olt;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public Integer getId_region() {
        return id_region;
    }

    public void setId_region(Integer id_region) {
        this.id_region = id_region;
    }

    public Integer getId_configuracion() {
        return id_configuracion;
    }

    public void setId_configuracion(Integer id_configuracion) {
        this.id_configuracion = id_configuracion;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public boolean isDescubrio() {
        return descubrio;
    }

    public void setDescubrio(boolean descubrio) {
        this.descubrio = descubrio;
    }

    public Integer getOnts_exito() {
        return onts_exito;
    }

    public void setOnts_exito(Integer onts_exito) {
        this.onts_exito = onts_exito;
    }

    public Integer getOnts_error() {
        return onts_error;
    }

    public void setOnts_error(Integer onts_error) {
        this.onts_error = onts_error;
    }

    public int getTotal_onts() {
        return total_onts;
    }

    public void setTotal_onts(int total_onts) {
        this.total_onts = total_onts;
    }
}
