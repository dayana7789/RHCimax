package com.nahmens.rhcimax.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.nahmens.rhcimax.controlador.LoginActivity;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.modelo.Configuracion;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Rol;
import com.nahmens.rhcimax.database.modelo.Rol_Permiso;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.ConfiguracionSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.GenericoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.PermisoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.RolSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.Rol_PermisoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;

public class SincronizacionAsyncTask extends AsyncTask<String, Float, String> {

	private final CharSequence TEXT_ERROR = "Ha ocurrido un error con la sincronización: ";
	private final CharSequence TEXT_OK = "Sincronización finalizada";
	private final int DURATION = Toast.LENGTH_LONG;
	private String dirServidor = "http://190.203.108.202:8080/vasaweb-1.0/";

	Context contexto;
	Sincronizacion sync;
	LogFile mLog;

	public SincronizacionAsyncTask(Context contexto) {
		super();
		this.contexto = contexto;
		this.sync = new Sincronizacion(contexto);
		this.mLog = new LogFile(contexto, new Formato().getNumeroAleatorio());
	}

	public void getUsuarios() throws Exception{

		UsuarioSqliteDao usuarioDao2 = new UsuarioSqliteDao();
		Usuario usuario2 =  usuarioDao2.buscarUsuario(contexto, "administrador", "1234");

		String input ="{\"token\":\""+usuario2.getToken()+"\"}";
		//String input ="data={\"assetId\":9876, \"assetName\":\"dayana1\"}";
		Log.e("input", input);

		JSONObject resp = sync.postValores(dirServidor+"createTest", input);
		
		/**********AQUI HAY QUE VERIFICAR LA RESPUESTA PARA PODER HACER ALGO *************/
		
		JSONArray userArray = sync.getValores(dirServidor+"getTest");

	/*	JSONArray userArray = new JSONArray();
		JSONObject jsObject =  new JSONObject();
		jsObject.put("_id", "80");
		jsObject.put("login", "nahmens");
		jsObject.put("password", "1234");
		jsObject.put("correo", "nahmens@nahmens.com");
		jsObject.put("idRol", "100");
		jsObject.put("token", "80");

		userArray.put(jsObject);*/

		Usuario usuario = null;
		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		String id = null;
		String login = null;
		String password = null;
		String correo = null;
		String idRol = null;
		String token = null;
		boolean modificado = false;

		//eliminamos a todos los usuarios
		//usuarioDao.eliminarUsuarios(contexto);

		//y los volvemos a insertar
		for (int i = 0; i < userArray.length(); ++i) {
			JSONObject userObject = userArray.getJSONObject(i);

			id = userObject.getString(Usuario.ID);
			login = userObject.getString(Usuario.LOGIN);
			password = userObject.getString(Usuario.PASSWORD);
			correo = userObject.getString(Usuario.CORREO);
			idRol = userObject.getString(Usuario.ID_ROL);
			token = userObject.getString(Usuario.TOKEN);

			usuario = new Usuario(id, login,password, correo, idRol, token);

			modificado = usuarioDao.modificarUsuario(contexto, usuario);

			if(!modificado){
				mLog.appendLog(obtenerTag() + "... " + "El usuario con id: "+ id + " es nuevo.");
				try{
					usuarioDao.insertarUsuario(contexto, usuario);
				}catch(android.database.sqlite.SQLiteConstraintException e){
					mLog.appendLog(obtenerTag() + "... " + e.getMessage() + ": " + "El usuario con id: "+ id + " no pudo ser insertado.");
				}
			}else{
				mLog.appendLog(obtenerTag() + "... " + "El usuario con id: "+ id + " fue modificado satisfactoriamente.");
			}

		}

	}

