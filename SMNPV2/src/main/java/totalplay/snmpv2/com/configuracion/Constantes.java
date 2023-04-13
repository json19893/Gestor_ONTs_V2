package totalplay.snmpv2.com.configuracion;

public class Constantes {

	/*Constantes de mensajes*/
	public static final String EJECUCION_EXITOSA = "Ejecucion exitosa ";
	public static final String EJECUCION_ERROR = "Ejecucion fallo: ";
	public static final String ERROR_OBT_CONFIGURACION = "Ocurrio un error al obtener la configuración: ";
	public static final String ERROR_EJECUTAR_COMANDO = "Ocurrio un error al ejecutar el comando bash: ";
	public static final String NO_PIN = "No es pineable ";
	public static final String ERROR_LIMPIAR_CADENA = "Ocurrio un error al limpiar la cadena ";
	public static final String SIN_METRICA = "Sin metrica";

	public static final String DESC = "INICIO DE PROCESO DE DESCUBRIMIENTO PARA LA OLT:  ";

	public static final String DESC_FIN = "FIN DE PROCESO DE DESCUBRIMIENTO PARA LA OLT:  ";
	
	/* Constantes de ejecucion de SNMP */
	public static final String SNMP_BULK = "snmpbulkwalk ";
	public static final String SNMP_GET = "snmpget ";
	public static final String SNMP_WALK = "snmpwalk ";
	public static final String OQV = "-Oqv ";
	public static final String IR = "-Ir ";
	public static final String PROTOCOL_ENCR = " -a ";
	public static final String PASSPHRASE = " -A ";
	public static final String LEVEL = " -l ";
	public static final String USER_NAME = " -u ";
	public static final String PROTOCOL_PRIV = " -x ";
	public static final String PROTOCOL_PHRASE = " -X ";
	public static final String RETRIES_COMAD = " -r ";
	public static final Integer RETRIES_VALUE = 1;
	public static final String TIME_OUT_COMAND = " -t ";
	public static final Integer TIME_OUT_VALUE = 30;
	public static final String C = "-c";
	public static final String SPACE = " ";
	


	public static final String comando1 = SNMP_BULK + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE + SPACE
			+ "-v3" + USER_NAME + "userAGPON17" + LEVEL + "authPriv" + PROTOCOL_ENCR + "SHA" + PASSPHRASE
			+ "accesskey372" + PROTOCOL_PRIV + "AES" + PROTOCOL_PHRASE + "securitykey372" + SPACE ;

	public static final String comando2 = SNMP_BULK + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE + SPACE
			+ "-v3" + USER_NAME + "userAGPON17" + LEVEL + "authPriv" + PROTOCOL_ENCR + "SHA" + PASSPHRASE
			+ "accesskey372" + PROTOCOL_PRIV + "DES" + PROTOCOL_PHRASE + "securitykey372" + SPACE;

	public static final String comando3 = SNMP_BULK + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE + SPACE
			+ "-v3" + USER_NAME + "ITSMon03" + LEVEL + "authPriv" + PROTOCOL_ENCR + "SHA" + PASSPHRASE
			+ "au*MGTm0n1t0r%03" + PROTOCOL_PRIV + "AES" + PROTOCOL_PHRASE + "sc#MGTm0n1t0r$30" + SPACE;

	public static final String comando4 = SNMP_BULK + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE + SPACE
			+ "-v3" + USER_NAME + "ITSMon03" + LEVEL + "authPriv" + PROTOCOL_ENCR + "SHA" + PASSPHRASE
			+ "au*MGTm0n1t0r%03" + PROTOCOL_PRIV + "DES" + PROTOCOL_PHRASE + "sc#MGTm0n1t0r$30" + SPACE;

	public static final String comando5 = SNMP_WALK + SPACE+ "-v2c"+SPACE+C+SPACE+"adsl"+SPACE;
	
	public static final String comando6 = SNMP_BULK + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE + SPACE
			+ "-v3" + USER_NAME + "ITSMon03" + LEVEL + "authPriv" + PROTOCOL_ENCR + "SHA" + PASSPHRASE
			+ "au\\*MGTm0n1t0r\\%03" + PROTOCOL_PRIV + "AES" + PROTOCOL_PHRASE + "sc\\#MGTm0n1t0r\\$30" + SPACE;
			
		

	
	public static final String PING = "ping ";
		
