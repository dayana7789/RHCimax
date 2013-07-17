package com.nahmens.rhcimax.database.modelo;

public class Empleado_Cotizacion {
	
	public final static String ID_COTIZACION = "idCotizacion";
	public final static String ID_EMPLEADO = "idEmpleado";
	
	String idEmpleado;
	String idCotizacion;
	
	public Empleado_Cotizacion(String idEmpleado, String idCotizacion) {
		super();
		this.idEmpleado = idEmpleado;
		this.idCotizacion = idCotizacion;
	}

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(String idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

}
