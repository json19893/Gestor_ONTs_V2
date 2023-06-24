package totalplay.monitor.snmp.com.presentacion.controller;


import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import totalplay.monitor.snmp.com.negocio.dto.*;
import totalplay.monitor.snmp.com.negocio.service.IBlockMetricService;
import totalplay.monitor.snmp.com.negocio.service.IProcesamientoTotalesOntService;
import totalplay.monitor.snmp.com.negocio.service.IconsultaService;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.service.impl.InsertaOntsServiceImpl;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IEstadoOntsResumenService;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IUpdateOLTsNCEService;
import totalplay.monitor.snmp.com.negocio.util.constantes;
import totalplay.monitor.snmp.com.negocio.util.utils;
import totalplay.monitor.snmp.com.persistencia.entidad.*;
import totalplay.monitor.snmp.com.persistencia.repository.*;


@RestController
@Slf4j
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class monitorController extends constantes {
    @Autowired
    ImonitorService monitorServicio;
    @Autowired
    private IcatOltsRepositorio catalogoOlt;
    @Autowired
    IconsultaService consulta;
 
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
    ImonitorPoleoManualRepository monitorPoleoManual;
    @Autowired
    IdetalleActualizacionRepositorio detalleAct;    
    @Autowired
    IinventarioRechazadosNCERepositorio rechazadasNCE;
    
    @Autowired
    IBlockMetricService BlockMetricService;
    @Autowired
    IEstadoOntsResumenService estadoOntsResumenService;
    @Autowired
    IEnvoltorioOntsTotalesActivoRepositorio repositorioOntEstatusTotales;
    @Autowired
    IEnvoltorioGetOltsByRegionRepository getOltsByRegionRepository;

    @Autowired
    ITotalesByTecnologiaRepository totalesByTecnologiaRepository;
    @Autowired
    InsertaOntsServiceImpl inserta;
    
    @Autowired
    IUpdateOLTsNCEService oltsNCE;
    @Autowired
    IUsuariosPermitidosRepositorio usuariosPermitidos;
    
   
    
    /**
     * Mètodo que busca las olts, sus totales por tecnologìa y las onts
     * empresariales por regiòn,
     *
     * @param tipo: T (totales), E (Empresariales), V (Vips)
     * @return retorna una estructura getOltsByRegion
     **/

     @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getOntsByOltsUp/{idOlt}/{estatus}/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<inventarioOntsEntidad> getOntsByOlts(@PathVariable("idOlt") Integer idOlt,
            @PathVariable("estatus") Integer estatus, @PathVariable("tipo") String tipo) throws Exception {

        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0) {
            return monitorServicio.getOntsByOlts(idOlt, estatus, tipo);
        } else {
            return null;
        }

    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getTotalesByTecnologia/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<datosRegionDto> getTotalesByTecnologia(@PathVariable("tipo") String tipo) throws Exception {

        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0 || tipo.compareTo("S") == 0) {
            TotalesByTecnologiaEntidad resumenExist = totalesByTecnologiaRepository.getEntity(tipo);
            if(resumenExist != null){
                return resumenExist.getResumenStatusOnts();
            }
            //Si no existe el resumen se manda a llamar directamente la logica del negocio:
            List<datosRegionDto> response = monitorServicio.getTotalesByTecnologia(tipo);
            return response;
        }
        return null;
    }

    /**
     * Mètodo que devuelve los totales de olts y onts empresariales por tecnologìa,
     *
     * @param tipo: T (totales), E (Empresariales), V (Vips)
     * @return retorna una estructura totalesActivoDto
     **/
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getTotalesActivo/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public totalesActivoDto getTotalesActivo(@PathVariable("tipo") String tipo) throws Exception {
        LocalTime timeRequestClient = utils.getDateTime().toLocalTime();

        if (tipo.compareTo("T") == 0 || tipo.compareTo("E") == 0 || tipo.compareTo("V") == 0||tipo.compareTo("S") == 0) {
            EnvoltorioOntsTotalesActivoEntidad resumenExist = repositorioOntEstatusTotales.getEntity(tipo.toUpperCase());
            if(resumenExist != null){
                return resumenExist.getTotalesOntsActivas();
            }
            return monitorServicio.getTotalesActivo(tipo);
        }
        return null;
    }
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getDatosMonitoreo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseMonitoreo getDatosMonitoreo() throws Exception {

        return monitorServicio.getDatosMonitoreo();

    }
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getSerie/{oid}/{ip}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getSerie(@PathVariable("oid") String oid, @PathVariable("ip") String ip)
            throws Exception {

        return consulta.consultaNumeroSerie(oid, ip);
    }


    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @PostMapping(value = "/actualizaEstatus")
    public respuestaStatusDto cambiarEstatusOnt(@RequestBody requestEstatusUserDto datos) {
        bitacoraEventos.save(new tblBitacoraEventosEntidad(LocalDateTime.now().toString(), DES_ACTUALIZACION_ONT,datos.getUsuario(),DESC_EVENTO_CAMBIO_ESTATUS_ONTS + datos.getLista().toString()));
        respuestaStatusDto response = new respuestaStatusDto();
        if (datos != null) {
            response = consulta.cambiarEstatusOnt(datos.getLista(), datos.getUsuario());
        } else {
            response.setSms("Debe ingresar onts");
        }
        return response;
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/consultaOLTs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<catOltsEntidad> obtenerOLTsActivas() {

        List<catOltsEntidad> response = new ArrayList<catOltsEntidad>();
        String data = "";
        // response.setSuccess(false);

        response = consulta.obtenerOLTsActivas();

        return response;
    }
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/findOnt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseFindOntDto findOnt(@RequestBody requestOntDto ontData) {

        responseFindOntDto response = new responseFindOntDto();
        String data = "";
        response.setSuccess(false);

        if (!ontData.getNumeroSerie().equals("null") || !ontData.getAlias().equals("null")) {
            data = !ontData.getNumeroSerie().equals("null") ? ontData.getNumeroSerie() : ontData.getAlias();

            if (ontData.getTipo() != null) {

                response = consulta.getOnt(ontData.getTipo(), data, !ontData.getNumeroSerie().equals("null") );

            } else {
                response.setMessage("Debe de ingresar una tipo de clasificaciòn");
            }

        } else {
            response.setMessage("Debe ingresar el nombre o la ip");
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getNombreByRegex/{regex}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<catOltsEntidad> getNombreByRegex(@PathVariable("regex") String regex) throws Exception {
        return catalogoOlt.findNombreByRegex("^" + regex);
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getAliasByRegex/{regex}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<inventarioOntsEntidad> getAliasByRegex(@PathVariable("regex") String regex) throws Exception {

        return inventario.findAliasByRegex(regex);
    }
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getIpByRegex/{regex}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<catOltsEntidad> getIpByRegex(@PathVariable("regex") String regex) throws Exception {

        return catalogoOlt.findIpByRegex("^" + regex);
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getSerieByRegex/{regex}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<inventarioOntsEntidad> getSerieByRegex(@PathVariable("regex") String regex) throws Exception {

        return inventario.findSerieByRegex("^" + regex);
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
     log.error("error", e);
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
            log.error("error", e);
        }

        return response;
    }

    // Servicio obtener las mètricas de una ont
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getMetrics/{idOlt}/{oid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseMetricasDto getMetrics(@PathVariable("idOlt") Integer idOlt, @PathVariable("oid") String oid)
            throws Exception {

        if (idOlt != null && oid != null) {
            return consulta.getMetrics(idOlt, oid);
        }

        return null;
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = {"/getOlts/{user}", "/getOlts"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<catOltsEntidad> getOlts(@PathVariable(required = false, name = "user") String user) throws Exception {
    	List<catOltsEntidad> lista=null;
    	
        try {
        	
        	UsuariosPermitidosEntidad usuario =  usuariosPermitidos.getUsuario(user);
    		 		
    		if(user!=null) {
    			//buscar sobre la olt
        		List<DescubrimientoNCEUsuariosDto> descubrimientos = usuario.getDescubrimientos();
    			List<Integer> olts=new ArrayList<Integer>();
        		
    			for(DescubrimientoNCEUsuariosDto des:descubrimientos ) {
    				olts.add(des.getOlt());
    			}       		
        		
        		lista = catalogoOlt.getOltsUser(olts);
        	}else {
    			lista = catalogoOlt.findAll();
    		}
    		return lista;
        } catch (Exception e) {
            log.error("error", e);
            return null;
        }

    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
            log.error("error", e);
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
            log.error("error", e);
            response.setMensaje("Problemas al validar al usuario");
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/updateStatusOlt/{idOlt}/{estatus}/{usuario}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseValidaUsuarioDto updateStatusOlt(@PathVariable("idOlt") Integer idOlt,
            @PathVariable("estatus") Integer estatus, @PathVariable("usuario") String usuario) {

        responseValidaUsuarioDto response = new responseValidaUsuarioDto();

        try {
          

            catOltsEntidad olt = catOlts.findOltByIdolt(idOlt);
  
            bitacoraEventos.save(new tblBitacoraEventosEntidad(LocalDateTime.now().toString(),DES_ACTUALIZACION_E,usuario,DESC_EVENTO_CAMBIO_ESTATUS + olt.getIp() + " nombre: " + olt.getNombre() + " a estatus: "
            + estatus));
            olt.setEstatus(estatus);
            catOlts.save(olt);
            response.setMensaje("OK");
            response.setSuccesss(true);
        } catch (Exception e) {
            log.error("error", e);
            response.setMensaje("No se pudo actualizar la olt");
            response.setSuccesss(false);
        }

        return response;
    }

    @SuppressWarnings("unused")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
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
            log.error("error", e);
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getDetalleDescubrimiento", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<estatusPoleoManualEntidad> getDetalleDescubrimiento() {

        List<estatusPoleoManualEntidad> response = new ArrayList<estatusPoleoManualEntidad>();

        try {
            response = descubrimientoManual.getDetalle(LocalDate.now().toString());
        } catch (Exception e) {
            log.error("error", e);
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/changeMetricBlock/{idconfigmetric}/block/{idblock}", produces = MediaType.APPLICATION_JSON_VALUE)
    public responseDto MetricBlock(@PathVariable("idconfigmetric") int id_metrica,
            @PathVariable("idblock") int id_bloque) {
        return BlockMetricService.changeMetricBlock(id_metrica, id_bloque);
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/configmetric", produces = MediaType.APPLICATION_JSON_VALUE)
    public responseDto getAllConfigMetric() {
        return BlockMetricService.getAllConfigMetrics();
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/removeMetricBlock/{idconfigmetric}/block/{idblock}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseDto removeMetricfromBlock(@PathVariable("idconfigmetric") int id_metrica,
            @PathVariable("idblock") int id_bloque) {
        return BlockMetricService.removeMetricBlock(id_metrica, id_bloque);
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getOltsOnts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<responseOltsOntsDto> getOltsOnts() {
        return inventario.getOltsOnts();
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getMonitorInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<responseMonitorMetricasManualInfoDto> getMonitorInfo() {
        return monitorPoleoManual.getMonitorData();
    }

    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getArchivo/{archivo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getArchivo(@PathVariable("archivo") Integer archivo) {
        return consulta.getArchivo(archivo);
    }
    
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getDetalleActuacionData/{tipo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<detalleActualizacionesEntidad> getActualizacionData(@PathVariable("tipo") String tipo) throws Exception {
        List<detalleActualizacionesEntidad> response = new ArrayList<detalleActualizacionesEntidad>();
        try {
        	 
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        	Date fecha = new Date();//format.parse( LocalDate.now().toString()+"T00:00:00.000Z");
        	fecha.setHours(0);
        	fecha.setMinutes(0);
        	fecha.setSeconds(0);
        	
            if (tipo.equals("E")) {
            	return detalleAct.getDetalleEmpresariales(fecha);
            } else if(tipo.equals("V")) {
            	return detalleAct.getDetalleVips(fecha);
            } else {
            	return detalleAct.getDetalle(fecha);
            }
        } catch (Exception e) {
            log.error("error", e);
        }

        return response;
    }
    
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/getDetalleActuacionSerie/{tipo}/{serie}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<detalleActualizacionesEntidad> getDetalleActuacionSerie(@PathVariable("tipo") String tipo,@PathVariable("serie") String serie) throws Exception {
        List<detalleActualizacionesEntidad> response = new ArrayList<detalleActualizacionesEntidad>();
        try {
        	        	
            if (tipo.equals("E")) {
            	return detalleAct.getDetalleBySerieEmp(serie);
            } else {
            	return detalleAct.getDetalleBySerie(serie);
            }
        } catch (Exception e) {
            log.error("error", e);
        }

        return response;
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "/actualizaOltOnOnt/{idOlt}/{serie}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public responseDto actualizaOltOnOnt(@PathVariable("idOlt") Integer idOlt,@PathVariable("serie") String serie) throws Exception {
        responseDto response = new responseDto();
        try {
        	        	
            response=  consulta.actualizaOnt(serie,idOlt);

        } catch (Exception e) {
            log.error("error", e);
        }

        return response;
    }
    
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getSinActualizar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InventarioRechazadoNCEDto> getSinActualizar() throws Exception {

        return catOlts.getRechazadasNCE();
    }
    
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/getAceptadasInventario/{olt}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InventarioAceptadasNCEDto> getAceptadasInventario(@PathVariable("olt") Integer olt) throws Exception {

        return catOlts.getaceptadasInventario(olt);
    }

    
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = {"/getRechazadasByOlt/{olt}/{ip}/{initDate}/{finishDate}", "/getRechazadasByOlt/{olt}/{ip}/{initDate}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<inventarioOntsEntidad> getAceptadasInventario(@PathVariable("olt") Integer olt, @PathVariable("ip") String ip, @PathVariable("initDate") String initDate, @PathVariable(required = false, name = "finishDate") String finishDate) throws Exception {
    	//"yyyy-MM-dd'T'HH:mm:ss.SSSX"
    	String fDate="";
    	Date fechaFinal= null;
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");    	
    	SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    	
    	String iDate = initDate.split("T")[0];
		Date fechaInicial = format.parse(iDate);
		
		if(finishDate == null ) {
			fechaFinal = Date.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant().minus(1,ChronoUnit.HOURS));finishDate= LocalDateTime.now().toString();
			
		}else {
			finishDate =  finishDate.replaceAll("\\+", "=");
			fDate = finishDate.split("=")[0];			
			fechaFinal= formatTime.parse(fDate);
		}
		
    	return rechazadasNCE.getRechazadasNCE(ip, olt, fechaInicial, fechaFinal);
    }
    
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = {"/getRechazadasByOltInventario/{olt}/{ip}/{user}/{initDate}/{finishDate}", "/getRechazadasByOltInventario/{olt}/{ip}/{user}/{initDate}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AceptadasNCEDto> getRechazadasByOltInventario(@PathVariable("olt") Integer olt, @PathVariable("ip") String ip, @PathVariable("user") String user, @PathVariable("initDate") String initDate, @PathVariable(required = false, name = "finishDate") String finishDate) throws Exception {
    	
    	String fDate="";
    	Date fechaFinal= null;
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");    	
    	SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    	
    	String iDate = initDate.split("T")[0];
		Date fechaInicial = format.parse(iDate);
		
		//Econtrar al usuario y obtener el id de la ejecuciòn
		
		UsuariosPermitidosEntidad usuario =  usuariosPermitidos.getUsuario(user);
		//UsuariosPermitidosEntidad usuario = usuarios.get(1);
		if(usuario == null)
				return null;
		
		
		//buscar sobre la olt
		List<DescubrimientoNCEUsuariosDto> descubrimientos = usuario.getDescubrimientos();
		
		
		
		//seter los valores del descubrimiento
		DescubrimientoNCEUsuariosDto descubrimiento = new  DescubrimientoNCEUsuariosDto();
		descubrimiento.setOlt(olt);
		
		int index = descubrimientos.indexOf(descubrimiento);
		
		
		if( index == -1) {
			return null;
		}
		
		String ejecucion = descubrimientos.get(index).getId_ejecucion();
		
		
		if(finishDate == null ) {
			fechaFinal = Date.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant().minus(1,ChronoUnit.HOURS));finishDate= LocalDateTime.now().toString();
			
		}else {
			fDate = finishDate.split("T")[0];
			fechaFinal = format.parse(fDate);
//			finishDate =  finishDate.replaceAll("\\+", "=");
//			fDate = finishDate.split("=")[0];			
//			fechaFinal= formatTime.parse(fDate);
		}
    	
        return rechazadasNCE.getRechazadasNCEInventario(ip, olt, fechaInicial, fechaFinal, ejecucion);
    }
    
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@GetMapping("/insertInventario/{serie}/{tipo}/{ejecucion}")
	public GenericResponseDto insertInventario(@PathVariable("serie") String serie, @PathVariable("tipo") String tipo, @PathVariable("ejecucion") String ejecucion) throws Exception {
		String respuesta = "EJECUCION EXITOSA";
		
		try {	
			
			if(!serie.equals("") && !tipo.equals("") && !ejecucion.equals("")) {
					respuesta = inserta.insertInventario(serie, tipo,ejecucion );
			}else {
				return  new GenericResponseDto("EJECUCION ERROR", 1);
			}				
		} catch (Exception e) {
			return  new GenericResponseDto("EJECUCION ERROR", 1);
		}
 		return new GenericResponseDto(respuesta, 0);
	}
    
    

}