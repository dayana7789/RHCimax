package com.nahmens.rhcimax.database.modelo;

import java.util.Date;

public class Tarea {
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String FECHA = "fecha";
	public final static String HORA = "hora";
	public final static String DESCRIPCION = "descripcion";
	public final static String FECHA_FINALIZACION = "fechaFinalizacion";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String ID_USUARIO = "idUsuario";
	public final static String ID_EMPRESA = "idEmpresa";
	public final static String ID_EMPLEADO = "idEmpleado";
	public final static String NOMBRE_EMPRESA = "nombreEmpresa";
	public final static String NOMBRE_EMPLEADO = "nombreEmpleado";
	public final static String APELLIDO_EMPLEADO = "apellidoEmpleado";
	
	int id;
	String nombre;
	String fecha;
	String hora;
	String descripcion;
	String fechaFinalizacion;
	String fechaCreacion;
	String fechaSincronizacion;
	String fechaModificacion;
	int idUsuario;
	int idEmpresa;
	int idEmpleado;
	
	//Usado al modificar
	public Tarea(int id, String nombre, String fecha, String hora,
			String descripcion, int idUsuario,
			int idEmpresa, int idEmpleado, String fechaFinalizacion, 
			String fechaModificacion) {

		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.descripcion = descripcion;
		this.idUsuario = idUsuario;
		this.idEmpresa = idEmpresa;
		this.idEmpleado = idEmpleado;
		this.fechaModificacion = fechaModificacion;
		this.fechaFinalizacion = fechaFinalizacion;
	}
	
	//usado al ingresar uno nuevo
	public Tarea(String nombre, String fecha, String hora,
			String descripcion, int idUsuario,
			int idEmpresa, int idEmpleado, String fechaFinalizacion) {

		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.descripcion = descripcion;
		this.idUsuario = idUsuario;
		this.idEmpresa = idEmpresa;
		this.idEmpleado = idEmpleado;
		this.fechaFinalizacion = fechaFinalizacion;
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

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public int getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(String fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}
	
}