	public void getRoles() throws Exception{

		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		Usuario usuario =  usuarioDao.buscarUsuario(contexto, "administrador", "1234");

		String input ="{\"token\":\""+usuario.getToken()+"\"}";
		//String input ="data={\"assetId\":9876, \"assetName\":\"dayana1\"}";
		Log.e("input", input);

		JSONObject resp = sync.postValores(dirServidor+"createTest", input);
		
		/**********AQUI HAY QUE VERIFICAR LA RESPUESTA PARA PODER HACER ALGO *************/
		
		JSONArray userArray = sync.getValores(dirServidor+"getTest");

	/*	JSONArray userArray = new JSONArray();

		JSONObject jsObject =  new JSONObject();
		jsObject.put(Rol.ID, "100");
		jsObject.put("nombre", "nahmens2");
		jsObject.put("descripcion", "1234");

		JSONObject jsObject2 =  new JSONObject();
		jsObject2.put(Rol.ID, "80");
		jsObject2.put("nombre", "nahmens");
		jsObject2.put("descripcion", "1234");

		userArray.put(jsObject);		
		userArray.put(jsObject2);*/

		Rol rol = null;
		RolSqliteDao rolDao = new RolSqliteDao();
		String id = null;
		String nombre = null;
		String descripcion = null;
		boolean modificado = false;

		//eliminamos a todos los usuarios
		//rolDao.eliminarRoles(contexto);

		//y los volvemos a insertar
		for (int i = 0; i < userArray.length(); ++i) {
			JSONObject userObject = userArray.getJSONObject(i);

			id = userObject.getString(Rol.ID);
			nombre = userObject.getString(Rol.NOMBRE);
			descripcion = userObject.getString(Rol.DESCRIPCION);

			rol = new Rol(id, nombre, descripcion);

			modificado = rolDao.modificarRol(contexto, rol);

			if(!modificado){
				mLog.appendLog(obtenerTag() + "... " + "El rol con id: "+ id + " es nuevo.");
				try{
					rolDao.insertarRol(contexto, rol);
				}catch(android.database.sqlite.SQLiteConstraintException e){
					mLog.appendLog(obtenerTag() + "... " + e.getMessage() + ": " + "El rol con id: "+ id + " no pudo ser insertado.");
				}
			}else{
				mLog.appendLog(obtenerTag() + "... " + "El rol con id: "+ id + " fue modificado satisfactoriamente.");
			}
		}
	}

	public void getPermisos() throws Exception{

		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		Usuario usuario =  usuarioDao.buscarUsuario(contexto, "administrador", "1234");

		String input ="{\"token\":\""+usuario.getToken()+"\"}";
		//String input ="data={\"assetId\":9876, \"assetName\":\"dayana1\"}";
		Log.e("input", input);

		JSONObject resp = sync.postValores(dirServidor+"createTest", input);
		
		/**********AQUI HAY QUE VERIFICAR LA RESPUESTA PARA PODER HACER ALGO *************/
		
		JSONArray userArray = sync.getValores(dirServidor+"getTest");

		/*JSONArray userArray = new JSONArray();

		JSONObject jsObject =  new JSONObject();
		jsObject.put(Permiso.ID, "100");
		jsObject.put("nombre", Permiso.LISTAR_TODO);
		jsObject.put("descripcion", "1234");

		JSONObject jsObject2 =  new JSONObject();
		jsObject2.put(Permiso.ID, "80");
		jsObject2.put("nombre", Permiso.MODIFICAR_TODO);
		jsObject2.put("descripcion", "1234");

		userArray.put(jsObject);		
		userArray.put(jsObject2);*/

		Permiso permiso = null;
		PermisoSqliteDao permisoDao = new PermisoSqliteDao();
		String id = null;
		String nombre = null;
		String descripcion = null;
		boolean modificado = false;

		//eliminamos a todos los usuarios
		//permisoDao.eliminarPermisos(contexto);

		//y los volvemos a insertar
		for (int i = 0; i < userArray.length(); ++i) {
			JSONObject userObject = userArray.getJSONObject(i);

			id = userObject.getString(Permiso.ID);
			nombre = userObject.getString(Permiso.NOMBRE);
			descripcion = userObject.getString(Permiso.DESCRIPCION);

			permiso = new Permiso(id, nombre, descripcion);

			modificado = permisoDao.modificarPermiso(contexto, permiso);

			if(!modificado){
				mLog.appendLog(obtenerTag() + "... " + "El permiso con id: "+ id + " es nuevo.");
				try{
					permisoDao.insertarPermiso(contexto, permiso);
				}catch(android.database.sqlite.SQLiteConstraintException e){
					mLog.appendLog(obtenerTag() + "... " +  e.getMessage() + ": " + "El permiso con id: "+ id + " no pudo ser insertado.");
				}
			}else{
				mLog.appendLog(obtenerTag() + "... " + "El permiso con id: "+ id + " fue modificado satisfactoriamente.");
			}
		}
	}

