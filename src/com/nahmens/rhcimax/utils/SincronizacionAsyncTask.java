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
import com.nahmens.rhcimax.database.modelo.Configuracion;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Rol;
import com.nahmens.rhcimax.database.modelo.Rol_Permiso;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.ConfiguracionSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
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

		//	JSONArray userArray = sync.getValores(dirServidor+"getTest");

		JSONArray userArray = new JSONArray();
		JSONObject jsObject =  new JSONObject();
		jsObject.put("_id", "80");
		jsObject.put("login", "nahmens");
		jsObject.put("password", "1234");
		jsObject.put("correo", "nahmens@nahmens.com");
		jsObject.put("idRol", "100");
		jsObject.put("token", "80");

		userArray.put(jsObject);

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
			Log.e("modificado: ", " modificado: " + modificado);

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

		//	JSONArray userArray = sync.getValores(dirServidor+"getTest");

		JSONArray userArray = new JSONArray();

		JSONObject jsObject =  new JSONObject();
		jsObject.put(Rol.ID, "100");
		jsObject.put("nombre", "nahmens2");
		jsObject.put("descripcion", "1234");

		JSONObject jsObject2 =  new JSONObject();
		jsObject2.put(Rol.ID, "80");
		jsObject2.put("nombre", "nahmens");
		jsObject2.put("descripcion", "1234");

		userArray.put(jsObject);		
		userArray.put(jsObject2);

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
			Log.e("modificado: ", " modificado: " + modificado);

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

		//	JSONArray userArray = sync.getValores(dirServidor+"getTest");

		JSONArray userArray = new JSONArray();

		JSONObject jsObject =  new JSONObject();
		jsObject.put(Permiso.ID, "100");
		jsObject.put("nombre", Permiso.LISTAR_TODO);
		jsObject.put("descripcion", "1234");

		JSONObject jsObject2 =  new JSONObject();
		jsObject2.put(Permiso.ID, "80");
		jsObject2.put("nombre", Permiso.MODIFICAR_TODO);
		jsObject2.put("descripcion", "1234");


		userArray.put(jsObject);		
		userArray.put(jsObject2);

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
			Log.e("modificado: ", " modificado: " + modificado);

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

		//	JSONArray userArray = sync.getValores(dirServidor+"getTest");

		JSONArray userArray = new JSONArray();

		JSONObject jsObject =  new JSONObject();
		jsObject.put(Rol_Permiso.ID_ROL, "100");
		jsObject.put(Rol_Permiso.ID_PERMISO, "100");


		JSONObject jsObject2 =  new JSONObject();
		jsObject2.put(Rol_Permiso.ID_ROL, "100");
		jsObject2.put(Rol_Permiso.ID_PERMISO, "80");



		userArray.put(jsObject);		
		//userArray.put(jsObject2);

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

			Log.e("existe: ", " existe: " + existe);
			
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

	public void postAutenticacion() throws Exception{

		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		String idUsuario = SesionUsuario.getIdUsuario(contexto);
		Usuario usuario =  usuarioDao.buscarUsuario(contexto, idUsuario);

		String input ="{\"token\":\""+usuario.getToken()+"\"}";
		//String input ="data={\"assetId\":9876, \"assetName\":\"dayana1\"}";
		Log.e("input", input);

		JSONObject resp = sync.postValores(dirServidor+"createTest", input);


	}

	public void postEmpresas() throws Exception{

		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Cursor cEmpresas =  empresaDao.listarEmpresasSync(contexto);

		String input = new Formato().cursorToJsonString(cEmpresas);
		Log.e("input", input);

		sync.postValores("createTest", input);

	}

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
		}else{
			Toast toast = Toast.makeText(contexto, TEXT_ERROR + result, DURATION);
			toast.show();
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
			mLog.appendLog(obtenerTag() + "Inicio de sincronización... ");

			try {
				this.getRoles();
				this.getPermisos();
				this.getRol_Permiso();
				this.getUsuarios();

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

	/**
	 * @return retorna el header para los mensajes de log.
	 */
	private String obtenerTag(){
		return "["+FormatoFecha.obtenerFechaTiempoActual()+"]: ";
	}

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