	/*------------------------------------------OID------------------------------------------*/
	public static final String OID_NUM_SERIE_ZTE = "1.3.6.1.4.1.3902.1012.3.28.1.1.5";
	public static final String OID_NUM_SERIE_HUAWEI = "hwGponDeviceOntSn";
	public static final String OID_NUM_SERIE_FIBER_HOME = "1.3.6.1.4.1.5875.800.3.10.1.1.10";	
	
	public static final String OID_RUN_STATUS_HUAWEI = "hwGponDeviceOntControlRunStatus";
	public static final String OID_RUN_STATUS_ZTE = "1.3.6.1.4.1.3902.1012.3.28.2.1.3";
	public static final String OID_RUN_STATUS_FIBER_HOME = "1.3.6.1.4.1.5875.800.3.10.1.1.11";
	
	public static final String OID_SYSTNAME="sysName.0";
	
	public static final String OID_LASTDOWNCAUSE_HUAWEI="hwGponDeviceOntControlLastDownCause";
	public static final String OID_LASTDOWNCAUSE_ZTE="1.3.6.1.4.1.3902.1012.3.28.2.1.4";
	
	public static final String OID_LAST_UP_TIME_HUAWEI = "hwGponDeviceOntControlLastUpTime";
	public static final String OID_LAST_UP_TIME_ZTE = "";
	
	public static final String OID_LASTDOWTIME_HUAWEI = "hwGponDeviceOntControlLastDownTime";
	public static final String OID_LASTDOWTIME_ZTE = "";
	
	public static final String OID_UP_BYTES_HUAWEI = "hwGponOntStatisticUpBytes";
	public static final String OID_UP_BYTES_ZTE = "";
	
	public static final String OID_DOWN_BYTES_HUAWEI = "hwGponOntStatisticDownBytes";
	public static final String OID_DOWN_BYTES_ZTE = "";
	
	public static final String OID_TIME_OUT_HUAWEI = "hwGponDeviceOntTimeOut";
	public static final String OID_TIME_OUT_ZTE = "";	
	
	public static final String OID_UP_PACKETS_HUAWEI = "hwGponOntStatisticUpPackts";
	public static final String OID_UP_PACKETS_ZTE = "";
	
	public static final String OID_DOWN_PACKETS_HUAWEI = "hwGponOntStatisticDownPackts";
	public static final String OID_DOWN_PACKETS_ZTE = "";
	
	public static final String OID_DROP_UP_PACKETS_HUAWEI = "hwGponOntStatisticUpDropPackts";
	public static final String OID_DROP_UP_PACKETS_ZTE = "";
	
	public static final String OID_DROP_DOWN_PACKETS_HUAWEI = "hwGponOntStatisticDownDropPackts";
	public static final String OID_DROP_DOWN_PACKETS_ZTE = "";
		
	public static final String OID_CPU_ZTE = "1.3.6.1.4.1.3902.1082.500.20.2.1.25.1.1";
	public static final String OID_CPU_HUAWEI = "1.3.6.1.4.1.2011.6.145.1.1.1.4.1.3";
	
	public static final String OID_MEMORY_HUAWEI = "1.3.6.1.4.1.2011.6.145.1.1.1.4.1.2";
	public static final String OID_MEMORY_ZTE = "1.3.6.1.4.1.3902.1082.500.20.2.1.25.1.2";
	
	public static final String OID_ALIAS_HUAWEI = "hwGponDeviceOntDespt";
	public static final String OID_ALIAS_ZTE = "1.3.6.1.4.1.3902.1012.3.28.1.1.3";
		
	public static final String OID_PROF_NAME_HUAWEI = "hwGponDeviceOntLineProfName";
	public static final String OID_PROF_NAME_ZTE = "";	
	
