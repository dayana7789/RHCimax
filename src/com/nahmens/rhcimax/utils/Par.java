package com.nahmens.rhcimax.utils;

import java.io.Serializable;

/**
 * Objeto de la forma (A,B). Donde A es un numero y B es un boolean.
 */

public class Par implements Serializable {

	private static final long serialVersionUID = 1L;
	
	boolean booleano;
	String medida;
	
	public Par(boolean booleano, String medida) {
		this.booleano = booleano;
		this.medida = medida;
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

	@Override
	public String toString() {
		return "Tripleta [booleano=" + booleano + ", medida=" + medida + "]";
	}


}
