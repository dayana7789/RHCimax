package com.nahmens.rhcimax.utils;

import java.io.Serializable;

/**
 * Objeto de la forma (A,B,C).
 */

public class Tripleta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	boolean booleano;
	String medida;
	String descripcion;
	
	public Tripleta(boolean booleano, String medida, String descripcion) {
		this.booleano = booleano;
		this.medida = medida;
		this.descripcion = descripcion;
	}

	public boolean getBooleano() {
		return booleano;
	}

	public void setBooleano(boolean booleano) {
		this.booleano = booleano;
	}


	public String getMedida() {
		return medida;
	}

	public void setMedida(String medida) {
		this.medida = medida;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "Tripleta [booleano=" + booleano + ", medida=" + medida
				+ ", descripcion=" + descripcion + "]";
	}

}
