package com.nahmens.rhcimax.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.nahmens.rhcimax.controlador.LoginActivity;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;

public class SincronizacionAsyncTask extends AsyncTask<String, Float, String> {

	final CharSequence TEXT_ERROR = "Ha ocurrido un error con la sincronización: ";
	final CharSequence TEXT_OK = "Sincronización finalizada";
	final int DURATION = Toast.LENGTH_LONG;
	
	Context contexto;

	public SincronizacionAsyncTask(Context contexto) {
		super();
		this.contexto = contexto;
	}

	public void getUsuarios() throws Exception{

		Sincronizacion sync = new Sincronizacion(contexto);

		JSONArray userArray = sync.getValores("http://190.203.108.202:8080/vasaweb-1.0/getTest");

		Usuario usuario = null;
		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		String id = null;
		String login = null;
		String password = null;
		String correo = null;
		String idRol = null;
		String token = null;
		
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
				
			usuario = new Usuario(Integer.parseInt(id), login,password, correo, Integer.parseInt(idRol), token);
			usuarioDao.insertarUsuario(contexto, usuario, false);
		}

	}
	
	public void postAutenticacion() throws Exception{

		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		int idUsuario = SesionUsuario.getIdUsuario(contexto);
		Usuario usuario =  usuarioDao.buscarUsuario(contexto, ""+idUsuario);

		String input ="{\"token\":\""+usuario.getToken()+"\"}";
		//String input ="data={\"assetId\":9876, \"assetName\":\"dayana1\"}";
		Log.e("input", input);
		Sincronizacion sync = new Sincronizacion(contexto);
		
		JSONObject resp = sync.postValores("http://190.203.108.202:8080/vasaweb-1.0/createTest", input);

	
	}

	public void postEmpresas() throws Exception{

		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Cursor cEmpresas =  empresaDao.listarEmpresasSync(contexto);
		
		String input = new Formato().cursorToJsonString(cEmpresas);
		Log.e("input", input);

		Sincronizacion sync = new Sincronizacion(contexto);
		
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
			this.postAutenticacion();

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

		return "OK";
	}

}


