package com.nahmens.rhcimax.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nahmens.rhcimax.database.DAO.UsuarioDAO;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.modelo.Historico;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.CheckinSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.HistoricoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.RolSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;
import com.nahmens.rhcimax.seguridad.Owasp;

public class SesionUsuario {

	public static boolean iniciarSesion(Context context, Usuario usu) {
		boolean showSettingsAlert = false;

		// Guardamos el geoposicionamiento
		GPSTracker gps = new GPSTracker(context);
		// check if GPS enabled
		if (!gps.canGetLocation()) {
			showSettingsAlert = true;
			gps.showSettingsAlert();
		}

		double latitud = gps.getLatitude();
		double longitud = gps.getLongitude();

		Log.e("Login", "Your Location is - \nLat: " + latitud + "\nLong: "
				+ longitud);

		String fecha = FormatoFecha.darFormatoDateTimeUS(new Date());

		Checkin checkin = new Checkin(latitud, longitud, fecha, null,
				usu.getId());
		CheckinSqliteDao checkinDao = new CheckinSqliteDao();
		String idCheckin = checkinDao.insertarCheckin(context, checkin);

		RolSqliteDao rolDao = new RolSqliteDao();
		JSONArray roles = rolDao.buscarRoles(context, usu.getId());

		UsuarioSqliteDao usuarioDao = new UsuarioSqliteDao();
		JSONArray permisos = usuarioDao.buscarPermisos(context, usu.getId());

		// Limpiamos el archivo de preferencias
		context.getSharedPreferences("Usuario", Context.MODE_PRIVATE).edit()
				.clear().commit();

		// Guardamos en el archivo de preferencias la sesion del usuario
		// de manera que pueda ser accedido desde cualquier parte del codigo
		SharedPreferences prefs = context.getSharedPreferences("Usuario",
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Usuario.ID, usu.getId());
		editor.putString(Usuario.CORREO, usu.getCorreo());
		editor.putString(Usuario.TOKEN, usu.getToken());
		editor.putString("idCheckin", idCheckin);

		for (int i = 0; i < permisos.length(); i++) {

			try {
				JSONObject permisoObject = permisos.getJSONObject(i);
				editor.putString("permiso" + i,
						permisoObject.getString(Permiso.NOMBRE));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < roles.length(); i++) {

			try {
				JSONObject rolObject = roles.getJSONObject(i);
				editor.putString("rol" + i, rolObject.getString(Permiso.NOMBRE));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		editor.commit();

		return showSettingsAlert;

	}

	public static void cerrarSesion(Context contexto) {
		String fecha = FormatoFecha.darFormatoDateTimeUS(new Date());

		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",
				Context.MODE_PRIVATE);
		String idCheckin = prefs.getString("idCheckin", "");

		CheckinSqliteDao checkinDao = new CheckinSqliteDao();
		HistoricoSqliteDao historicoDao = new HistoricoSqliteDao();

		Checkin checkin = checkinDao.buscarCheckin(contexto, idCheckin);
		checkin.setCheckout(fecha);

		checkinDao.modificarCheckin(contexto, checkin);

		Historico historico = historicoDao.buscarHistoricoPorCheckin(contexto,
				idCheckin);

		if (historico != null) {
			historico.setFechaModificacion(fecha);
			historicoDao.modificarHistorico(contexto, historico);
		}

	}

	public static String getIdUsuario(Context contexto) {
		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",
				Context.MODE_PRIVATE);
		return prefs.getString(Usuario.ID, "");
	}

	public static String getIdCheckin(Context contexto) {
		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",
				Context.MODE_PRIVATE);
		return prefs.getString("idCheckin", "");
	}

	public static String getCorreo(Context contexto) {
		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",
				Context.MODE_PRIVATE);
		return prefs.getString(Usuario.CORREO, "");
	}

	public static String getToken(Context contexto) {
		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",
				Context.MODE_PRIVATE);
		return prefs.getString(Usuario.TOKEN, "44dd55ee66ff");
	}

	public static ArrayList<String> getRoles(Context contexto) {
		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",
				Context.MODE_PRIVATE);
		Map<String, ?> todos = prefs.getAll();
		ArrayList<String> roles = new ArrayList<String>();

		for (int i = 0; i < todos.size(); i++) {
			if (todos.containsKey("permiso" + i)) {
				String rol = (String) todos.get("rol" + i);
				roles.add(rol);
			}
		}

		return roles;
	}

	public static ArrayList<String> getPermisos(Context contexto) {
		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",
				Context.MODE_PRIVATE);
		Map<String, ?> todos = prefs.getAll();
		ArrayList<String> permisos = new ArrayList<String>();

		for (int i = 0; i < todos.size(); i++) {
			if (todos.containsKey("permiso" + i)) {
				String permiso = (String) todos.get("permiso" + i);
				permisos.add(permiso);
			}
		}

		return permisos;
	}

}

/*
 * public class Autologout extends IntentService{
 * 
 * public Autologout(String name) { super(name); }
 * 
 * @Override protected void onHandleIntent(Intent intent) { // TODO
 * Auto-generated method stub
 * 
 * }
 * 
 * }
 */
