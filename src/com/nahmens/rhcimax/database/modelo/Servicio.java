package com.nahmens.rhcimax.database.modelo;

public class Servicio {
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String PRECIO = "precio";
	public final static String DESCRIPCION = "descripcion";
	
	int id;
	String nombre;
	double precio;
	String descripcion;
	
	
	public Servicio(String nombre, double precio, String descripcion) {
		this.nombre = nombre;
		this.precio = precio;
		this.descripcion = descripcion;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
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

}
