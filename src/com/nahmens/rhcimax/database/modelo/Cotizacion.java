package com.nahmens.rhcimax.database.modelo;

public class Cotizacion {
	int id;
	String fechaEnvio;
	String fechaLeido;
	int enviado;
	int recibido;
	int idUsuario;
	
	public Cotizacion(String fechaEnvio, String fechaLeido, int enviado,
			int recibido, int idUsuario) {

		this.fechaEnvio = fechaEnvio;
		this.fechaLeido = fechaLeido;
		this.enviado = enviado;
		this.recibido = recibido;
		this.idUsuario = idUsuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
}