	public void getRol_Permiso() throws Exception{
		
		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		Usuario usuario =  usuarioDao.buscarUsuario(contexto, "administrador", "1234");

		String input ="{\"token\":\""+usuario.getToken()+"\"}";
		//String input ="data={\"assetId\":9876, \"assetName\":\"dayana1\"}";
		Log.e("input", input);

		JSONObject resp = sync.postValores(dirServidor+"createTest", input);
		
		/**********AQUI HAY QUE VERIFICAR LA RESPUESTA PARA PODER HACER ALGO *************/
		
		JSONArray userArray = sync.getValores(dirServidor+"getTest");

		//	JSONArray userArray = sync.getValores(dirServidor+"getTest");

		/*JSONArray userArray = new JSONArray();

		JSONObject jsObject =  new JSONObject();
		jsObject.put(Rol_Permiso.ID_ROL, "100");
		jsObject.put(Rol_Permiso.ID_PERMISO, "100");


		JSONObject jsObject2 =  new JSONObject();
		jsObject2.put(Rol_Permiso.ID_ROL, "100");
		jsObject2.put(Rol_Permiso.ID_PERMISO, "80");

		
		userArray.put(jsObject);		
		//userArray.put(jsObject2);*/

		Rol_Permiso rol_permiso = null;
		Rol_PermisoSqliteDao rol_PermisoDao = new Rol_PermisoSqliteDao();
		String idPermiso = null;
		String idRol = null;

		boolean existe = false;

		//eliminamos a todos los usuarios
		//rol_PermisoDao.eliminarRoles_Permisos(contexto);

		//y los volvemos a insertar
		for (int i = 0; i < userArray.length(); ++i) {
			JSONObject userObject = userArray.getJSONObject(i);

			idPermiso = userObject.getString(Rol_Permiso.ID_PERMISO);
			idRol = userObject.getString(Rol_Permiso.ID_ROL);

			rol_permiso = new Rol_Permiso(idRol,idPermiso);

			existe = rol_PermisoDao.existeRol_Permiso(contexto, rol_permiso);
			
			if(!existe){
				try{
					rol_PermisoDao.insertaRol_Permiso(contexto, rol_permiso);
				}catch(android.database.sqlite.SQLiteConstraintException e){
					mLog.appendLog(obtenerTag() + "... " + e.getMessage() + ": " + "El rol_permiso con idPermiso: "+ idPermiso + " e idRoL: " + idRol +" no pudo ser insertado.");
				}
			}else{
				mLog.appendLog(obtenerTag() + "... " + "El rol_permiso con idPermiso: "+ idPermiso + " e idRoL: " + idRol +" ya existe.");
			}
		}
	}

	/**
	 * Esta funcion se utiliza despues de haber hecho login en el sistema.
	 * @throws Exception
	 */
	public void postAutenticacion() throws Exception{

		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		String idUsuario = SesionUsuario.getIdUsuario(contexto);
		Usuario usuario =  usuarioDao.buscarUsuario(contexto, idUsuario);

		String input ="{\"token\":\""+usuario.getToken()+"\"}";
		//String input ="data={\"assetId\":9876, \"assetName\":\"dayana1\"}";
		Log.e("input", input);

		JSONObject resp = sync.postValores(dirServidor+"createTest", input);


	}
	
	/**
	 * Esta funcion solo se utiliza para sincronizar usuarios
	 * desde la pagina del login
	 * @throws Exception
	 */
	public void postAutenticacionMaster() throws Exception{

		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		Usuario usuario =  usuarioDao.buscarUsuario(contexto, "administrador", "1234");

		String input ="{\"token\":\""+usuario.getToken()+"\"}";
		//String input ="data={\"assetId\":9876, \"assetName\":\"dayana1\"}";
		Log.e("input", input);

		JSONObject resp = sync.postValores(dirServidor+"createTest", input);
	}

