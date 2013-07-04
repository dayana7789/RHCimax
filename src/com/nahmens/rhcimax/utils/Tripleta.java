package com.nahmens.rhcimax.utils;

import java.io.Serializable;

/**
 * Objeto de la forma (A,B,C,D,E).
 */

public class Tripleta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	boolean booleano;
	String medida;
	String descripcion;
	double precio;
	double inicial;
	
	public Tripleta(boolean booleano, String medida, String descripcion,
			double precio, double inicial) {

		this.booleano = booleano;
		this.medida = medida;
		this.descripcion = descripcion;
		this.precio = precio;
		this.inicial = inicial;
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getInicial() {
		return inicial;
	}

	public void setInicial(double inicial) {
		this.inicial = inicial;
	}

	@Override
	public String toString() {
		return "Tripleta [booleano=" + booleano + ", medida=" + medida
				+ ", descripcion=" + descripcion + ", precio=" + precio
				+ ", inicial=" + inicial + "]";
	}

}
