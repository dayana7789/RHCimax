package com.nahmens.rhcimax.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.nahmens.rhcimax.controlador.LoginActivity;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Rol;
import com.nahmens.rhcimax.database.modelo.Rol_Permiso;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.PermisoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.RolSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.Rol_PermisoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;

public class SincronizacionAsyncTask extends AsyncTask<String, Float, String> {

	final CharSequence TEXT_ERROR = "Ha ocurrido un error con la sincronización: ";
	final CharSequence TEXT_OK = "Sincronización finalizada";
	final int DURATION = Toast.LENGTH_LONG;
	
	Context contexto;
	Sincronizacion sync;

	public SincronizacionAsyncTask(Context contexto) {
		super();
		this.contexto = contexto;
		this.sync = new Sincronizacion(contexto);
	}

	public void getUsuarios() throws Exception{

	//	JSONArray userArray = sync.getValores("http://190.203.108.202:8080/vasaweb-1.0/getTest");
		
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
		String idFila = null;
		
		//eliminamos a todos los usuarios
		usuarioDao.eliminarUsuarios(contexto);
		
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
			idFila = usuarioDao.insertarUsuario(contexto, usuario);
			
			if(idFila.equals("-1")){
				throw new Exception("El usuario con id: "+ id + " no pudo ser insertado.");
			}
		}

	}
	
	public void getRoles() throws Exception{

	//	JSONArray userArray = sync.getValores("http://190.203.108.202:8080/vasaweb-1.0/getTest");
		
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
		String idFila = null;
		
		//eliminamos a todos los usuarios
		rolDao.eliminarRoles(contexto);
		
		//y los volvemos a insertar
		for (int i = 0; i < userArray.length(); ++i) {
			JSONObject userObject = userArray.getJSONObject(i);
			
			id = userObject.getString(Rol.ID);
			nombre = userObject.getString(Rol.NOMBRE);
			descripcion = userObject.getString(Rol.DESCRIPCION);
				
			rol = new Rol(id, nombre, descripcion);
			idFila = rolDao.insertarRol(contexto, rol);
			
			if(idFila.equals("-1")){
				throw new Exception("El rol con id: "+ id + " no pudo ser insertado.");
			}
		}

	}
	
	public void getPermisos() throws Exception{

	//	JSONArray userArray = sync.getValores("http://190.203.108.202:8080/vasaweb-1.0/getTest");
		
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
		String idFila = null;
		
		//eliminamos a todos los usuarios
		permisoDao.eliminarPermisos(contexto);
		
		//y los volvemos a insertar
		for (int i = 0; i < userArray.length(); ++i) {
			JSONObject userObject = userArray.getJSONObject(i);
			
			id = userObject.getString(Permiso.ID);
			nombre = userObject.getString(Permiso.NOMBRE);
			descripcion = userObject.getString(Permiso.DESCRIPCION);
							
			permiso = new Permiso(id, nombre, descripcion);
			idFila = permisoDao.insertarPermiso(contexto, permiso);
			
			if(idFila.equals("-1")){
				throw new Exception("El permiso con id: "+ id + " no pudo ser insertado.");
			}
		}

	}
	
	public void getRol_Permiso() throws Exception{

		//	JSONArray userArray = sync.getValores("http://190.203.108.202:8080/vasaweb-1.0/getTest");
			
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

			String idFila = null;
			
			//eliminamos a todos los usuarios
			rol_PermisoDao.eliminarRoles_Permisos(contexto);
			
			//y los volvemos a insertar
			for (int i = 0; i < userArray.length(); ++i) {
				JSONObject userObject = userArray.getJSONObject(i);
				
				idPermiso = userObject.getString(Rol_Permiso.ID_PERMISO);
				idRol = userObject.getString(Rol_Permiso.ID_ROL);
								
				rol_permiso = new Rol_Permiso(idRol,idPermiso);
				idFila = rol_PermisoDao.insertaRol_Permiso(contexto, rol_permiso);
				
				if(idFila.equals("-1")){
					throw new Exception("El rol_permiso con idPermiso: "+ idPermiso + " e idRoL: " + idRol +" no pudo ser insertado.");
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
		
		JSONObject resp = sync.postValores("http://190.203.108.202:8080/vasaweb-1.0/createTest", input);

	
	}

	public void postEmpresas() throws Exception{

		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Cursor cEmpresas =  empresaDao.listarEmpresasSync(contexto);
		
		String input = new Formato().cursorToJsonString(cEmpresas);
		Log.e("input", input);
		
		sync.postValores("http://190.203.108.202:8080/vasaweb-1.0/createTest", input);
	
	}

	//Al iniciar la carga
	protected void onPreExecute() {
		LoginActivity.dialog.show();
	}



	//Al terminar la carga
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


	protected String doInBackground(String... params) {

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
	}

}