	public static final String OID_FRAME_SLOT_PORT_HUAWEI = "ifName";
	public static final String OID_FRAME_SLOT_PORT_ZTE =  "";
	public static final String OID_FRAME_SLOT_FIBER_HOME=  "1.3.6.1.4.1.5875.800.3.9.3.3.1.2";
	

	
	public static final String SPLIT_OID="=";
	public static final String PUNTO=".";
	public static final String SPLIT_NUM_SERIE_CADENA_HUAWEI="HUAWEI-XPON-MIB::hwGponDeviceOntSn.";
	public static final String SPLIT_NUM_SERIE_CADENA_ZTE="SNMPv2-SMI::enterprises.3902.1012.3.28.1.1.5.";
	public static final String SPLIT_NUM_SERIE_CADENA_FIBER_HOME="SNMPv2-SMI::enterprises.5875.800.3.10.1.1.10.";
	public static final String SPLIT_NUM_SERIE_VAL2="Hex-STRING:";
	public static final String SPLIT_MEMORY_CADENA="HUAWEI-MIB::huaweiUtility.145.1.1.1.4.1.2.";
	public static final String SPLIT_MEMORY_CADENA_ZTE = "SNMPv2-SMI::enterprises.3902.1082.500.20.2.1.25.1.2.";
	public static final String SPLIT_VALUE_INTEGER="INTEGER:";

	public static final String SPLIT_VALUE_STRING="STRING:";
	public static final String SPLIT_RUN_STATUS_CADENA_HUAWEI="HUAWEI-XPON-MIB::hwGponDeviceOntControlRunStatus.";
	public static final String SPLIT_RUN_STATUS_CADENA_FIBER_HOME="SNMPv2-SMI::enterprises.5875.800.3.10.1.1.11.";
	
	public static final String SPLIT_RUN_STATUS_CADENA_ZTE="SNMPv2-SMI::enterprises.3902.1012.3.28.2.1.3.";
	public static final String SPLIT_LAST_DOWN_CAUSE_CADENA_HUAWEI="HUAWEI-XPON-MIB::hwGponDeviceOntControlLastDownCause.";
	public static final String SPLIT_LAST_DOWN_CAUSE_CADENA_ZTE="SNMPv2-SMI::enterprises.3902.1012.3.28.2.1.4.";
	public static final String SPLIT_UP_PAKETS_CADENA="HUAWEI-XPON-MIB::hwGponOntStatisticUpPackts.";
	public static final String SPLIT_DOWN_PAKETS_CADENA="HUAWEI-XPON-MIB::hwGponOntStatisticDownPackts.";
	public static final String SPLIT_DROP_UP_PAKETS_CADENA="HUAWEI-XPON-MIB::hwGponOntStatisticUpDropPackts.";
	public static final String SPLIT_DROP_DOW_PAKETS_CADENA="HUAWEI-XPON-MIB::hwGponOntStatisticDownDropPackts.";
	public static final String SPLIT_CPU_CADENA="HUAWEI-MIB::huaweiUtility.145.1.1.1.4.1.3.";
	public static final String SPLIT_CPU_CADENA_ZTE="SNMPv2-SMI::enterprises.3902.1082.500.20.2.1.25.1.1.";
	public static final String SPLIT_VALUE_COUNTER64="Counter64:";

	public static final String SPLIT_LAST_UP_TIME="HUAWEI-XPON-MIB::hwGponDeviceOntControlLastUpTime.";
	public static final String SPLIT_LAST_DOWN_TIME="HUAWEI-XPON-MIB::hwGponDeviceOntControlLastDownTime.";
	public static final String SPLIT_UP_BYTES="HUAWEI-XPON-MIB::hwGponOntStatisticUpBytes.";
	public static final String SPLIT_DOWN_BYTES="HUAWEI-XPON-MIB::hwGponOntStatisticDownBytes.";
	public static final String SPLIT_TIME_OUT="HUAWEI-XPON-MIB::hwGponDeviceOntTimeOut.";
	public static final String SPLIT_ALIAS_ONT_HUAWEI="HUAWEI-XPON-MIB::hwGponDeviceOntDespt.";
	public static final String SPLIT_ALIAS_ONT_ZTE="SNMPv2-SMI::enterprises.3902.1012.3.28.1.1.3.";
	public static final String SPLIT_PROF_NAME_ONT="HUAWEI-XPON-MIB::hwGponDeviceOntLineProfName.";
	public static final String SPLIT_FRAME_SLOT_PORT="IF-MIB::ifName.";
	public static final String SPLIT_FRAME_SLOT_PORT_FIBER_HOME = "SNMPv2-SMI::enterprises.5875.800.3.9.3.3.1.2.";
	
	
	/* Constantes de estatus de ejecución */
	public static final Integer INICIO=1;
	public static final Integer CURSO=2;
	public static final Integer TERMINO_EXITOSO=3;
	public static final Integer TERMINO_ERRO=4;
	
