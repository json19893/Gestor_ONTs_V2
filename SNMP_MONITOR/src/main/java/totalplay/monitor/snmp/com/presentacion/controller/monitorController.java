package totalplay.monitor.snmp.com.presentacion.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import totalplay.monitor.snmp.com.negocio.dto.*;
import totalplay.monitor.snmp.com.negocio.service.IBlockMetricService;
import totalplay.monitor.snmp.com.negocio.service.ICronExecution;
import totalplay.monitor.snmp.com.negocio.service.IconsultaService;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.util.constantes;
import totalplay.monitor.snmp.com.persistencia.entidad.catConfiguracionEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.estatusPoleoManualEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.tblBitacoraEventosEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.usuariosEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwActualizacionEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IcatConfiguracionRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsPdmRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.ImonitorPoleoRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.ItblDescubrimientoManualRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IusuariosRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IvwActualizacionRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IvwTotalOntsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.bitacoraEventosRepository;


@Controller
@RestController
public class monitorController extends constantes {
    @Autowired
    ImonitorService monitorServicio;
    @Autowired
    private IcatOltsRepositorio catalogoOlt;
    @Autowired
    IconsultaService consulta;
    // @Autowired
    // IOltsCMDBDAO<oltsCMDBDto> oltsCMDB;
    @Autowired
    ICronExecution cronExecution;
    @Autowired
    IinventarioOntsRepositorio inventario;
    @Autowired
    ImonitorPoleoRepositorio monitorPoleo;
    @Autowired
    IvwActualizacionRepositorio vwActualizacion;
    @Autowired
    IinventarioOntsPdmRepositorio inventarioPdm;
    @Autowired
    IcatOltsRepositorio catOlts;
    @Autowired
    IusuariosRepositorio usuarios;

    @Autowired
    ImonitorPoleoRepositorio monitor;
    @Autowired
    IcatConfiguracionRepositorio configuracion;
    @Autowired
    ItblDescubrimientoManualRepositorio descubrimientoManual;
    @Autowired
    bitacoraEventosRepository bitacoraEventos;
    @Autowired
    IvwTotalOntsRepositorio vwOnts;

    @Autowired
    IBlockMetricService BlockMetricService;

    /**
     * Mètodo que busca las olts, sus totales por tecnologìa y las onts
     * empresariales por regiòn,
     *
     * @param tipo: T (totales), E (Empresariales), V (Vips)
     * @return retorna una estructura getOltsByRegion
     **/

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getOltsByRegion/{idRegion}/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseRegionDto getOltsByRegion(@PathVariable("idRegion") Integer idRegion,
                                             @PathVariable("tipo") String tipo) throws Exception {
        responseRegionDto response = new responseRegionDto();

        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0) {
            try {

                response = monitorServicio.getOltsByRegion(idRegion, tipo, false);
            } catch (Exception e) {

            }

            return response;
        }
        return null;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getRegion", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<catRegionDto> getRegion() throws Exception {
        List<catRegionDto> response = new ArrayList<catRegionDto>();
        try {
            response = monitorServicio.getRegion();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getOntsByOltsUp/{idOlt}/{estatus}/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<inventarioOntsEntidad> getOntsByOlts(@PathVariable("idOlt") Integer idOlt,
                                                     @PathVariable("estatus") Integer estatus, @PathVariable("tipo") String tipo) throws Exception {

        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0) {
            return monitorServicio.getOntsByOlts(idOlt, estatus, tipo);
        } else {
            return null;
        }

    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getTotalesByOlt/{idOlt}/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public totalesOltsDto getTotalesByOlt(@PathVariable("idOlt") Integer idOlt, @PathVariable("tipo") String tipo)
            throws Exception {
        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0) {
            return monitorServicio.getTotalesByOlt(idOlt, tipo);
        } else {
            return null;
        }

    }

