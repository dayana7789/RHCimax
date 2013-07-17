package com.nahmens.rhcimax.database.modelo;

public class Rol {
	
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String DESCRIPCION = "descripcion";
	
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