	public void getEmpresas() throws Exception{

		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Cursor cEmpresas =  empresaDao.listarEmpresasNoSync(contexto);

		String input = new Formato().cursorToJsonString(cEmpresas);
		Log.e("input", input);
		
		//JSONObject resp = sync.postValores(dirServidor+"createTest", input);
		
		/**********AQUI HAY QUE VERIFICAR LA RESPUESTA PARA PODER HACER ALGO *************/
		
		//JSONArray userArray = sync.getValores(dirServidor+"getTest");

		JSONArray myJsonArray = new JSONArray(input);

		/*JSONArray userArray = new JSONArray();

		JSONObject jsObject =  new JSONObject();
		jsObject.put(Rol_Permiso.ID_ROL, "100");
		jsObject.put(Rol_Permiso.ID_PERMISO, "100");


		JSONObject jsObject2 =  new JSONObject();
		jsObject2.put(Rol_Permiso.ID_ROL, "100");
		jsObject2.put(Rol_Permiso.ID_PERMISO, "80");

		
		userArray.put(jsObject);		
		//userArray.put(jsObject2);*/

		Empresa empresa = null;
		EmpresaSqliteDao myDao = new EmpresaSqliteDao();
		String id;
		String nombre;
		String telefono;
		String rif;
		String web;
		String dirFiscal;
		String dirComercial;
		Double latitud;
		Double longitud;
		String fechaCreacion;
		String idUsuarioCreador;
		String fechaModificacion;
		String idUsuarioModificador;
		String fechaSincronizacion;
		int modificado2;

		boolean modificado = false;

		//eliminamos a todos los usuarios
		//rol_PermisoDao.eliminarRoles_Permisos(contexto);

		//y los volvemos a insertar
		for (int i = 0; i < myJsonArray.length(); ++i) {
			JSONObject myJsonObject = myJsonArray.getJSONObject(i);

			id = myJsonObject.getString(Empresa.ID);
			nombre = myJsonObject.getString(Empresa.NOMBRE);
			telefono = myJsonObject.getString(Empresa.TELEFONO);
			rif = myJsonObject.getString(Empresa.RIF);
			web = myJsonObject.getString(Empresa.WEB);
			dirFiscal = myJsonObject.getString(Empresa.DIR_FISCAL);
			dirComercial = myJsonObject.getString(Empresa.DIR_COMERCIAL);
			latitud = myJsonObject.getDouble(Empresa.LATITUD);
			longitud = myJsonObject.getDouble(Empresa.LONGITUD);
			fechaCreacion = myJsonObject.getString(Empresa.FECHA_CREACION);
			idUsuarioCreador = myJsonObject.getString(Empresa.ID_USUARIO_CREADOR);
			fechaModificacion = myJsonObject.getString(Empresa.FECHA_MODIFICACION);
			idUsuarioModificador = myJsonObject.getString(Empresa.ID_USUARIO_MODIFICADOR);
			fechaSincronizacion = myJsonObject.getString(Empresa.FECHA_SINCRONIZACION);
			modificado2 = myJsonObject.getInt(Empresa.MODIFICADO);

			empresa = new Empresa(id, nombre, telefono, rif, web, dirFiscal, dirComercial, latitud, longitud, fechaCreacion, idUsuarioCreador, fechaModificacion, idUsuarioModificador, fechaSincronizacion, modificado2);

			modificado = myDao.modificarEmpresa(contexto, empresa);
			
			if(!modificado){
				try{
					myDao.insertarEmpresa(contexto, empresa);
				}catch(android.database.sqlite.SQLiteConstraintException e){
					mLog.appendLog(obtenerTag() + "... " + e.getMessage() + ": " + "La empresa con id "+ id + " no pudo ser insertado.");
				}
			}else{
				mLog.appendLog(obtenerTag() + "... " + "La empresa con id "+ id +" ya existe.");
			}
		}

	}
	
	
	public void getGenerico(String nombreTabla) throws Exception{

		GenericoSqliteDao myDao = new GenericoSqliteDao();
		Cursor myCursor =  myDao.listarGenericoNoSync(contexto, nombreTabla);

		String input = new Formato().cursorToJsonString(myCursor);
		Log.e("input", input);
		
		//JSONObject resp = sync.postValores(dirServidor+"createTest", input);
		
		/**********AQUI HAY QUE VERIFICAR LA RESPUESTA PARA PODER HACER ALGO *************/
		
		//JSONArray userArray = sync.getValores(dirServidor+"getTest");

		JSONArray myJsonArray = new JSONArray(input);
		boolean modificado = false;

		//eliminamos a todos los usuarios
		//rol_PermisoDao.eliminarRoles_Permisos(contexto);

		//y los volvemos a insertar
		for (int i = 0; i < myJsonArray.length(); ++i) {
			JSONObject myJsonObject = myJsonArray.getJSONObject(i);

			modificado = myDao.modificarGenerico(contexto, myJsonObject, nombreTabla);
			
			if(!modificado){
				try{
					myDao.insertarGenerico(contexto, myJsonObject, DataBaseHelper.TABLA_EMPRESA);
				}catch(android.database.sqlite.SQLiteConstraintException e){
					mLog.appendLog(obtenerTag() + "... " + e.getMessage() + ": " + "La "+nombreTabla+" con id "+ "id" + " no pudo ser insertado.");
				}
			}else{
				mLog.appendLog(obtenerTag() + "... " + "La empresa con id "+ "id" +" ya existe.");
			}
		}

	}
	