    /**
     * Mètodo que busca las onts totales de una olt segùn su clasificacion (totales,
     * empresariales, vips)
     *
     * @param tipo: T (totales), E (Empresariales), V (Vips)
     * @return devuelve una lista de inventarioOntsEntidad
     **/
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/finOntsByIdAll/{idOlt}/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<inventarioOntsEntidad> finOntsByIdAll(@PathVariable("idOlt") Integer idOlt,
                                                      @PathVariable("tipo") String tipo) throws Exception {
        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0) {
            return monitorServicio.finOntsByIdAll(idOlt, tipo);
        } else {
            return null;
        }
    }

    /**
     * Mètodo que devuelve las onts que han sufrido cambios con repecto a la
     * ejecuciòn previa
     *
     * @param tipo: T (totales), E (Empresariales), V (Vips)
     * @return devuelve una lista de tbHistoricoDto
     **/
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getHistoricoCambios/{idOlt}/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<tbHistoricoDto> getHistoricoCambios(@PathVariable("idOlt") Integer idOlt,
                                                    @PathVariable("tipo") String tipo) throws Exception {

        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0) {
            return monitorServicio.getHistoricoCambios(idOlt, tipo);
        } else {
            return null;
        }

    }

    /**
     * Mètodo que devuelve los totales de onts y olts po regiòn, asì como su
     * calsificaciòn poleadas, no poleadas, arriba y anbjo.
     *
     * @param tipo: T (totales), E (Empresariales), V (Vips)
     * @return devuelve una lista de datosRegionDto
     **/
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getTotalesByTecnologia/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<datosRegionDto> getTotalesByTecnologia(@PathVariable("tipo") String tipo) throws Exception {

        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0) {
            return monitorServicio.getTotalesByTecnologia(tipo);
        }

        return null;

    }

    /**
     * Mètodo que devuelve los totales de olts y onts empresariales por tecnologìa,
     *
     * @param tipo: T (totales), E (Empresariales), V (Vips)
     * @return retorna una estructura totalesActivoDto
     **/
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getTotalesActivo/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public totalesActivoDto getTotalesActivo(@PathVariable("tipo") String tipo) throws Exception {

        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0) {

            return monitorServicio.getTotalesActivo(tipo);
        }

        return null;

    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getDatosMonitoreo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseMonitoreo getDatosMonitoreo() throws Exception {

        return monitorServicio.getDatosMonitoreo();

    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getSerie/{oid}/{ip}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getSerie(@PathVariable("oid") String oid, @PathVariable("ip") String ip)
            throws Exception {

        return consulta.consultaNumeroSerie(oid, ip);
    }

    @RequestMapping(value = "/getPoleoOlts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPoleoOlts() throws Exception {
        String response = "";
        try {
            boolean res = cronExecution.updateOlts();
            if (res) {
                response = "ok";
            } else {
                response = "error";
            }
        } catch (Exception e) {
            response = "error: " + e;
        }

        return response;

    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/findOlt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseFindDto findOlt(@RequestBody requestOltDto oltData) {

        responseFindDto response = new responseFindDto();
        String data = "";
        response.setSuccess(false);

        if (oltData.getIp() != null || oltData.getNombre() != null) {
            data = oltData.getIp() != null ? oltData.getIp() : oltData.getNombre();

            if (oltData.getTipo() != null) {

                response = consulta.getOlt(oltData.getTipo(), data, oltData.getIp() != null);

            } else {
                response.setMessage("Debe de ingresar una tipo de clasificaciòn");
            }

        } else {
            response.setMessage("Debe ingresar el nombre o la ip");
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/actualizaEstatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public respuestaStatusDto cambiarEstatusOnt(@RequestBody requestEstatusUserDto datos) {
        tblBitacoraEventosEntidad bt = new tblBitacoraEventosEntidad();
        bt.setFecha(LocalDateTime.now().toString());
        bt.setModulo(DES_ACTUALIZACION_ONT);
        bt.setUsuario(datos.getUsuario());
        bt.setDescripcion(DESC_EVENTO_CAMBIO_ESTATUS_ONTS + datos.getLista().toString());
        bitacoraEventos.save(bt);
        respuestaStatusDto response = new respuestaStatusDto();
        if (datos != null) {
            response = consulta.cambiarEstatusOnt(datos.getLista(), datos.getUsuario());
        } else {
            response.setSms("Debe ingresar onts");
        }
        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/consultaOLTs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<catOltsEntidad> obtenerOLTsActivas() {

        List<catOltsEntidad> response = new ArrayList<catOltsEntidad>();
        String data = "";
        // response.setSuccess(false);

        response = consulta.obtenerOLTsActivas();

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/findOnt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseFindOntDto findOnt(@RequestBody requestOntDto ontData) {

        responseFindOntDto response = new responseFindOntDto();
        String data = "";
        response.setSuccess(false);

        if (ontData.getNumeroSerie() != null || ontData.getAlias() != null) {
            data = ontData.getNumeroSerie() != null ? ontData.getNumeroSerie() : ontData.getAlias();

            if (ontData.getTipo() != null) {

                response = consulta.getOnt(ontData.getTipo(), data, ontData.getNumeroSerie() != null);

            } else {
                response.setMessage("Debe de ingresar una tipo de clasificaciòn");
            }

        } else {
            response.setMessage("Debe ingresar el nombre o la ip");
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getNombreByRegex/{regex}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<catOltsEntidad> getNombreByRegex(@PathVariable("regex") String regex) throws Exception {
        return catalogoOlt.findNombreByRegex("^" + regex);
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getAliasByRegex/{regex}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<inventarioOntsEntidad> getAliasByRegex(@PathVariable("regex") String regex) throws Exception {

        return inventario.findAliasByRegex(regex);
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getIpByRegex/{regex}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<catOltsEntidad> getIpByRegex(@PathVariable("regex") String regex) throws Exception {

        return catalogoOlt.findIpByRegex("^" + regex);
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getSerieByRegex/{regex}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<inventarioOntsEntidad> getSerieByRegex(@PathVariable("regex") String regex) throws Exception {

        return inventario.findSerieByRegex("^" + regex);
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getDetalleActualizacion/{tipo}/{skip}/{limit}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public detalleActualizadasDto getDetalleActualizacion(@PathVariable("tipo") String tipo,
                                                          @PathVariable("skip") Integer skip, @PathVariable("limit") Integer limit) throws Exception {
        detalleActualizadasDto response = new detalleActualizadasDto();
        try {
            if (tipo.equals("E")) {
                if (skip > 0) {
                    response.setTotal(inventarioPdm.finOntsByTotalE() + inventario.findTotalCambiosE());
                    response.setDetalle(inventario.getDetalleActualizacionESkip(skip, limit));
                } else {
                    response.setTotal(inventarioPdm.finOntsByTotalE() + inventario.findTotalCambiosE());
                    response.setDetalle(inventario.getDetalleActualizacionE(limit));

                }
            } else {
                if (skip > 0) {
                    response.setTotal(inventarioPdm.finOntsByTotalT() + inventario.findTotalCambiosT());
                    response.setDetalle(inventario.getDetalleActualizacionTSkip(skip, limit));
                } else {
                    response.setTotal(inventarioPdm.finOntsByTotalT() + inventario.findTotalCambiosT());
                    response.setDetalle(inventario.getDetalleActualizacionT(limit));
                }
            }
        } catch (Exception e) {
            System.out.println("Error:: " + e);
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getRegexActualizacion/{tipo}/{numeroSerie}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<vwActualizacionEntidad> getRegexActualizacion(@PathVariable("tipo") String tipo,
                                                              @PathVariable("numeroSerie") String numeroSerie) throws Exception {
        List<vwActualizacionEntidad> response = new ArrayList<vwActualizacionEntidad>();
        try {
            if (tipo.equals("E")) {
                response = vwActualizacion.findSerieByRegexE("^" + numeroSerie);
            } else {
                response = vwActualizacion.findSerieByRegexT("^" + numeroSerie);
            }
        } catch (Exception e) {
            System.out.println("Error:: " + e);
        }

        return response;
    }

    // Servicio obtener las mètricas de una ont
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getMetrics/{idOlt}/{oid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseMetricasDto getMetrics(@PathVariable("idOlt") Integer idOlt, @PathVariable("oid") String oid)
            throws Exception {

        if (idOlt != null && oid != null) {
            return consulta.getMetrics(idOlt, oid);
        }

        return null;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getOlts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<vwTotalOntsEntidad> getOlts() throws Exception {

        try {
            List<vwTotalOntsEntidad> lista = vwOnts.findAll();
            return lista;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getOntDetalleAc/{tipo}/{numeroSerie}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<vwActualizacionEntidad> getOntDetalleAc(@PathVariable("tipo") String tipo,
                                                        @PathVariable("numeroSerie") String numeroSerie) throws Exception {
        List<vwActualizacionEntidad> response = new ArrayList<vwActualizacionEntidad>();
        try {
            if (tipo.equals("E")) {
                response = vwActualizacion.findSerieE(numeroSerie);
            } else {
                response = vwActualizacion.findSerieT(numeroSerie);
            }
        } catch (Exception e) {
            System.out.println("Error:: " + e);
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/validaUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseValidaUsuarioDto validaUser(@RequestBody usuariosEntidad data) {

        responseValidaUsuarioDto response = new responseValidaUsuarioDto();

        try {
            usuariosEntidad user = usuarios.getUsuario(data.getUsuario(), data.getPassword());
            if (user != null) {
                response.setSuccesss(true);
            } else {
                response.setMensaje("Usuario o contraseña incorrecta");
            }
        } catch (Exception e) {
            response.setMensaje("Problemas al validar al usuario");
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/updateStatusOlt/{idOlt}/{estatus}/{usuario}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseValidaUsuarioDto updateStatusOlt(@PathVariable("idOlt") Integer idOlt,
                                                    @PathVariable("estatus") Integer estatus, @PathVariable("usuario") String usuario) {

        responseValidaUsuarioDto response = new responseValidaUsuarioDto();

        try {
            tblBitacoraEventosEntidad bt = new tblBitacoraEventosEntidad();


            catOltsEntidad olt = catOlts.findOltByIdolt(idOlt);
            bt.setFecha(LocalDateTime.now().toString());
            bt.setModulo(DES_ACTUALIZACION_E);
            bt.setUsuario(usuario);
            bt.setDescripcion(DESC_EVENTO_CAMBIO_ESTATUS + olt.getIp() + " nombre: " + olt.getNombre() + " a estatus: " + estatus);
            bitacoraEventos.save(bt);
            olt.setEstatus(estatus);
            catOlts.save(olt);
            response.setMensaje("OK");
            response.setSuccesss(true);
        } catch (Exception e) {
            response.setMensaje("No se pudo actualizar la olt");
            response.setSuccesss(false);
        }

        return response;
    }

    @SuppressWarnings("unused")
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/validaMaximoDescunbrimiento", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer validaMaximoDescunbrimiento() {

        Integer response = 0;

        try {
            catConfiguracionEntidad cofiguracion = configuracion.getIntentos(8, LocalDate.now().toString());

            if (cofiguracion == null) {
                catConfiguracionEntidad cofig = configuracion.getIntentos(8, LocalDate.now().minusDays(1).toString());
                if (cofig != null) {
                    cofig.setFecha(LocalDate.now().toString());
                    cofig.setDescubrimientos(5);
                    configuracion.save(cofig);
                }
            } else {
                if (cofiguracion.getDescubrimientos() == 0
                        && cofiguracion.getFecha().equals(LocalDate.now().toString())) {
                    cofiguracion.setFecha(LocalDate.now().plusDays(1).toString());
                    cofiguracion.setDescubrimientos(5);
                    configuracion.save(cofiguracion);
                }
                response = cofiguracion.getDescubrimientos();
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getDetalleDescubrimiento", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<estatusPoleoManualEntidad> getDetalleDescubrimiento() {

        List<estatusPoleoManualEntidad> response = new ArrayList<estatusPoleoManualEntidad>();

        try {
            response = descubrimientoManual.getDetalle(LocalDate.now().toString());
        } catch (Exception e) {

        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/changeMetricBlock/{idconfigmetric}/block/{idblock}", produces = MediaType.APPLICATION_JSON_VALUE)
    public responseDto MetricBlock(@PathVariable("idconfigmetric") int id_metrica, @PathVariable("idblock") int id_bloque) {
        return BlockMetricService.changeMetricBlock(id_metrica, id_bloque);
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/configmetric", produces = MediaType.APPLICATION_JSON_VALUE)
    public responseDto getAllConfigMetric() {
        return BlockMetricService.getAllConfigMetrics();
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/removeMetricBlock/{idconfigmetric}/block/{idblock}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseDto removeMetricfromBlock(@PathVariable("idconfigmetric") int id_metrica, @PathVariable("idblock") int id_bloque) {
        return BlockMetricService.removeMetricBlock(id_metrica, id_bloque);
    }
}
