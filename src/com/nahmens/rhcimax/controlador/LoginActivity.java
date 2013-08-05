package com.nahmens.rhcimax.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;
import com.nahmens.rhcimax.utils.SesionUsuario;
import com.nahmens.rhcimax.utils.SincronizacionAsyncTask;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.login, menu); return true; }
	 */

	/*
	 * Funcion que verifica si el usuario es válido o no.
	 */
	public void onClickValidar(View v) {
		EditText etLogin = (EditText) findViewById(R.id.editTextLogin);
		EditText etPassword = (EditText) findViewById(R.id.editTextPassword);

		String login = etLogin.getText().toString();
		String password = etPassword.getText().toString();

		UsuarioSqliteDao usuSqlDao = new UsuarioSqliteDao();

		Usuario usu = usuSqlDao.buscarUsuario(this, login, password);

		if (usu != null) {

			boolean showSettingsAlert = SesionUsuario.iniciarSesion(this, usu);

			if(!showSettingsAlert){
				final Intent inte = new Intent(this, AplicacionActivity.class);
				startActivity(inte);
				finish();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Error: login o password inválido", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void onClickSincronizar(View v) {

		AplicacionActivity.dialog = new ProgressDialog(this);
		AplicacionActivity.onClickSincronizar();

		new SincronizacionAsyncTask(this).execute(
				DataBaseHelper.TABLA_ROL, DataBaseHelper.TABLA_USUARIO,
				DataBaseHelper.TABLA_PERMISO, DataBaseHelper.TABLA_PERMISO,
				DataBaseHelper.TABLA_ROL_PERMISO, DataBaseHelper.TABLA_ROL_PERMISO,
				DataBaseHelper.TABLA_SERVICIO);
	}
}
