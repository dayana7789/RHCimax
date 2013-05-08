package com.nahmens.rhcimax.controlador;

import com.nahmens.rhcimax.R;

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
	
	/* Identificadores de los fragments que se cargan en este activity.
	 * Por cada activity nuevo de tipo Fragment, se debe inicializar el
	 * tag del Fragment aqui.
	 */
	final static String tagFragmentClientes = "fragmentClientes";
	final static String tagFragmentSettings = "fragmentSettings";
	final static String tagFragmentLogout = "fragmentLogout";
	final static String tagFragmentDatosCliente = "fragmentDatosCliente";
	
	/* Varible utilizada para saber, cual layout esta activo.
	 * Se actualiza su valor cada vez que llamo a un fragment 
	 * en particular, es decir, en el onCreateView de cada fragment
	 * (ej. ClientesActivity.java, SettingsActivity.java, etc.)
	 */
	public static String layout_activo;

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

		/*
		 * Cuando cambio la orientacion del tablet, el oncreate
		 * de esta clase se llama. Para evitar perder el layout 
		 * que tengo activo en el momento en que cambie la orientacion
		 * del tablet, verifico el valor de savedInstanceState y si es
		 * distinto de null le indico que cargue el layout correspondiente.
		 */
		if (savedInstanceState != null){
			establecerLayout();
		}

	}   

	public void establecerLayout(){
		Log.e("layout_activo", layout_activo);
		mTabHost.setCurrentTabByTag(layout_activo);
		Fragment fragmentoActual = this.getSupportFragmentManager().findFragmentByTag(layout_activo);
		Log.e("fragmentoActual", ""+this.getSupportFragmentManager().findFragmentById(android.R.id.tabcontent));
		if(fragmentoActual!=null){
			Log.e("hice push", ""+fragmentoActual.getTag());
			pushFragments(layout_activo, fragmentoActual);
		}else{
			Log.e("**ENTRE**", "**ENTRE**");
		}
	}
	
	/*
	 * Inicializa los tabs y setea los views e identificadores para los tabs.
	 */
	public void inicializarTab() {

		//Obtenemos los recursos
		Resources res = getResources(); 

		//TabHost.TabSpec: A tab has a tab indicator, content, and a tag that is used to keep track of it. 
		TabHost.TabSpec spec =  mTabHost.newTabSpec(TAB_clientes);


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
				pushFragments(tagFragmentClientes, fragmentClientes);
			}else if(tabId.equals(TAB_settings)){
				pushFragments(tagFragmentSettings, fragmentSettings);
			}else if(tabId.equals(TAB_logout)){
				pushFragments(tagFragmentLogout, fragmentLogout);
			}
		}
	};

	/*
	 * agrega el fragment al FrameLayout
	 */
	public void pushFragments(String tag, Fragment fragment){

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();

		ft.replace(android.R.id.tabcontent, fragment, tag);
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
