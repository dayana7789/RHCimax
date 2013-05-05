package com.nahmens.rhcimax;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;

/*
 * Contenedor principal de todos los fragmentos
 */

public class AplicacionActivity extends FragmentActivity {

	/* Tab identificadores */
	static String TAB_clientes = "Clientes";
	static String TAB_settings = "Settings";
	static String TAB_logout = "Logout";

	TabHost mTabHost;

	ClientesActivity fragmentClientes;
	SettingsActivity fragmentSettings;
	LogoutActivity fragmentLogout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aplicacion);

		fragmentClientes = new ClientesActivity();
		fragmentSettings = new SettingsActivity();
		fragmentLogout = new LogoutActivity();

		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setOnTabChangedListener(listener);

		//Se debe llamar a setup() antes de agregar los tabs 
		//si se esta usando findViewById().
		mTabHost.setup();

		inicializarTab();
	}   


	/*
	 * Inicializa los tabs y setea los views e identificadores para los tabs.
	 */
	public void inicializarTab() {

		//Obtenemos los recursos
		Resources res = getResources(); 

		//TabHost.TabSpec: A tab has a tab indicator, content, and a tag that is used to keep track of it. 
		TabHost.TabSpec spec =  mTabHost.newTabSpec(TAB_clientes);
		mTabHost.setCurrentTab(-3);

		//TabHost.TabContentFactory: Makes the content of a tab when it is selected. 
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(" Clientes ", res.getDrawable(R.drawable.clientes));
		mTabHost.addTab(spec);


		spec =   mTabHost.newTabSpec(TAB_settings);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(" Settings ",res.getDrawable(R.drawable.settings));
		mTabHost.addTab(spec);

		spec =   mTabHost.newTabSpec(TAB_logout);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(" Logout ",res.getDrawable(R.drawable.logouticon));
		mTabHost.addTab(spec);
	}

	/*
	 * TabChangeListener para cambiar el tab cuando uno de los tabs es presionado
	 */
	TabHost.OnTabChangeListener listener    =   new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			/*Set current tab..*/
			if(tabId.equals(TAB_clientes)){
				pushFragments(tabId, fragmentClientes);
			}else if(tabId.equals(TAB_settings)){
				pushFragments(tabId, fragmentSettings);
			}else if(tabId.equals(TAB_logout)){
				pushFragments(tabId, fragmentLogout);
			}
		}
	};

	/*
	 * agrega el fragment al FrameLayout
	 */
	public void pushFragments(String tag, Fragment fragment){

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();

		ft.replace(android.R.id.tabcontent, fragment);
		ft.commit();
	}
	/*
	 * //Metodo que confirma la salida de la aplicacion.
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() { 

		//Obtenemos el fragment que esta cargado actualmente en el layout
		Fragment fragmentoActual = this.getSupportFragmentManager().findFragmentById(android.R.id.tabcontent);
		Log.e("log_tag ", "a: "+fragmentoActual);

		//Si hacemos click en el back button desde el fragmento de clientes, settings o logout, se nos muestra alert.
		if (fragmentoActual.equals(fragmentClientes) || fragmentoActual.equals(fragmentSettings) || fragmentoActual.equals(fragmentLogout)){
			AlertDialog.Builder builder = new AlertDialog.Builder(AplicacionActivity.this);
			builder.setMessage("Está seguro que desea salir?")
			.setCancelable(false)
			.setPositiveButton("Si", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					AplicacionActivity.this.finish();
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			//sino permitimos que el back button nos lleven al fragmento anterior ejecutado (ej. desde datos_cliente a cliente)
		}else{
			super.onBackPressed();
		}

	}         



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aplicacion, menu);
		return true;
	}

}
