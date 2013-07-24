package com.nahmens.rhcimax.database.modelo;

public class Servicio {
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String PRECIO = "precio";
	public final static String DESCRIPCION = "descripcion";
	public final static String STATUS = "status";
	public final static String UNIDAD_MEDICION = "unidadMedicion";
	public final static String INICIAL = "inicial";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String SINCRONIZADO = "sincronizado";

	
	String id;
	String nombre;
	double precio;
	String descripcion;
	String status;
	String unidadMedicion;
	double inicial;
	
	
	public Servicio(String id, String nombre, double precio, String descripcion,
			String status, String unidadMedicion, double inicial) {

		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.descripcion = descripcion;
		this.status = status;
		this.unidadMedicion = unidadMedicion;
		this.inicial = inicial;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getUnidadMedicion() {
		return unidadMedicion;
	}


	public void setUnidadMedicion(String unidadMedicion) {
		this.unidadMedicion = unidadMedicion;
	}


	public double getInicial() {
		return inicial;
	}


	public void setInicial(double inicial) {
		this.inicial = inicial;
	}
	
	
	
	
	

}
