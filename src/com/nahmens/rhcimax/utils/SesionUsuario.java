package com.nahmens.rhcimax.utils;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.CheckinSqliteDao;

public class SesionUsuario {

	public static void iniciarSesion(Context context, Usuario usu) {
		//Guardamos el geoposicionamiento
		GPSTracker gps = new GPSTracker(context);
		// check if GPS enabled
        if(!gps.canGetLocation()){

            gps.showSettingsAlert();
        }
        
        double latitud = gps.getLatitude();
        double longitud = gps.getLongitude();
        
        Log.e("Login", "Your Location is - \nLat: " + latitud + "\nLong: " +longitud);

		String fecha = FormatoFecha.darFormatoDateTimeUS(new Date());

        Checkin checkin = new Checkin(latitud, longitud, fecha, null, usu.getId());
        CheckinSqliteDao checkinDao = new CheckinSqliteDao();
        int idCheckin = (int) checkinDao.insertarCheckin(context, checkin);
		
		//Guardamos en el archivo de preferencias la sesion del usuario
		//de manera que pueda ser accedido desde cualquier parte del codigo
		SharedPreferences prefs = context.getSharedPreferences("Usuario",Context.MODE_PRIVATE);

		Log.e("preferencias antes",""+prefs.getInt("idCheckin", 0));
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Usuario.ID, usu.getId());
		editor.putString(Usuario.CORREO, usu.getCorreo());
		editor.putInt(Usuario.ID_ROL, usu.getIdRol());
		editor.putInt("idCheckin", idCheckin);
		editor.commit();
		
		
		SharedPreferences prefs2 = context.getSharedPreferences("Usuario",Context.MODE_PRIVATE);
		Log.e("preferencias despues",""+prefs2.getInt("idCheckin", 0));

	}
	
	public static void cerrarSesion(Context contexto) {
		String fecha = FormatoFecha.darFormatoDateTimeUS(new Date());

		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",Context.MODE_PRIVATE);
		int idCheckin = prefs.getInt("idCheckin", 0); 

		CheckinSqliteDao checkinDao = new CheckinSqliteDao();
		Checkin checkin = checkinDao.buscarCheckin(contexto, ""+idCheckin);
		checkin.setCheckout(fecha);

		checkinDao.modificarCheckin(contexto, checkin);

	}
	
	public static int getIdUsuario(Context contexto) {

		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",Context.MODE_PRIVATE);
		return prefs.getInt(Usuario.ID, 0); 
	}

}



/*public class Autologout extends IntentService{

	public Autologout(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	}

}*/
