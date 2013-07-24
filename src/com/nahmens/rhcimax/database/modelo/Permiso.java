package com.nahmens.rhcimax.database.modelo;

public class Permiso {
	
	public final static String ID = "_id";
	public final static String NOMBRE = "nombre";
	public final static String DESCRIPCION = "descripcion";
	public final static String FECHA_CREACION = "fechaCreacion";
	public final static String FECHA_MODIFICACION = "fechaModificacion";
	public final static String FECHA_SINCRONIZACION = "fechaSincronizacion";
	public final static String SINCRONIZADO = "sincronizado";

	
	/*Lista de permisos*/
	public final static String LISTAR_TODO = "ListarTodo";
	public final static String LISTAR_PROPIOS = "ListarPropios";
	public final static String MODIFICAR_TODO = "ModificarTodo";
	public final static String MODIFICAR_PROPIOS = "ModificarPropios";
	public final static String ELIMINAR_TODO = "EliminarTodo";
	public final static String ELIMINAR_PROPIOS = "EliminarPropios";
	
	
	String id;
	String nombre;
	String descripcion;
	
	public Permiso(String id, String nombre, String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
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
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}
