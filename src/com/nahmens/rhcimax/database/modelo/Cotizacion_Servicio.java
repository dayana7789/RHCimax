package com.nahmens.rhcimax.database.modelo;

public class Cotizacion_Servicio {
	int idCotizacion;
	int idServicio;
	
	
	public Cotizacion_Servicio(int idCotizacion, int idServicio) {
		this.idCotizacion = idCotizacion;
		this.idServicio = idServicio;
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

}
