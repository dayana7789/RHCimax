package com.nahmens.rhcimax.database.modelo;

public class Empresa {
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String TELEFONO = "telefono";
	
	int id;
	String nombre;
	String telefono;
	String rif;
	String web;
	String dirFiscal;
	String dirComercial;
	
	public Empresa(String nombre, String telefono, String rif, String web,
			String dirFiscal, String dirComercial) {
		this.nombre = nombre;
		this.telefono = telefono;
		this.rif = rif;
		this.web = web;
		this.dirFiscal = dirFiscal;
		this.dirComercial = dirComercial;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getRif() {
		return rif;
	}

	public void setRif(String rif) {
		this.rif = rif;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getDirFiscal() {
		return dirFiscal;
	}

	public void setDirFiscal(String dirFiscal) {
		this.dirFiscal = dirFiscal;
	}

	public String getDirComercial() {
		return dirComercial;
	}

	public void setDirComercial(String dirComercial) {
		this.dirComercial = dirComercial;
	}
	
	
}
