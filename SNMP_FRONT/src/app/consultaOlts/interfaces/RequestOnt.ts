//Interface para mapear la respuesta del servidor
export interface RequestOnt {
    idOlt: number,
    ipOlt: string,
    fechaIni: string | null | undefined,
    fechaFin: string | null | undefined
}