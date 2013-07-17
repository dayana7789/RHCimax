package com.nahmens.rhcimax.database.modelo;

public class Usuario {
	
	public final static String ID = "_id";
	public final static String LOGIN = "login";
	public final static String PASSWORD = "password";
	public final static String CORREO = "correo";
	public final static String ID_ROL = "idRol";
	public final static String ROL = "rol";
	public final static String TOKEN = "token";
	
	String id;
	String login;
	String password;
	String correo;
	String idRol;
	String token;

	public Usuario(String id, String login, String password, String correo, String idRol, String token) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.correo = correo;
		this.idRol = idRol;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}


	public String getIdRol() {
		return idRol;
	}


	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}

	
}
