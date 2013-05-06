package com.nahmens.rhcimax.database.modelo;

public class Rol_Permiso {
	int idRol;
	int idPermiso;
	
	public Rol_Permiso(int idRol, int idPermiso) {
		this.idRol = idRol;
		this.idPermiso = idPermiso;
	}

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public int getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(int idPermiso) {
		this.idPermiso = idPermiso;
	}
	
}
