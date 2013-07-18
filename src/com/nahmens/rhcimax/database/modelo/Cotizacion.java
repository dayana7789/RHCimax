package com.nahmens.rhcimax.database.modelo;

public class Cotizacion {
	public final static String ID = "_id";
	public final static String FECHA_ENVIO = "fechaEnvio";
	public final static String FECHA_LEIDO = "fechaLeido";
	public final static String ENVIADO = "enviado";
	public final static String RECIBIDO = "recibido";
	public final static String ID_USUARIO = "idUsuario";
	public final static String ID_EMPRESA = "idEmpresa";
	public final static String DESCRIPCION = "descripcion";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String NUM_COTIZACION = "numCotizacion";
	
	String id;
	String fechaEnvio;
	String fechaLeido;
	int enviado;
	int recibido;
	String idUsuario;
	String idEmpresa;
	String descripcion;
	int numCotizacion;
	
	public Cotizacion(String fechaEnvio, String fechaLeido,
			int enviado, int recibido, String idUsuario, String idEmpresa,
			String descripcion) {
		this.fechaEnvio = fechaEnvio;
		this.fechaLeido = fechaLeido;
		this.enviado = enviado;
		this.recibido = recibido;
		this.idUsuario = idUsuario;
		this.idEmpresa = idEmpresa;
		this.descripcion = descripcion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public String getFechaLeido() {
		return fechaLeido;
	}

	public void setFechaLeido(String fechaLeido) {
		this.fechaLeido = fechaLeido;
	}

	public int getEnviado() {
		return enviado;
	}

	public void setEnviado(int enviado) {
		this.enviado = enviado;
	}

	public int getRecibido() {
		return recibido;
	}

	public void setRecibido(int recibido) {
		this.recibido = recibido;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getNumCotizacion() {
		return numCotizacion;
	}

	public void setNumCotizacion(int numCotizacion) {
		this.numCotizacion = numCotizacion;
	}
	
	
	
}
