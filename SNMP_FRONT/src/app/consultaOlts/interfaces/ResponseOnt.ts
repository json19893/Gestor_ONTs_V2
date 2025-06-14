export interface OntResponse { 
    _id:                  string;
    oid:                  null | string;
    uid:                  string;
    valor:                string;
    id_olt:               number;
    id_metrica:           number;
    fecha_poleo:          Date;
    fecha_modificacion:   Date;
    fecha_descubrimiento: Date;
    estatus:              number;
    id_ejecucion:         string;
    id_region:            number;
    frame:                number;
    slot:                 number;
    port:                 number;
    id_puerto:            string;
    numero_serie:         string;
    tecnologia:           null | string;
    index:                string;
    indexFSP:             string;
    descripcion:          string;
    error:                boolean;
    alias:                string;
    tipo:                 string;
    lastDownTime:         string;
    descripcionAlarma:    string;
    actualizacion:        number;
    vip:                  number;
    sa:                   boolean;
    inventario:           boolean;
}