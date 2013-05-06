package com.nahmens.rhcimax.database.modelo;

public class Usuario {
	int id;
	String login;
	String password;
	String correo;
	int idRol;

	public Usuario(String login, String password, String correo) {
		this.login = login;
		this.password = password;
		this.correo = correo;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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


	public int getIdRol() {
		return idRol;
	}


	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

}
