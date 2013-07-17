package com.nahmens.rhcimax.database.modelo;

public class Empleado {
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String APELLIDO = "apellido";
	public final static String EMPRESA = "nombreEmpresa";
	public final static String EMPRESA_ID = "idEmpresa";
	public final static String POSICION = "posicion";
	public final static String EMAIL = "email";
	public final static String TELF_OFICINA = "telfOficina"; 
	public final static String CELULAR = "celular";
	public final static String PIN = "pin";
	public final static String LINKEDIN = "linkedin";
	public final static String DESCRIPCION = "descripcion";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String ID_USUARIO_CREADOR = "idUsuarioCreador";
	public final static String ID_USUARIO_MODIFICADOR = "idUsuarioModificador";
	
	String id;
	String nombre;
	String apellido;
	String posicion;
	String email;
	String telfOficina;
	String celular;
	String pin;
	String linkedin;
	String descripcion;
	String idEmpresa;
	String fechaCreacion;
	String fechaModificacion;
	String fechaSincronizacion;
	String idUsuarioCreador;
	String idUsuarioModificador;

	//Se utiliza cuando estoy agregando
	public Empleado(String nombre, String apellido, String posicion,
			String email, String telfOficina, String celular, String pin,
			String linkedin, String descripcion, String idEmpresa,
			String idUsuarioCreador, String idUsuarioModificador) {

		this.nombre = nombre;
		this.apellido = apellido;
		this.posicion = posicion;
		this.email = email;
		this.telfOficina = telfOficina;
		this.celular = celular;
		this.pin = pin;
		this.linkedin = linkedin;
		this.descripcion = descripcion;
		this.idEmpresa = idEmpresa;
//		this.fechaCreacion = fechaCreacion;
//		this.fechaModificacion = fechaModificacion;
//		this.fechaSincronizacion = fechaSincronizacion;
		this.idUsuarioCreador = idUsuarioCreador;
		this.idUsuarioModificador = idUsuarioModificador;
	}
	
	
	//Se utiliza cuando estoy modificando
		public Empleado(String id, String nombre, String apellido, String posicion,
				String email, String telfOficina, String celular, String pin,
				String linkedin, String descripcion, String idEmpresa, 
				String fechaModificacion, String idUsuarioModificador) {

			this.id = id;
			this.nombre = nombre;
			this.apellido = apellido;
			this.posicion = posicion;
			this.email = email;
			this.telfOficina = telfOficina;
			this.celular = celular;
			this.pin = pin;
			this.linkedin = linkedin;
			this.descripcion = descripcion;
			this.idEmpresa = idEmpresa;
//			this.fechaCreacion = fechaCreacion;
			this.fechaModificacion = fechaModificacion;
//			this.fechaSincronizacion = fechaSincronizacion;
//			this.idUsuarioCreador = idUsuarioCreador;
			this.idUsuarioModificador = idUsuarioModificador;
		}

		
	//se utiliza para buscar
	public Empleado(String id, String nombre, String apellido, String posicion,
			String email, String telfOficina, String celular, String pin,
			String linkedin, String descripcion, String idEmpresa,
			String fechaCreacion, String fechaModificacion,
			String fechaSincronizacion, String idUsuarioCreador,
			String idUsuarioModificador) {

		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.posicion = posicion;
		this.email = email;
		this.telfOficina = telfOficina;
		this.celular = celular;
		this.pin = pin;
		this.linkedin = linkedin;
		this.descripcion = descripcion;
		this.idEmpresa = idEmpresa;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.fechaSincronizacion = fechaSincronizacion;
		this.idUsuarioCreador = idUsuarioCreador;
		this.idUsuarioModificador = idUsuarioModificador;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelfOficina() {
		return telfOficina;
	}

	public void setTelfOficina(String telfOficina) {
		this.telfOficina = telfOficina;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getFechaSincronizacion() {
		return fechaSincronizacion;
	}

	public void setFechaSincronizacion(String fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
	}

	public String getIdUsuarioCreador() {
		return idUsuarioCreador;
	}

	public void setIdUsuarioCreador(String idUsuarioCreador) {
		this.idUsuarioCreador = idUsuarioCreador;
	}

	public String getIdUsuarioModificador() {
		return idUsuarioModificador;
	}

	public void setIdUsuarioModificador(String idUsuarioModificador) {
		this.idUsuarioModificador = idUsuarioModificador;
	}
	
	
	
	

}
