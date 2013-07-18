package com.nahmens.rhcimax.database.modelo;

public class Cotizacion_Servicio {
	public final static String ID_COTIZACION = "idCotizacion";
	public final static String ID_SERVICIO = "idServicio";
	public final static String MEDIDA = "medida";
	public final static String DESCRIPCION = "descripcion";
	public final static String PRECIO = "precio";
	public final static String INICIAL = "inicial";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	
	String idCotizacion;
	String idServicio;
	double medida;
	double precio;
	double inicial;
	String descripcion;
	
	public Cotizacion_Servicio(String idCotizacion, String idServicio, double medida,
			double precio, double inicial, String descripcion) {

		this.idCotizacion = idCotizacion;
		this.idServicio = idServicio;
		this.medida = medida;
		this.precio = precio;
		this.inicial = inicial;
		this.descripcion = descripcion;
	}

	public String getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(String idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public String getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(String idServicio) {
		this.idServicio = idServicio;
	}

	public double getMedida() {
		return medida;
	}

	public void setMedida(double medida) {
		this.medida = medida;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getInicial() {
		return inicial;
	}

	public void setInicial(double inicial) {
		this.inicial = inicial;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