	/*************************** FUNCIONES ASYNC TASK ***************************/

	/**
	 * Funcion que se llama antes de iniciar la carga.
	 * Muestra dialog.
	 */
	protected void onPreExecute() {
		LoginActivity.dialog.show();
	}



	/**
	 * Funcion que se llama al terminar la carga.
	 * Oculta dialos.
	 */
	protected void onPostExecute(String result) {
		LoginActivity.dialog.dismiss(); 

		if(result.equals("OK")){
			Toast toast = Toast.makeText(contexto, TEXT_OK,  DURATION);
			toast.show();
			mLog.appendLog(obtenerTag() + TEXT_OK);

		}else{
			Toast toast = Toast.makeText(contexto, TEXT_ERROR + result, DURATION);
			toast.show();
			mLog.appendLog(obtenerTag() + TEXT_ERROR + result);
		}

	}


	/**
	 * Funcion que corre en background que se encarga de establecer
	 * la sincronizacion cuando existe conexion a internet.
	 */
	protected String doInBackground(String... params) {

		String servidorDB = obtenerServidor();
		if((servidorDB.equals("")==false) && servidorDB!=null){
			dirServidor = servidorDB;
		}

		if(hayInternet()){
			mLog.appendLog(obtenerTag() + "Dirección servidor: " +dirServidor);
			
			mLog.appendLog(obtenerTag() + "Enviando credenciales... ");
			try{
				//postAutenticacionMaster();
			}catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}
			
			mLog.appendLog(obtenerTag() + "... Conexión establecida");
			mLog.appendLog(obtenerTag() + "Inicio de sincronización... ");

			try {
				
				getGenerico(DataBaseHelper.TABLA_EMPLEADO);
				//getEmpresas();
				/*this.getRoles();
				this.getPermisos();
				this.getRol_Permiso();
				this.getUsuarios();*/

			} catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}

			return "OK";

		}else{
			mLog.appendLog(obtenerTag() + " No se pudo realizar la sincronización porque no se detectan servicios de red en este momento.. ");
		}

		return "ERROR";

	}
	
	/*************************** FUNCIONES COMPLEMENTARIAS ***************************/

	/**
	 * @return retorna el header para los mensajes de log.
	 */
	private String obtenerTag(){
		return "["+FormatoFecha.obtenerFechaTiempoActualES()+"]: ";
	}

	/**
	 * Funcion que obtiene de la BD la direccion del servidor
	 * @return
	 */
	private String obtenerServidor(){
		ConfiguracionSqliteDao configuracionDao = new ConfiguracionSqliteDao();
		Configuracion config = configuracionDao.buscarPorKey(contexto, Configuracion.NOMBRE_SERVIDOR);
		return config.getValue();
	}

	/**
	 * Funcion que determina si existe alguna conexion a internet para 
	 * poder hacer la sincronizacion. Escribe el tipo de conexion en 
	 * el archivo de log.
	 * @return si existe conexion o no
	 */
	private boolean hayInternet() {
		ConnectivityManager connMgr = (ConnectivityManager) 
				contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		NetworkInfo networkInfoWifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		boolean isWifiConn = networkInfoWifi.isConnected();
		NetworkInfo networkInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = networkInfoMobile.isConnected();

		mLog.appendLog(obtenerTag() + "Status de red... ");
		mLog.appendLog(obtenerTag() + "... Wifi conectado: " + isWifiConn);
		mLog.appendLog(obtenerTag() + "... Red de datos conectado: " + isMobileConn);

		return (networkInfo != null && networkInfo.isConnected());
	}


}


