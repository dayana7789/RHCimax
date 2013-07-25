package com.nahmens.rhcimax.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.nahmens.rhcimax.controlador.ClientesActivity;
import com.nahmens.rhcimax.controlador.HistoricosActivity;
import com.nahmens.rhcimax.controlador.TareasActivity;
import com.nahmens.rhcimax.database.modelo.Configuracion;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.sqliteDAO.ConfiguracionSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.GenericoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.HistoricoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;

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
		this.mLog = new LogFile(contexto, new Utils().getNumeroAleatorio());
	}


	public void getGenerico(String nombreTabla) throws Exception{
Log.e("nombreTabla", "nombre: "+nombreTabla);
		GenericoSqliteDao myDao = new GenericoSqliteDao();
		Cursor myCursor =  myDao.listarGenericoNoSync(contexto, nombreTabla);

		String input = new Utils().cursorToJsonString(myCursor);
		Log.e("input", input);

		//JSONObject resp = sync.postValores(dirServidor+"createTest", input);

		/**********AQUI HAY QUE VERIFICAR LA RESPUESTA PARA PODER HACER ALGO *************/

		//JSONArray userArray = sync.getValores(dirServidor+"getTest");

		JSONArray myJsonArray = new JSONArray(input);
		boolean modificado = false;
		boolean error = false;
		boolean sincronizado = false;

		for (int i = 0; i < myJsonArray.length(); ++i) {
			JSONObject myJsonObject = myJsonArray.getJSONObject(i);

			modificado = myDao.modificarGenerico(contexto, myJsonObject, nombreTabla);

			if(!modificado){
				try{
					myDao.insertarGenerico(contexto, myJsonObject, nombreTabla);
				}catch(android.database.sqlite.SQLiteConstraintException e){
					mLog.appendLog(obtenerTag() + "... " + e.getMessage() + ": " + "La "+nombreTabla+" con id "+ myJsonObject.getString("_id") + " no pudo ser insertado.");
					error = true;
				}
			}else{
				mLog.appendLog(obtenerTag() + "... " + "La "+nombreTabla+" con id "+ "id" +" ya existe.");
			}

			if(!error){
				sincronizado = myDao.sincronizarGenerico(contexto, myJsonObject, nombreTabla);

				if(!sincronizado){
					mLog.appendLog(obtenerTag() + "... " + "La " + nombreTabla + " con id "+ "id" +" no pudo ser sincronizado.");
				}else{
					//Guardamos la fecha de sincronizacion en shared preferences
					new Sincronizacion(contexto).setFechaSincronizacion(contexto, nombreTabla);
				}
			}
		}

	}

	/*************************** FUNCIONES ASYNC TASK ***************************/

	/**
	 * Funcion que se llama antes de iniciar la carga.
	 * Muestra dialog.
	 */
	protected void onPreExecute() {
		//LoginActivity.dialog.show();
	}



	/**
	 * Funcion que se llama al terminar la carga.
	 * Oculta dialog.
	 */
	protected void onPostExecute(String result) {
		//LoginActivity.dialog.dismiss(); 

		if(result.equals("OK")){
			Toast toast = Toast.makeText(contexto, TEXT_OK,  DURATION);
			toast.show();
			mLog.appendLog(obtenerTag() + TEXT_OK);
			
			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			TareaSqliteDao tareaDao = new TareaSqliteDao();
			HistoricoSqliteDao historicoDao =  new HistoricoSqliteDao();
			ArrayList<String> permisos= SesionUsuario.getPermisos(contexto);

			try{
				if(ClientesActivity.listCursorAdapterEmpleados !=null){
					ClientesActivity.listCursorAdapterEmpleados.changeCursor(empleadoDao.buscarEmpleadoFilter(contexto,null));
					ClientesActivity.listCursorAdapterEmpleados.notifyDataSetChanged();
				}
			}catch(Exception e){
				//e.printStackTrace();
			}

			try{
				if(ClientesActivity.listCursorAdapterEmpresas != null){
					ClientesActivity.listCursorAdapterEmpresas.changeCursor(empresaDao.buscarEmpresaFilter(contexto,null));
					ClientesActivity.listCursorAdapterEmpresas.notifyDataSetChanged();
				}
			}catch(Exception e){
				//e.printStackTrace();
			}

			try{

				if(TareasActivity.listCursorAdapterTareas !=null){

					Cursor mCursorTareas = null;
					if(permisos.contains(Permiso.LISTAR_TODO)){
						mCursorTareas = tareaDao.buscarTareaFilter(contexto,null, false);
					}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
						mCursorTareas = tareaDao.buscarTareaFilter(contexto,null, true);
					}else{
						mCursorTareas = tareaDao.buscarTareaFilter(contexto,null, false);
					}

					TareasActivity.listCursorAdapterTareas.changeCursor(mCursorTareas);
					TareasActivity.listCursorAdapterTareas.notifyDataSetChanged();
				}
			}catch(Exception e){
				//e.printStackTrace();
			}

			try{
				if(HistoricosActivity.listCursorAdapterHistoricos != null){
					
					Cursor mCursorHistoricos = null;
					if(permisos.contains(Permiso.LISTAR_TODO)){
						mCursorHistoricos = historicoDao.buscarHistoricoFilter(contexto,null, false);
					}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
						mCursorHistoricos = historicoDao.buscarHistoricoFilter(contexto,null, true);
					}else{
						mCursorHistoricos = historicoDao.buscarHistoricoFilter(contexto,null, false);
					}
					
					HistoricosActivity.listCursorAdapterHistoricos.changeCursor(mCursorHistoricos);
					HistoricosActivity.listCursorAdapterHistoricos.notifyDataSetChanged();
				}
			}catch(Exception e){
				//e.printStackTrace();
			}
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
	protected String doInBackground(String... nombreTablas) {

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
				int count = nombreTablas.length;

				for (int i = 0; i < count; i++) {
					getGenerico(nombreTablas[i]);
				}


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