	public static final Integer FALLIDO = 0;
	public static final Integer EXITOSO = 1;
	public static final Integer ZTE = 2;
	
	
	public static final String EJECUCION_ONT = "ONTs";
	public static final String EJECUCION_RUN_STATUS = "RUN STATUS";
	public static final String EJECUCION_LAST_DOWN_CASE = "LAST DOWN CASE";
	public static final String EJECUCION_LAST_UP_TIME = "LAST UP TIME";
	public static final String EJECUCION_LAST_DOWN_TIME = "LAST DOWN TIME";
	public static final String EJECUCION_UP_BYTES = "UP BYTES";
	public static final String EJECUCION_DOWN_BYTES = "DOWN BYTES";
	public static final String EJECUCION_TIME_OUT = "TIMEOUT";
	public static final String EJECUCION_UP_PACKETS = "UP PACKETS";
	public static final String EJECUCION_DOWN_PACKETS = "DOWN PACKETS";
	public static final String EJECUCION_DROP_UP_PACKETS = "DROP UP PACKETS";
	public static final String EJECUCION_DROP_DOWN_PACKETS = "DROP_DOWN PACKETS";
	public static final String EJECUCION_CPU = "CPU";
	public static final String EJECUCION_MEMORY = "MEMORY";
	public static final String EJECUCION_ALIAS_ONT = "ALIAS ONT";
	public static final String EJECUCION_PROF_NAME_ONT = "PROF NAME ONT";
	public static final String EJECUCION_FRAME_SLOT_PORT = "FRAME/SLOT/PORT";
	
	
	
	public static final String INICIO_DESC = "Se ha iniciado la ejecucion del proceso de ";
	public static final String EN_PROCESO_DESC = "Se encuentra en proceso de ";
	public static final String PROCESO = "El proceso de ";
	public static final String POLEO = "poleo de ";
	public static final String DESCUBRIMIENTO = "descubrimiento de ";		
	public static final String DESCUBRIMIENTO_MANUAL = "descubrimiento manual de ";	
	public static final String FINAL_EXITO = " termino de manera correcta";
	public static final String FINAL_ERROR = " termino de manera incorrecta";
	public static final String NO_PROCESO = "Esta OLT no pudo ser procesada en el descubrimiento nocturno ";
	
	public static final String LIMPIEZA = "limpieza.";
	public static final String DIFERENCIAS = "identificación de diferencias.";
	public static final String ESTATUS = "cambio de estatus.";
	public static final String VOLCADO = "volcado de inventario a inventario_1.";
	public static final String ASIGNACION = "asignaciòn de métricas en invetario_aux.";
	public static final String BORRADO = "borrado de tablas temporales.";
	public static final String IDENTIFICACION = "identificacion de diferencias y volcado a inventario final.";
	

	public static final String INICIO_PUT_ESTATUS = "Inicio del proceso de cambio de estatus";
	public static final String FIN_PUT_ESTATUS = "Finalizo del proceso de cambio de estatus correctamente";
	
	/*Constantes para descubrimiento manual*/
	
