package com.nahmens.rhcimax.database.modelo;

public class Usuario {
	
	public final static String ID = "_id";
	public final static String LOGIN = "login";
	public final static String NOMBRE = "nombre";
	public final static String APELLIDO = "apellido";
	public final static String PASSWORD = "password";
	public final static String SALT = "salt";
	public final static String CORREO = "correo";
	public final static String ROL = "rol";
	public final static String TOKEN = "token";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String SINCRONIZADO = "sincronizado";

	
	String id;
	String login;
	String nombre;
	String apellido;
	String password;
	String salt;
	String correo;
	String token;

	public Usuario(String id, String login, String password, String salt, String correo, String token) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.salt=salt;
		this.correo = correo;
		this.token = token;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSalt() {
		return salt;
	}


	public void setSalt(String salt) {
		this.salt = salt;
	}


	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}

	
}
