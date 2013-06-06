package com.nahmens.rhcimax.database.modelo;

public class Empresa {
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String TELEFONO = "telefono";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String MODIFICADO = "modificado";
	
	int id;
	String nombre;
	String telefono;
	String rif;
	String web;
	String dirFiscal;
	String dirComercial;
	String fechaCreacion;
	String fechaSincronizacion;
	int modificado;
	int idUsuario;

	public Empresa(int id, String nombre, String telefono, String rif,
			String web, String dirFiscal, String dirComercial, int idUsuario) {

		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.rif = rif;
		this.web = web;
		this.dirFiscal = dirFiscal;
		this.dirComercial = dirComercial;
//		this.fechaCreacion = fechaCreacion;
//		this.fechaSincronizacion = fechaSincronizacion;
		this.idUsuario = idUsuario;
	}
	
	public Empresa(int id, String nombre, String telefono, String rif,
			String web, String dirFiscal, String dirComercial, int idUsuario,
			String fechaSincronizacion) {

		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.rif = rif;
		this.web = web;
		this.dirFiscal = dirFiscal;
		this.dirComercial = dirComercial;
//		this.fechaCreacion = fechaCreacion;
		this.fechaSincronizacion = fechaSincronizacion;
		this.idUsuario = idUsuario;
	}

	

	public Empresa(String nombre, String telefono, String rif, String web,
			String dirFiscal, String dirComercial, int idUsuario) {
		
//		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.rif = rif;
		this.web = web;
		this.dirFiscal = dirFiscal;
		this.dirComercial = dirComercial;
//		this.fechaCreacion = fechaCreacion;
//		this.fechaSincronizacion = fechaSincronizacion;
		this.idUsuario = idUsuario;
	}
	
	public Empresa(String nombre, String telefono, String rif,
			String web, String dirFiscal, String dirComercial, int idUsuario,
			String fechaSincronizacion) {

//		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.rif = rif;
		this.web = web;
		this.dirFiscal = dirFiscal;
		this.dirComercial = dirComercial;
//		this.fechaCreacion = fechaCreacion;
		this.fechaSincronizacion = fechaSincronizacion;
		this.idUsuario = idUsuario;
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
	
	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getFechaSincronizacion() {
		return fechaSincronizacion;
	}

	public void setFechaSincronizacion(String fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
	}
	
	public int getModificado() {
		return modificado;
	}

	public void setModificado(int modificado) {
		this.modificado = modificado;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
}
