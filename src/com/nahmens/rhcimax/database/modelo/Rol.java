package com.nahmens.rhcimax.database.modelo;

public class Rol {
	
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String DESCRIPCION = "descripcion";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String SINCRONIZADO = "sincronizado";

	
	String id;
	String nombre;
	String descripcion;
	
	
	public Rol(String id, String nombre, String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
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
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



}
