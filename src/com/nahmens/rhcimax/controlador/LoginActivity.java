package com.nahmens.rhcimax.controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	/*
	 * FUNCION TEMPORAL PARA CAMBIAR DE VISTA
	 */
	public void onClickValidar(View v){
		EditText etLogin = (EditText)findViewById(R.id.textEditLogin);
		EditText etPassword = (EditText)findViewById(R.id.textEditPassword); 

		String login = etLogin.getText().toString();
		String password = etPassword.getText().toString();
		
		Usuario usu = new Usuario(1, "daya", "1234", "daya@gmail.com");
		
		UsuarioSqliteDao usuSqlDao = new UsuarioSqliteDao();
		Boolean resu = usuSqlDao.insertarUsuario(this, usu);

		Intent i = new Intent(this, AplicacionActivity.class);
		startActivity(i);
		finish();
	}

}
