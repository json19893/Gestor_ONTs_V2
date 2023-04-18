export class AppUrlSettings{


    public static BASE_API = 'http://localhost:9081/';
    //public static BASE_API_LOGIN = 'http://localhost:9084/';
    //public static BASE_API_DESCUBRIMIENTO = 'http://localhost:9080/';
    //public static BASE_API = 'http://10.180.199.75:9081/';
    public static BASE_API_DESCUBRIMIENTO = 'http://localhost:9082/';
    public static BASE_API_LOGIN = 'http://10.180.199.75:9084/';
    //public static BASE_API = 'http://10.180.251.83:9081/';
    //public static BASE_API_DESCUBRIMIENTO = 'http://10.180.251.83:9080/';
    //public static BASE_API_LOGIN = 'http://10.180.251.83:9084/';
    public static GET_REGIONES = 'getRegion';
    public static GET_ONTS_BY_REGION = 'getOltsByRegion/';
    public static GET_ONTS_BY_OLTS_UP_DOWN= 'getOntsByOltsUp/';
    public static GET_ONTS_ALL= 'finOntsByIdAll/';
    public static GET_HISTORICO_CAMBIOS= 'getHistoricoCambios/';
    public static GET_TOTALES_TECNOLOGIA= 'getTotalesByTecnologia/';
    public static GET_TOTALES_ACTIVO= 'getTotalesActivo/';
    public static GET_TOTALES_BY_OLT= 'getTotalesByOlt/';
    public static GET_NAME_OLT= 'getNombreByRegex/';
    public static GET_ALIAS_ONT= 'getAliasByRegex/';
    public static GET_IP_OLT= 'getIpByRegex/';
    public static GET_SERIE_ONT= 'getSerieByRegex/';
    public static GET_REGEX_ACTUALIZACION= 'getRegexActualizacion/';
    public static GET_ONT_DETALLE_ACT= 'getOntDetalleAc/';
    public static FIND_OLT= 'findOlt';
    public static FIND_ONT= 'findOnt';
    public static GET_DETALLE_ACTUALIZACION= 'getDetalleActualizacion/';
    public static GET_DETALLE_ACTUALIZACION_DATA= 'getDetalleActuacionData/'
    public static GET_DETALLE_ACTUALIZACION_SERIE= 'getDetalleActuacionSerie/'
    public static GET_METRICAS= 'getMetrics/';
    public static GET_OLTS= 'getOlts/';
    public static UPDATE_STATUS="actualizaEstatus/";
    public static VALIDA_USUARIO="validaUser/";
    public static LOGIN ="login"
    public static UPDATE_ESTATUS_OLT ="updateStatusOlt/"
    public static DESCUBRIMIENTO_MANUAL ="descubrimientoManual"
    public static POLEO_METRICAS_MANUAL="poleoMetricasManual/"
    public static VALIDA_MAXIMO ="validaMaximoDescunbrimiento"
    public static DETALLE_DES_MANUAL ="getDetalleDescubrimiento"
    public static DETALLES_METRICAS ="configmetric"
    public static CAMBIA_BLOQUE_METRICA="changeMetricBlock/"
    public static DESASIGNA_BLOQUE_METRICA="removeMetricBlock/"
    public static GET_ARCHIVO="getArchivo";
    public static POLEO_METRICA_OID="metrica/poleo"
    public static CAMBIOS="api/v2/limpieza/onts/repetidas/"
    public static ACTUALIZA_OLT_BY_ONT="actualizaOltOnOnt/"
    
   
    
}