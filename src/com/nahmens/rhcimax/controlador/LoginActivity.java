package com.nahmens.rhcimax.controlador;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.CheckinSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;
import com.nahmens.rhcimax.utils.FormatoFecha;
import com.nahmens.rhcimax.utils.GPSTracker;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}*/

	/*
	 * Funcion que verifica si el usuario es v�lido o no.
	 */
	public void onClickValidar(View v){
		EditText etLogin = (EditText)findViewById(R.id.editTextLogin);
		EditText etPassword = (EditText)findViewById(R.id.editTextPassword); 

		String login = etLogin.getText().toString();
		String password = etPassword.getText().toString();

		UsuarioSqliteDao usuSqlDao = new UsuarioSqliteDao();

		Usuario usu = usuSqlDao.buscarUsuario(this, login, password);

		if(usu!=null){

			//Guardamos el geoposicionamiento
			GPSTracker gps = new GPSTracker(LoginActivity.this);
			// check if GPS enabled
	        if(!gps.canGetLocation()){

	            gps.showSettingsAlert();
	        }
	        
	        double latitud = gps.getLatitude();
            double longitud = gps.getLongitude();
            
            Log.e("Login", "Your Location is - \nLat: " + latitud + "\nLong: " +longitud);

    		String fecha = FormatoFecha.darFormatoDateTimeUS(new Date());

            Checkin checkin = new Checkin(latitud, longitud, fecha, null, 0, usu.getId());
            CheckinSqliteDao checkinDao = new CheckinSqliteDao();
            long idCheckin = checkinDao.insertarCheckin(getApplicationContext(), checkin);
			
			//Guardamos en el archivo de preferencias la sesion del usuario
			//de manera que pueda ser accedido desde cualquier parte del codigo
			SharedPreferences prefs = getSharedPreferences("Usuario",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(Usuario.ID, usu.getId());
			editor.putString(Usuario.CORREO, usu.getCorreo());
			editor.putString(Usuario.ID_ROL, ""+usu.getIdRol());
			editor.putString("idCheckin", ""+idCheckin);
			editor.commit();

			final Intent inte = new Intent(this, AplicacionActivity.class);
			startActivity(inte);
			finish();
		}else{
			Toast.makeText(getApplicationContext(), "Error: login o password inv�lido",Toast.LENGTH_LONG).show();
		}
	}
}
