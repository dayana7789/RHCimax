package com.nahmens.rhcimax.database.modelo;

public class Rol_Permiso {
	
	public final static String ID_ROL = "idRol";
	public final static String ID_PERMISO = "idPermiso";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String SINCRONIZADO = "sincronizado";

	
	String idRol;
	String idPermiso;
	
	public Rol_Permiso(String idRol, String idPermiso) {
		this.idRol = idRol;
		this.idPermiso = idPermiso;
	}

	public String getIdRol() {
		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(String idPermiso) {
		this.idPermiso = idPermiso;
	}
	
}
