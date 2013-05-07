package com.nahmens.rhcimax.database.modelo;

public class Empleado_Cotizacion {
	int idEmpleado;
	int idCotizacion;
	
	public Empleado_Cotizacion(int idEmpleado, int idCotizacion) {
		super();
		this.idEmpleado = idEmpleado;
		this.idCotizacion = idCotizacion;
	}

	public int getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public int getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(int idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

}
