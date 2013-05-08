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

	/* Identificadores de los fragments que se cargan en este activity.
	 * Por cada activity nuevo de tipo Fragment, se debe inicializar el
	 * tag del Fragment aqui.
	 */
	final static String tagFragmentClientes = "fragmentClientes";
	final static String tagFragmentSettings = "fragmentSettings";
	final static String tagFragmentLogout = "fragmentLogout";
	final static String tagFragmentDatosCliente = "fragmentDatosCliente";

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
			String value = savedInstanceState.getString("fragmentoActual");
			establecerLayout(value);
		}

	}
	
	/*
	 * Funcion que se llama antes de de destruir el layout al cambiarse la orientacion
	 * del dispositivo. Guarda el tag del frame que estaba activo antes de producirse
	 * el cambio de orientacion de manera que pueda ser recuperado en el onCreate.
	 * 
	 * @param bundle Instancia del Bundle de la actividad creada.
	 * 
	 */
	protected void onSaveInstanceState(Bundle bundle) {
		  super.onSaveInstanceState(bundle);
		  Fragment fragmentoActual = this.getSupportFragmentManager().findFragmentById(android.R.id.tabcontent);
		  bundle.putString("fragmentoActual", fragmentoActual.getTag());
		  Log.e("layout-guardando",fragmentoActual.getTag());
		}

	
	/*
	 * Funcion que establece el layout correspondiente cuando se cambia la orientacion
	 * del dispositivo.
	 * 
	 * @param tagFragment Tag del fragmento que se va a cargar.
	 */
	public void establecerLayout(String tagFragment){
		mTabHost.setCurrentTabByTag(tagFragment);
		Fragment fragmentoActual = this.getSupportFragmentManager().findFragmentByTag(tagFragment);
		if(fragmentoActual!=null){
			pushFragments(tagFragment, fragmentoActual);
		}else{
			Log.e("**Error**", "**No debo entrar aqui**");
		}
	}
	
	/*
	 * Inicializa los tabs y setea los views e identificadores para los tabs.
	 */
	public void inicializarTab() {

		//Obtenemos los recursos
		Resources res = getResources(); 

		//TabHost.TabSpec: A tab has a tab indicator, content, and a tag that is used to keep track of it. 
		TabHost.TabSpec spec =  mTabHost.newTabSpec(tagFragmentClientes);


		//TabHost.TabContentFactory: Makes the content of a tab when it is selected. 
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(" Clientes ", res.getDrawable(R.drawable.clientes));
		mTabHost.addTab(spec);


		spec = mTabHost.newTabSpec(tagFragmentSettings);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(" Settings ",res.getDrawable(R.drawable.settings));
		mTabHost.addTab(spec);

		spec =   mTabHost.newTabSpec(tagFragmentLogout);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(" Logout ",res.getDrawable(R.drawable.logouticon));
		mTabHost.addTab(spec);

	}

	/*
	 * TabChangeListener para cambiar el contenido del FrameLayout cuando uno de los 
	 * tabs es presionado.
	 */
	TabHost.OnTabChangeListener listener    =   new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			/*Set current tab..*/
			if(tabId.equals(tagFragmentClientes)){
				pushFragments(tagFragmentClientes, fragmentClientes);
			}else if(tabId.equals(tagFragmentSettings)){
				pushFragments(tagFragmentSettings, fragmentSettings);
			}else if(tabId.equals(tagFragmentLogout)){
				pushFragments(tagFragmentLogout, fragmentLogout);
			}
		}
	};

	/*
	 * Reemplaza el contenido del FrameLayout por el nuevo fragment.
	 * 
	 * @param tag Tag del fragment que se va a invocar.
	 * @param fragment Fragmento que se reemplaza en el FrameLayout.
	 */
	public void pushFragments(String tag, Fragment fragment){

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();

		ft.replace(android.R.id.tabcontent, fragment, tag);
		ft.commit();
	}
	
	
	/*
	 * Metodo que confirma la salida de la aplicacion.
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
