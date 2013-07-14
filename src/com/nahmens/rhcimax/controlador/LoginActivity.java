package com.nahmens.rhcimax.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;
import com.nahmens.rhcimax.utils.SesionUsuario;
import com.nahmens.rhcimax.utils.Sincronizacion;

public class LoginActivity extends Activity {
	 public static ProgressDialog dialog;

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
	 * Funcion que verifica si el usuario es válido o no.
	 */
	public void onClickValidar(View v){
		EditText etLogin = (EditText)findViewById(R.id.editTextLogin);
		EditText etPassword = (EditText)findViewById(R.id.editTextPassword); 

		String login = etLogin.getText().toString();
		String password = etPassword.getText().toString();

		UsuarioSqliteDao usuSqlDao = new UsuarioSqliteDao();

		Usuario usu = usuSqlDao.buscarUsuario(this, login, password);

		if(usu!=null){

			SesionUsuario.iniciarSesion(getApplicationContext(), usu);

			final Intent inte = new Intent(this, AplicacionActivity.class);
			startActivity(inte);
			finish();
		}else{
			Toast.makeText(getApplicationContext(), "Error: login o password inválido",Toast.LENGTH_LONG).show();
		}
	}

	public void onClickSincronizar(View v){
		dialog = new ProgressDialog(this);
        dialog.setMessage("Descargando...");
        dialog.setTitle("Progreso");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        

		new Sincronizacion(getApplicationContext()).execute();
	}
}
