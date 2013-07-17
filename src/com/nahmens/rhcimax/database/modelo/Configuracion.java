package com.nahmens.rhcimax.database.modelo;

public class Configuracion {
	
	public final static String ID = "_id";
	public final static String KEY = "key";
	public final static String VALUE = "value";
	public final static String NOMBRE_SERVIDOR = "nombreServidor";
	
	String id;
	String key;
	String value;

	
	public Configuracion(String id, String key, String value) {
		this.id = id;
		this.key = key;
		this.value = value;
	}
	
	public Configuracion(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	
}
