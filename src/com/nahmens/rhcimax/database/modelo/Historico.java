package com.nahmens.rhcimax.database.modelo;

public class Historico {
	
	public final static String ID = "_id";
	public final static String TIPO_REGISTRO = "tipoRegistro";
	public final static String ID_COTIZACION = "idCotizacion";
	public final static String ID_TAREA = "idTarea";
	public final static String ID_EMPRESA = "idEmpresa";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";

	int id;
	String tipoRegistro;
	int idCotizacion;
	int idTarea;
	int idEmpresa;
	String fechaCreacion;
	String fechaSincronizacion;
	

	public Historico(String tipoRegistro, int idCotizacion, int idTarea, int idEmpresa) {
		this.tipoRegistro = tipoRegistro;
		this.idCotizacion = idCotizacion;
		this.idTarea = idTarea;
		this.idEmpresa = idEmpresa;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public int getIdCotizacion() {
		return idCotizacion;
	}
	
	public void setIdCotizacion(int idCotizacion) {
		this.idCotizacion = idCotizacion;
	}
	
	public int getIdTarea() {
		return idTarea;
	}
	
	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getFechaSincronizacion() {
		return fechaSincronizacion;
	}

	public void setFechaSincronizacion(String fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
	}
	
	

}
