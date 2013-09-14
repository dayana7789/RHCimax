package com.nahmens.rhcimax.database.modelo;

public class Usuario_Rol {
	
	public final static String ID_ROL = "idRol";
	public final static String ID_USUARIO = "idUsuario";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String SINCRONIZADO = "sincronizado";

	
	String idRol;
	String idUsuario;
	
	public Usuario_Rol(String idRol, String idUsuario) {
		this.idRol = idRol;
		this.idUsuario = idUsuario;
	}

	public String getIdRol() {
		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
}
