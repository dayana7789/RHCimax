package com.nahmens.rhcimax.database.modelo;


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
	public final static String ID_USUARIO_CREADOR = "idUsuarioCreador";
	public final static String ID_USUARIO_MODIFICADOR = "idUsuarioModificador";
	public final static String ID_EMPRESA = "idEmpresa";
	public final static String ID_EMPLEADO = "idEmpleado";
	public final static String NOMBRE_EMPRESA = "nombreEmpresa";
	public final static String NOMBRE_EMPLEADO = "nombreEmpleado";
	public final static String APELLIDO_EMPLEADO = "apellidoEmpleado";
	public final static String SINCRONIZADO = "sincronizado";
	public final static String FINALIZADA = "finalizada";

	
	String id;
	String nombre;
	String fecha;
	String hora;
	String descripcion;
	String fechaFinalizacion;
	String fechaCreacion;
	String fechaModificacion;
	String fechaSincronizacion;
	String idUsuarioCreador;
	String idUsuarioModificador;
	String idEmpresa;
	String idEmpleado;
	
	//Usado al modificar
	public Tarea(String id, String nombre, String fecha, String hora,
			String descripcion, String idUsuarioModificador,
			String idEmpresa, String idEmpleado, String fechaFinalizacion, 
			String fechaModificacion) {

		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.descripcion = descripcion;
		this.idUsuarioModificador = idUsuarioModificador;
		this.idEmpresa = idEmpresa;
		this.idEmpleado = idEmpleado;
		this.fechaModificacion = fechaModificacion;
		this.fechaFinalizacion = fechaFinalizacion;
	}
	
	//usado al ingresar uno nuevo
	public Tarea(String nombre, String fecha, String hora,
			String descripcion, String idUsuarioCreador, String idUsuarioModificador,
			String idEmpresa, String idEmpleado, String fechaFinalizacion) {

		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.descripcion = descripcion;
		this.idUsuarioCreador = idUsuarioCreador;
		this.idUsuarioModificador = idUsuarioModificador;
		this.idEmpresa = idEmpresa;
		this.idEmpleado = idEmpleado;
		this.fechaFinalizacion = fechaFinalizacion;
	}
	
	
//utilizado para buscar
	public Tarea(String id, String nombre, String fecha, String hora,
			String descripcion, String fechaFinalizacion, String fechaCreacion,
			String fechaModificacion, String fechaSincronizacion,
			String idUsuarioCreador, String idUsuarioModificador,
			String idEmpresa, String idEmpleado) {

		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.descripcion = descripcion;
		this.fechaFinalizacion = fechaFinalizacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.fechaSincronizacion = fechaSincronizacion;
		this.idUsuarioCreador = idUsuarioCreador;
		this.idUsuarioModificador = idUsuarioModificador;
		this.idEmpresa = idEmpresa;
		this.idEmpleado = idEmpleado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(String fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getFechaSincronizacion() {
		return fechaSincronizacion;
	}

	public void setFechaSincronizacion(String fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
	}

	public String getIdUsuarioCreador() {
		return idUsuarioCreador;
	}

	public void setIdUsuarioCreador(String idUsuarioCreador) {
		this.idUsuarioCreador = idUsuarioCreador;
	}

	public String getIdUsuarioModificador() {
		return idUsuarioModificador;
	}

	public void setIdUsuarioModificador(String idUsuarioModificador) {
		this.idUsuarioModificador = idUsuarioModificador;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

}
