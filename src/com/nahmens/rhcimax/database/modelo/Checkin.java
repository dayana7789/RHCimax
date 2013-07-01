package com.nahmens.rhcimax.database.modelo;

public class Checkin {
	
	public final static String ID = "_id";
	public final static String LATITUD = "latitud";
	public final static String LONGITUD = "longitud";
	public final static String CHECKIN = "checkin";
	public final static String CHECKOUT = "checkout";
	public final static String ID_EMPRESA = "idEmpresa";
	public final static String ID_USUARIO = "idUsuario";
	
	
	int id;
	Double latitud;
	Double longitud;
	String checkin;
	String checkout;
	int idEmpresa;
	int idUsuario;
	
	//estoy modificando
	public Checkin(int id, Double latitud, Double longitud, String checkin,
			String checkout, int idEmpresa, int idUsuario) {
		
		this.id = id;
		this.latitud = latitud;
		this.longitud = longitud;
		this.checkin = checkin;
		this.checkout = checkout;
		this.idEmpresa = idEmpresa;
		this.idUsuario = idUsuario;
	}

	//estoy agregando
	public Checkin(Double latitud, Double longitud, String checkin,
			String checkout, int idEmpresa, int idUsuario) {

		this.latitud = latitud;
		this.longitud = longitud;
		this.checkin = checkin;
		this.checkout = checkout;
		this.idEmpresa = idEmpresa;
		this.idUsuario = idUsuario;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

}