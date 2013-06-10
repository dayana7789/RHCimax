package com.nahmens.rhcimax.database.modelo;

public class Cotizacion_Servicio {
	public final static String ID_COTIZACION = "idCotizacion";
	public final static String ID_SERVICIO = "idServicio";
	public final static String MEDIDA = "medida";
	
	int idCotizacion;
	int idServicio;
	int medida;
	
	
	public Cotizacion_Servicio(int idCotizacion, int idServicio, int medida) {
		this.idCotizacion = idCotizacion;
		this.idServicio = idServicio;
		this.medida = medida;
	}


	public int getIdCotizacion() {
		return idCotizacion;
	}


	public void setIdCotizacion(int idCotizacion) {
		this.idCotizacion = idCotizacion;
	}


	public int getIdServicio() {
		return idServicio;
	}


	public void setIdServicio(int idServicio) {
		this.idServicio = idServicio;
	}


	public int getMedida() {
		return medida;
	}


	public void setMedida(int medida) {
		this.medida = medida;
	}
	
}
