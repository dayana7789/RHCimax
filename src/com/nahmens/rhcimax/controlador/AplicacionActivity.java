package com.nahmens.rhcimax.controlador;

import java.util.HashMap;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaCorreosCursorAdapter;
import com.nahmens.rhcimax.adapters.ListaServiciosCursorAdapter;
import com.nahmens.rhcimax.controlador.ClientesActivity.OnClienteSelectedListener;
import com.nahmens.rhcimax.utils.Tripleta;

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
	final public static String tagFragmentHistoricos = "fragmentHistoricos";
	final public static String tagFragmentTareas = "fragmentTareas";

	/*Tag para identificar a los cuadros de colores en la lista de clientes*/
	final public static String tagCuadroColor = "cuadroColor";
	final public static String tagAmarillo = "amarillo";
	final public static String tagVerde = "verde";
	final public static String tagRojo = "rojo";

	TabHost mTabHost;

	/*Principales fragments de los tabs*/
	ClientesActivity fragmentClientes;
	HistoricosActivity fragmentHistoricos;
	TareasActivity fragmentTareas;
	SettingsActivity fragmentSettings;
	LogoutActivity fragmentLogout;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aplicacion);

		fragmentClientes = new ClientesActivity();
		fragmentHistoricos = new HistoricosActivity();
		fragmentTareas = new TareasActivity();
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

			/* Si estamos en el fragmento de servicios,
			 * seteamos la lista de servicios que fueron seleccionados
			 * seteamos la lista de correos que fueron seleccionados
			 */		
			if(value.equals(tagFragmentServicios)){
				if(ListaServiciosCursorAdapter.getServiciosSeleccionados()!=null){
					ListaServiciosCursorAdapter.setServiciosSeleccionados((HashMap<Integer, Tripleta>) savedInstanceState.getSerializable("serviciosSelected"));
				}
				
				if(ListaCorreosCursorAdapter.getCorreosSeleccionados()!=null){
					ListaCorreosCursorAdapter.setCorreosSeleccionados((HashMap<Integer, Boolean>) savedInstanceState.getSerializable("correosSelected"));
				}
			}
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

		/* Si estamos en el fragmento de servicios,
		 * Guardamos la lista de servicios que fueron seleccionados
		 * Guardamos la lista de correos que fueron seleccionados
		 */
		if(fragmentoActual.getTag().equals(tagFragmentServicios)){
			if(ListaServiciosCursorAdapter.getServiciosSeleccionados()!=null){
				bundle.putSerializable("serviciosSelected", ListaServiciosCursorAdapter.getServiciosSeleccionados());
			}
			
			if(ListaCorreosCursorAdapter.getCorreosSeleccionados()!=null){
				bundle.putSerializable("correosSelected", ListaCorreosCursorAdapter.getCorreosSeleccionados());
			}
		}
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
		
		spec = mTabHost.newTabSpec(tagFragmentTareas);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(" Tareas ",res.getDrawable(R.drawable.tareas));
		mTabHost.addTab(spec);

		spec = mTabHost.newTabSpec(tagFragmentHistoricos);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(" Histórico ",res.getDrawable(R.drawable.historicos));
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

				//Creamos listener para cuando hagamos click sobre el tab previamente seleccionado
				//nos lleve al fragmento correspondiente.
				mTabHost.getCurrentTabView().setOnTouchListener(new OnTouchListener(){
					@Override
					public boolean onTouch(View v, MotionEvent event){

						if (event.getAction() == MotionEvent.ACTION_DOWN){
							if (mTabHost.getCurrentTabTag().equals(tagFragmentClientes)){
								pushFragments(tagFragmentClientes, fragmentClientes, false);
							}
						}
						return false;
					}
				});

			}else if(tabId.equals(tagFragmentHistoricos)){

				pushFragments(tagFragmentHistoricos, fragmentHistoricos, false);

			}else if(tabId.equals(tagFragmentTareas)){

				pushFragments(tagFragmentTareas, fragmentTareas, false);

			}else if(tabId.equals(tagFragmentSettings)){

				pushFragments(tagFragmentSettings, fragmentSettings, false);

			}else if(tabId.equals(tagFragmentLogout)){

				mostrarAvisoCierreApp();

				//Creamos listener para cuando hagamos click sobre el tab previamente seleccionado
				//nos saque el aviso de nuevo si lo volvemos a presionar.
				mTabHost.getCurrentTabView().setOnTouchListener(new OnTouchListener(){
					@Override
					public boolean onTouch(View v, MotionEvent event){

						if (event.getAction() == MotionEvent.ACTION_DOWN){
							if (mTabHost.getCurrentTabTag().equals(tagFragmentLogout)){
								mostrarAvisoCierreApp();
							}
						}
						return false;
					}
				});

				//pushFragments(tagFragmentLogout, fragmentLogout, false);
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

		//Si hacemos click en el back button desde el fragmento de clientes, historicos, tareas, settings o logout, se nos muestra alert.
		if (fragmentoTag.equals(tagFragmentClientes) || fragmentoTag.equals(tagFragmentHistoricos) ||  fragmentoTag.equals(tagFragmentTareas) || fragmentoTag.equals(tagFragmentSettings) || fragmentoTag.equals(tagFragmentLogout)){

			mostrarAvisoCierreApp();


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

			//seteamos la lista de servicios seleccionados y de correos a null de la vista servicios
			ListaServiciosCursorAdapter.setServiciosSeleccionados(null);
			ListaCorreosCursorAdapter.setCorreosSeleccionados(null);
			super.onBackPressed();
		}
	}   

	/*
	 * Funcion que muestra mensaje de alerta cuando hacemos logout
	 * o cuando llegamos a la raiz de la app y presionamos back button.
	 */
	private void mostrarAvisoCierreApp() {
		AlertDialog.Builder builder = new AlertDialog.Builder(AplicacionActivity.this);
		builder.setMessage("Está seguro que desea salir?")
		.setCancelable(false)
		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

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
