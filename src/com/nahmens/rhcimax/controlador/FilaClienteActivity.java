package com.nahmens.rhcimax.controlador;

import com.nahmens.rhcimax.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FilaClienteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fila_cliente);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fila_cliente, menu);
		return true;
	}

}
