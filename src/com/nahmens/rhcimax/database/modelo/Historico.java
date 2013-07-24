package com.nahmens.rhcimax.database.modelo;

public class Historico {
	
	public final static String ID = "_id";
	public final static String TIPO_REGISTRO = "tipoRegistro";
	public final static String ID_COTIZACION = "idCotizacion";
	public final static String ID_TAREA = "idTarea";
	public final static String ID_EMPRESA = "idEmpresa";
	public final static String ID_CHECKIN = "idCheckin";
	public final static String ID_USUARIO_CREADOR = "idUsuarioCreador";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String SINCRONIZADO = "sincronizado";



	String id;
	String tipoRegistro;
	String idCotizacion;
	String idTarea;
	String idEmpresa;
	String idCheckin;
	String idUsuarioCreador;
	String fechaCreacion;
	String fechaSincronizacion;
	

	public Historico(String tipoRegistro, String idCotizacion, String idTarea, String idEmpresa, String idCheckin, String idUsuarioCreador) {
		this.tipoRegistro = tipoRegistro;
		this.idCotizacion = idCotizacion;
		this.idTarea = idTarea;
		this.idEmpresa = idEmpresa;
		this.idCheckin = idCheckin;
		this.idUsuarioCreador = idUsuarioCreador;
	}
	
	public Historico(String tipoRegistro, String idCotizacion, String idTarea, String idEmpresa, String idUsuarioCreador) {
		this.tipoRegistro = tipoRegistro;
		this.idCotizacion = idCotizacion;
		this.idTarea = idTarea;
		this.idEmpresa = idEmpresa;
		this.idUsuarioCreador = idUsuarioCreador;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public String getIdCotizacion() {
		return idCotizacion;
	}
	
	public void setIdCotizacion(String idCotizacion) {
		this.idCotizacion = idCotizacion;
	}
	
	public String getIdTarea() {
		return idTarea;
	}
	
	public void setIdTarea(String idTarea) {
		this.idTarea = idTarea;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getFechaSincronizacion() {
		return fechaSincronizacion;
	}

	public void setFechaSincronizacion(String fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
	}

	public String getIdCheckin() {
		return idCheckin;
	}

	public void setIdCheckin(String idCheckin) {
		this.idCheckin = idCheckin;
	}

	public String getIdUsuarioCreador() {
		return idUsuarioCreador;
	}

	public void setIdUsuarioCreador(String idUsuarioCreador) {
		this.idUsuarioCreador = idUsuarioCreador;
	}



}
