package com.nahmens.rhcimax.database.modelo;

public class Checkin {
	
	public final static String ID = "_id";
	public final static String LATITUD = "latitud";
	public final static String LONGITUD = "longitud";
	public final static String CHECKIN = "checkin";
	public final static String CHECKOUT = "checkout";
	public final static String ID_USUARIO = "idUsuario";
	
	
	String id;
	Double latitud;
	Double longitud;
	String checkin;
	String checkout;
	String idUsuario;
	
	//estoy modificando
	public Checkin(String id, Double latitud, Double longitud, String checkin,
			String checkout, String idUsuario) {
		
		this.id = id;
		this.latitud = latitud;
		this.longitud = longitud;
		this.checkin = checkin;
		this.checkout = checkout;
		this.idUsuario = idUsuario;
	}

	//estoy agregando
	public Checkin(Double latitud, Double longitud, String checkin,
			String checkout, String idUsuario) {

		this.latitud = latitud;
		this.longitud = longitud;
		this.checkin = checkin;
		this.checkout = checkout;
		this.idUsuario = idUsuario;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getLatitud() {
		return latitud;
	}
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	public Double getLongitud() {
		return longitud;
	}
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	public String getCheckin() {
		return checkin;
	}
	public void setCheckin(String checkin) {
		this.checkin = checkin;
	}
	public String getCheckout() {
		return checkout;
	}
	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

}