	public static final String INICIO_PROCESO_MANUAL = "Esta en ejecución el descubrimiento";
	public static final String FIN_ERROR_PROCESO_MANUAL = "Ocurrio un error al procesar";
	public static final String FIN_EXITO_PROCESO_MANUAL = "Termino de manera correcta";
	public static final String LIMITE_PROCESO = "Ya alcanzaste el limite de descubrimientos diaria";
	public static final String PROCESANDO_LIMPIEZA = "En estos momento se encuetra corriendo el proceso de limpieza,favor de esperar a que termine ";
	public static final String PROCESANDO= "En estos momento se encuetra corriendo el proceso de descubrimiento,favor de esperar a que termine ";
	public static final String PROCESO_BLOQUEADO= "Por el momento no se puede ejecutar el proceso manual, hasta que termine el descubrimiento nocturno  ";
	public static final String DES_MANUAL= "DESCUBRIMIENTO MANUAL";
	public static final String DESC_EVENTO_MANUAL= "Se ejecuto el proceso manual de las siguientes OLTs: ";
	
	/*Constantes para metricas*/
	
	
	
	
	/*Constantes de identificación de estatus de Lat_Down_Cause*/
	public static final String CAUSE_1="signal loss";
	public static final String CAUSE_2="ONUi signal loss";
	public static final String CAUSE_3="ONUi frame loss";
	public static final String CAUSE_4="ONUi signal failure";
	public static final String CAUSE_5="ONUi acknowledgement loss";
	public static final String CAUSE_6="ONUi physical-layer maintenance loss";
	public static final String CAUSE_7="failure to deactivate the ONT";
	public static final String CAUSE_8="ONT deactivated";
	public static final String CAUSE_9="restart ONT";
	public static final String CAUSE_10="re-register ONT";
	public static final String CAUSE_11="pop-up failure";
	public static final String CAUSE_12="authentication failure";
	public static final String CAUSE_13="DyingGasp";
	public static final String CAUSE_14="reserved";
	public static final String CAUSE_15="ONUi synchronization loss";
	public static final String CAUSE_16="query failure";
	public static final String CAUSE_18="ring check deactivated";
	public static final String CAUSE_30="ONT turnoff optical";
	public static final String CAUSE_31="reboot with command";
	public static final String CAUSE_32="reset with ONT reset-key";
	public static final String CAUSE_33="reset by ONT software";
	public static final String CAUSE_34="broadcast attack deactivated";
	public static final String CAUSE_35="operator check failure";
	public static final String CAUSE_37="rogue ONT self-detect";
	public static final String CAUSE_38="rogue ONT auto-isolate reset";
	public static final String CAUSE_39="rogue ONT auto-isolate deactivated";
	public static final String CAUSE_40="ONT not in the whitelist";
	public static final String CAUSE_DEFAULT="not cause found";

	/*Constantes: identificador de metricas mediante un id*/
	public static final int  SERIAL_NUMBER = 0;
	public static final int  RUN_STATUS = 1;
	public static final int  LAST_DOWN_CASE = 2;
	public static final int LAST_UP_TIME = 3;
	public static final int  LAST_DOWN_TIME = 4;
	public static final int  UP_BYTES = 5;
	public static final int  DOWN_BYTES = 6;
	public static final int  TIMEOUT = 7;
	public static final int  UP_PACKETS = 8;
	public static final int  DOWN_PACKETS = 9;
	public static final int  DROP_UP_PACKETS = 10;
	public static final int  DROP_DOWN_PACKETS = 11;
	public static final int  CPU = 12;
	public static final int  MEMORY = 13;
	public static final int  ALIAS_ONT = 14;
	public static final int  PROF_NAME_ONT = 15;
	public static final int  FRAME_SLOT_PORT = 16;

	//Errores genericos:
	public static final int SNMP_COMMAND_ERROR = 1;
	public static final int RESOURCE_NOT_FOUND = 5;
	public static final int METRIC_NOT_SUPPORTED = 6;
	public static final int TECHNOLOGY_NOT_SUPPORTED = 7;
	public static final int RESOURCE_NOT_AVAILABLE = 8;
	public static final String COMANDO = "aux=/home/implementacion/proxy/aux/OLT_CONSULTA_STATUS_ONT_$$.sh &&"
			+ "echo JSONIN=$JSONIN > $aux &&"
			+ "echo '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' | base64 -d -i | bunzip2 -c >> $aux &&"
			+ "chmod 700 $aux &&"
			+ "$aux &&"
			+ "rm $aux &&";
	
	
}
