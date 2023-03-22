export interface ValidaUser {
    successs: boolean;
    mensaje:  null|string;
}


export interface Actualizacion {
    sms:               string;
    cod:               number;
    totalActualizadas: number;
    totalRecibidas:    number;
    noActualizadas:    NoActualizada[];
    data:              Datum[];
}

export interface Datum {
    frame:             number;
    slot:              number;
    port:              number;
    uid:               null;
    ip:                string;
    estatus:           string;
    descripcionAlarma: null;
    serie:             string;
    usuario:           null;
    motivo:            null;
}

export interface NoActualizada {
    ip:                 string;
    numeroSerie:        string;
    causa:              null;
    frame:              number;
    slot:               number;
    port:               number;
    uid:                null;
    descripcionAlarma:  null;
    fechaActualizacion: Date;
    usuario:            null;
}
