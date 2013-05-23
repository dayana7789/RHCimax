package com.nahmens.rhcimax.controlador;

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

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.controlador.ClientesActivity.OnClienteSelectedListener;

/*
 * Contenedor principal de todos los fragmentos
 */

public class AplicacionActivity extends FragmentActivity implements OnClienteSelectedListener {

	/* Identificadores de los fragments que se cargan en este activity.
	 * Por cada activity nuevo de tipo Fragment, se debe inicializar el
	 * tag del Fragment aqui.
	 */
	final public static String tagFragmentClientes = "fragmentClientes";
	final public static String tagFragmentSettings = "fragmentSettings";
	final public static String tagFragmentLogout = "fragmentLogout";
	final public static String tagFragmentDatosCliente = "fragmentDatosCliente";
	final public static String tagFragmentDatosEmpresa = "fragmentDatosEmpresa";
	final public static String tagFragmentServicios = "fragmentServicios";

	TabHost mTabHost;

	/*Principales fragments de los tabs*/
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
			pushFragments(tagFragment, fragmentoActual, false);
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
				pushFragments(tagFragmentClientes, fragmentClientes, false);
			}else if(tabId.equals(tagFragmentSettings)){
				pushFragments(tagFragmentSettings, fragmentSettings, false);
			}else if(tabId.equals(tagFragmentLogout)){
				pushFragments(tagFragmentLogout, fragmentLogout, false);
			}
		}
	};

	/*
	 * Reemplaza el contenido del FrameLayout por el nuevo fragment.
	 * 
	 * @param tag Tag del fragment que se va a invocar.
	 * @param fragment Fragmento que se reemplaza en el FrameLayout.
	 * @param backStack. Indica si queremos que se guarde el fragmento anterior
	 *                   al presionar back button
	 */
	public void pushFragments(String tag, Fragment fragment, boolean backStack){

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();

		ft.replace(android.R.id.tabcontent, fragment, tag);

		if(backStack){
			ft.addToBackStack(null);
		}

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

		String fragmentoTag = fragmentoActual.getTag().toString();

		//Si hacemos click en el back button desde el fragmento de clientes, settings o logout, se nos muestra alert.
		if (fragmentoTag.equals(tagFragmentClientes) || fragmentoTag.equals(tagFragmentSettings) || fragmentoTag.equals(tagFragmentLogout)){

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


			//Si presionan back button desde fragmento Datos Cliente
		}else if(fragmentoTag.equals(tagFragmentDatosCliente)){

			/*
			 * OJO: las siguientes lineas de codigo son para que al agregar una empresa nueva, y despues hacer
			 * click en el boton + para agregar a un empleado y luego se haga click en el back button, se cargue 
			 * el fragment Datos empresa con la lista de empleados actualizada.
			 */
			//Obtenemos informacion del fragmento que queremos sobreescribir
			Fragment removefragment = this.getSupportFragmentManager().findFragmentByTag(tagFragmentDatosEmpresa);

			//Obtenemos informacion del fragmento datos cliente para poder tener acceso a los argumentos pasados a este: idEmpresa.
			Fragment fragmentClient = this.getSupportFragmentManager().findFragmentByTag(tagFragmentDatosCliente);

			if(removefragment != null){
				//Si llaman a datos empresa sin argumentos, eso significa que estoy en un formulario en blanco o vacio
				if(removefragment.getArguments()== null){

					//Verificamos que fragmentClient tenga argumentos
					if(fragmentClient.getArguments()!= null){

						//Creamos un nuevo fragmento DatosEmpresaActivity y le pasamos los argumentos que necesitamos: idEmpresa.
						//Tambien necesitamos saber que este fragmento paso por aqui, de manera que se pueda
						//añadir la logica necesaria cuando presionamos el back button desde datos empresa.
						DatosEmpresaActivity fragment = new DatosEmpresaActivity();

						Bundle mArgumentos = fragmentClient.getArguments();
						mArgumentos.putString("FLAG","Pase por aqui");
						fragment.setArguments(mArgumentos);

						pushFragments(tagFragmentDatosEmpresa, fragment, true);
					}else{
						
						super.onBackPressed();
					}
				}else{
					//Estoy en el caso en que agregue un empleado, sobre una empresa ya creada.
					super.onBackPressed();
				}
			}else{
				//Estoy en el caso en que estoy viendo informacion del empleado.
				super.onBackPressed();
			}


			//Si presionan back button desde fragmento Datos Empresa
		}else if(fragmentoTag.equals(tagFragmentDatosEmpresa)){

			/*
			 * OJO: Las siguientes lineas de codigo son para que al hacer click en el back button
			 * desde Datos empresa, necesariamente me lleve a la pagina de lista de clientes siempre
			 * y cuando, haya pasado por el if anterior a este, el cual pasa un FLAG al fragmento.
			 */
			//Obtenemos informacion del fragmento
			Fragment removefragment = this.getSupportFragmentManager().findFragmentByTag(tagFragmentDatosEmpresa);

			//Si me llamaron con argumentos..
			if(removefragment.getArguments()!= null){
				Bundle mArgumentos = removefragment.getArguments();
				String flag = mArgumentos.getString("FLAG");

				//Si me sobreescribieron..
				if(flag!=null){
					//Invocamos a la lista de clientes
					pushFragments(tagFragmentClientes, fragmentClientes, false);

				}else{
					super.onBackPressed();
				}
			}else{
				super.onBackPressed();
			}

			//sino permitimos que el back button nos lleven al fragmento anterior ejecutado	
		}else{
			super.onBackPressed();
		}

	}    

	/***INICIO DE CODIGO PARA PERMITIR QUE UNA FILA PUEDA SER SELECCIONADA ****/
	public void onEmpleadoSelected(String id) {
		//creamos un bundle para poder enviar al fragment, el id del empleado
		Bundle arguments = new Bundle();
		arguments.putString("id", id);

		DatosClienteActivity fragment = new DatosClienteActivity();

		//pasamos al fragment el id del empleado
		fragment.setArguments(arguments); 

		getSupportFragmentManager().beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosCliente)
		.addToBackStack(null)
		.commit();

	}

	public void onEmpresaSelected(String id) {
		//creamos un bundle para poder enviar al fragment, el id de la empresa
		Bundle arguments = new Bundle();
		arguments.putString("idEmpresa", id);

		DatosEmpresaActivity fragment = new DatosEmpresaActivity();

		//pasamos al fragment el id de la empresa
		fragment.setArguments(arguments); 

		getSupportFragmentManager().beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosEmpresa)
		.addToBackStack(null)
		.commit();

	}
	/***************FIN ****************************/



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aplicacion, menu);
		return true;
	}

}
