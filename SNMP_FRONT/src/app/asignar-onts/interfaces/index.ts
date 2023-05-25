export interface olts {
    id:               string;
    id_olt:           number;
    nombre:           string;
    ip:               string;
    descripcion:      string;
    tecnologia:       string;
    id_region:        number;
    id_configuracion: number;
    estatus:          number;
    pin:              number;
    descubrio:        boolean;
    total_onts:       number;
    onts:             Ont[];
}

export interface Ont {
    _id:                  string;
    numero_serie:         string;
    oid:                  null;
    fecha_descubrimiento: null;
    fecha_modificacion:   Date;
    id_olt:               number;
    estatus:              null;
    id_ejecucion:         null;
    alias:                null;
    id_region:            null;
    slot:                 number;
    frame:                number;
    port:                 number;
    tipo:                 null;
    uid:                  string;
    descripcionAlarma:    string;
    lastDownTime:         null;
    actualizacion:        null;
    id_puerto:            null;
    tecnologia:           null;
    index:                null;
    indexFSP:             null;
    error:                boolean;
    sa:                   boolean;
}

