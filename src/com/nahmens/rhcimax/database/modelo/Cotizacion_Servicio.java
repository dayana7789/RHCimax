package com.nahmens.rhcimax.database.modelo;

public class Cotizacion_Servicio {
	public final static String ID_COTIZACION = "idCotizacion";
	public final static String ID_SERVICIO = "idServicio";
	public final static String MEDIDA = "medida";
	public final static String DESCRIPCION = "descripcion";
	
	int idCotizacion;
	int idServicio;
	int medida;
	String descripcion;
	
	
	public Cotizacion_Servicio(int idCotizacion, int idServicio, int medida, String descripcion) {
		this.idCotizacion = idCotizacion;
		this.idServicio = idServicio;
		this.medida = medida;
		this.descripcion = descripcion;
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


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
