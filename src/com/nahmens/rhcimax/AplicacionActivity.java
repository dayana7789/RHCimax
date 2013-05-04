package com.nahmens.rhcimax;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class AplicacionActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aplicacion);
		
		TabHost tabHost = getTabHost(); // Creamos el tabhost de la actividad
        
        TabHost.TabSpec spec; // Creamos un recurso para las propiedades de la pestana
        Intent intent; // Intent que se utilizara para abrir cada pestana
        Resources res = getResources(); //Obtenemos los recursos
		
        intent = new Intent().setClass(this, ClientesActivity.class);
        spec = tabHost.newTabSpec("Clientes").setContent(intent).setIndicator(" Clientes ", res.getDrawable(R.drawable.clientes));
        tabHost.addTab(spec);
        // tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.GREEN);
        
        intent = new Intent().setClass(this, SettingsActivity.class);
        spec = tabHost.newTabSpec("Settings").setContent(intent).setIndicator(" Settings ",res.getDrawable(R.drawable.settings));
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, LogoutActivity.class);
        spec = tabHost.newTabSpec("Logout").setContent(intent).setIndicator(" Logout ",res.getDrawable(R.drawable.logouticon));
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, DatosClienteActivity.class);

       // tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.GREEN);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aplicacion, menu);
		return true;
	}

}
