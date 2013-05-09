package com.nahmens.rhcimax.database.modelo;

public class Empleado {
	public final static String NOMBRE = "nombre";
	public final static String APELLIDO = "apellido";
	public final static String EMPRESA = "idEmpresa";
	
	int id;
	String nombre;
	String apellido;
	String posicion;
	String email;
	String telfOficina;
	String celular;
	String pin;
	String linkedin;
	String descripcion;
	int idEmpresa;
	
	
	public Empleado(String nombre, String apellido, String posicion,
			String email, String telfOficina, String celular, String pin,
			String linkedin, String descripcion, int idEmpresa) {

		this.nombre = nombre;
		this.apellido = apellido;
		this.posicion = posicion;
		this.email = email;
		this.telfOficina = telfOficina;
		this.celular = celular;
		this.pin = pin;
		this.linkedin = linkedin;
		this.descripcion = descripcion;
		this.idEmpresa = idEmpresa;
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


	public String getApellido() {
		return apellido;
	}


	public void setApellido(String apellido) {
		this.apellido = apellido;
	}


	public String getPosicion() {
		return posicion;
	}


	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getTelfOficina() {
		return telfOficina;
	}


	public void setTelfOficina(String telfOficina) {
		this.telfOficina = telfOficina;
	}


	public String getCelular() {
		return celular;
	}


	public void setCelular(String celular) {
		this.celular = celular;
	}


	public String getPin() {
		return pin;
	}


	public void setPin(String pin) {
		this.pin = pin;
	}


	public String getLinkedin() {
		return linkedin;
	}


	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public int getIdEmpresa() {
		return idEmpresa;
	}


	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	
	

}
