package com.nahmens.rhcimax.database.modelo;

public class Cotizacion_Servicio {
	public final static String ID_COTIZACION = "idCotizacion";
	public final static String ID_SERVICIO = "idServicio";
	public final static String MEDIDA = "medida";
	public final static String DESCRIPCION = "descripcion";
	public final static String PRECIO = "precio";
	public final static String INICIAL = "inicial";
	
	int idCotizacion;
	int idServicio;
	double medida;
	double precio;
	double inicial;
	String descripcion;
	
	public Cotizacion_Servicio(int idCotizacion, int idServicio, double medida,
			double precio, double inicial, String descripcion) {

		this.idCotizacion = idCotizacion;
		this.idServicio = idServicio;
		this.medida = medida;
		this.precio = precio;
		this.inicial = inicial;
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
