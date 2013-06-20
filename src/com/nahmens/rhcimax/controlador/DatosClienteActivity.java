package com.nahmens.rhcimax.controlador;

import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.AutocompleteEmpresaCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;

public class DatosClienteActivity extends Fragment {

	private LayoutInflater inflater;
	private View mView;
	private FragmentManager fragmentManager; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_datos_cliente, container, false);
		this.inflater=inflater;
		this.mView = view;
		this.fragmentManager = this.getFragmentManager();

		//usando dentro del metodo onclick del boton ver empresa
		final LayoutInflater inf = inflater;

		setAutocompleteEmpresa(view);

		final Bundle mArgumentos = this.getArguments();

		//Si me pasaron argumentos, relleno la vista con la informacion. 
		//De lo contrario, dejo todo vacio.
		if(mArgumentos!= null){

			String idEmpleado = mArgumentos.getString("id");

			if(idEmpleado !=null){
				//estamos modificando a un empleado
				EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				Empleado empleado  = empleadoDao.buscarEmpleado(getActivity(),idEmpleado);

				if(empleado !=null){
					Empresa empresa = empresaDao.buscarEmpresa(view.getContext(), String.valueOf(empleado.getIdEmpresa()));
					llenarCamposEmpleado(view, empleado,empresa.getNombre());

				}else{
					//Esto nunca deberia llamarse
					Mensaje mToast = new Mensaje("error_general");
					try {
						mToast.controlMensajesToast();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				//estamos agregando un nuevo empleado, a partir del id de la empresa
				String idEmpresa = mArgumentos.getString("idEmpresa");

				EditText etIdEmpresa = (EditText) view.findViewById(R.id.textEditHiddenIdEmpresa);
				AutoCompleteTextView acNombreEmpresa = (AutoCompleteTextView) view.findViewById(R.id.autocompleteEmpresa);

				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				Empresa empresa = empresaDao.buscarEmpresa(view.getContext(), idEmpresa);

				etIdEmpresa.setText(idEmpresa);
				acNombreEmpresa.setText(empresa.getNombre());
			}
		}

		// Registro del evento OnClick del buttonVerEmpresa
		ImageButton bVerEmpresa = (ImageButton)view.findViewById(R.id.imageButtonVerEmpresa);
		bVerEmpresa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText etIdEmpresa = (EditText) mView.findViewById(R.id.textEditHiddenIdEmpresa);
				String idEmpresa = etIdEmpresa.getText().toString();

				if(idEmpresa.equals("") || idEmpresa.equals(null)){
					Mensaje mToast = new Mensaje(inf, getActivity(), "error_empresa_no_existe");

					try {
						mToast.controlMensajesToast();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else{

					Bundle mArgumentos = new Bundle();
					mArgumentos.putString("idEmpresa",idEmpresa);

					Fragment fragment = new DatosEmpresaActivity();

					fragment.setArguments(mArgumentos); 

					FragmentManager manager = getFragmentManager();
					FragmentTransaction ft = manager.beginTransaction();
					ft.addToBackStack(null);
					ft.replace(android.R.id.tabcontent, fragment, AplicacionActivity.tagFragmentDatosEmpresa);
					ft.commit();
				}

			}
		});

		// Registro del evento OnClick del LinearLayoutButtonTareas
		LinearLayout bTareas = (LinearLayout) view.findViewById(R.id.LinearLayoutButtonTareas);
		bTareas.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("tareas", "me presionaron");
			}
		});

		// Registro del evento OnClick del LinearLayoutButtonHistoricos
		LinearLayout bHistoricos = (LinearLayout) view.findViewById(R.id.LinearLayoutButtonHistoricos);
		bHistoricos.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("historicos", "me presionaron");
			}
		});

		// Registro del evento OnClick del buttonSalvar
		Button bSalvar = (Button)view.findViewById(R.id.buttonSalvar);
		bSalvar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String id = null;

				if(mArgumentos!=null){
					id = mArgumentos.getString("id");
				}

				onClickSalvar(id);
			}
		});

		// Registro del evento OnClick del buttonCotizar
		Button bCotizar = (Button)view.findViewById(R.id.buttonCotizar);
		bCotizar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ServiciosActivity fragment = new ServiciosActivity();

				//pasamos al fragment el id de la empresa
				fragment.setArguments(mArgumentos); 

				fragmentManager.beginTransaction()
				.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentServicios)
				.addToBackStack(null)
				.commit();
			}
		});
		return view;
	}

	/*
	 * Funcion que prepara el campo de empresa para que sea autocomplete.
	 * @param view View de la actividad.
	 */
	private void setAutocompleteEmpresa(View view){


		AutocompleteEmpresaCursorAdapter mAutocompleteCursor = new AutocompleteEmpresaCursorAdapter(view);

		AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autocompleteEmpresa);
		textView.setAdapter(mAutocompleteCursor);
		//seteamos este listener definido en la clase AutocompleteEmpresaCursorAdapter
		textView.setOnItemClickListener(mAutocompleteCursor);
		textView.addTextChangedListener(mAutocompleteCursor);
		//modificamos el siguiente valor para que despliegue lista a partir del ingreso de un caracter
		textView.setThreshold(1);
	}


	/*
	 * Funcion que se encarga de cargar los datos de un empleado en sus respectivos campos.
	 * 
	 * @param v View de la actividad.
	 * @param emplado Empleado cuya informacion se esta cargando.
	 */
	private void llenarCamposEmpleado(View v, Empleado empleado, String nombreEmpresa) {
		EditText etNombre = (EditText) v.findViewById(R.id.textEditNombEmpleado);
		EditText etApellido = (EditText) v.findViewById(R.id.textEditApellidoEmpleado);
		EditText etPosicion = (EditText) v.findViewById(R.id.textEditPosEmpleado);
		EditText etEmail = (EditText) v.findViewById(R.id.textEditEmailEmpleado);
		EditText etTelfOficina = (EditText) v.findViewById(R.id.textEditTelfOficEmpleado);
		EditText etCelular = (EditText) v.findViewById(R.id.textEditCelularEmpleado);
		EditText etPin = (EditText) v.findViewById(R.id.textEditPinEmpleado);
		EditText etLinkedin = (EditText) v.findViewById(R.id.textEditLinkedinEmpleado);
		EditText etDescripcion = (EditText) v.findViewById(R.id.textEditDescripEmpleado);
		EditText etIdEmpresa = (EditText) v.findViewById(R.id.textEditHiddenIdEmpresa);
		AutoCompleteTextView acNombreEmpresa = (AutoCompleteTextView) v.findViewById(R.id.autocompleteEmpresa);

		etNombre.setText(empleado.getNombre());
		etApellido.setText(empleado.getApellido());
		etPosicion.setText(empleado.getPosicion());
		etEmail.setText(empleado.getEmail());
		etTelfOficina.setText(empleado.getTelfOficina());
		etCelular.setText(empleado.getCelular());
		etPin.setText(empleado.getPin());
		etLinkedin.setText(empleado.getLinkedin());
		etDescripcion.setText(empleado.getDescripcion());
		etIdEmpresa.setText(""+empleado.getIdEmpresa());
		acNombreEmpresa.setText(nombreEmpresa);

	}

	/*
	 * @param id Id del empleado
	 */
	public void onClickSalvar(String id){
		Mensaje mToast = null;
		boolean error = false;

		EditText etIdEmpresa = (EditText) getActivity().findViewById(R.id.textEditHiddenIdEmpresa);
		EditText etNombre = (EditText) getActivity().findViewById(R.id.textEditNombEmpleado);
		EditText etApellido = (EditText) getActivity().findViewById(R.id.textEditApellidoEmpleado);
		EditText etPosicion = (EditText) getActivity().findViewById(R.id.textEditPosEmpleado);
		EditText etEmail = (EditText) getActivity().findViewById(R.id.textEditEmailEmpleado);
		EditText etTelfOficina = (EditText) getActivity().findViewById(R.id.textEditTelfOficEmpleado);
		EditText etCelular = (EditText) getActivity().findViewById(R.id.textEditCelularEmpleado);
		EditText etPin = (EditText) getActivity().findViewById(R.id.textEditPinEmpleado);
		EditText etLinkedin = (EditText) getActivity().findViewById(R.id.textEditLinkedinEmpleado);
		EditText etDescripcion = (EditText) getActivity().findViewById(R.id.textEditDescripEmpleado);
		AutoCompleteTextView acNombreEmpresa = (AutoCompleteTextView) getActivity().findViewById(R.id.autocompleteEmpresa);

		int idEmpresa = 0;

		//este try catch es para evitar errores de tipo de campo
		try{
			idEmpresa = Integer.parseInt(etIdEmpresa.getText().toString());
		}catch (Exception e) {}

		String nombre = etNombre.getText().toString();
		String apellido = etApellido.getText().toString();
		String posicion = etPosicion.getText().toString();
		String email = etEmail.getText().toString();
		String telfOficina = etTelfOficina.getText().toString();
		String celular = etCelular.getText().toString();
		String linkedin = etLinkedin.getText().toString();
		String pin = etPin.getText().toString();
		String descripcion = etDescripcion.getText().toString();
		String nombreEmpresa = acNombreEmpresa.getText().toString();

		/** Verificacion de errores **/
		if(nombre.equals("") || nombre==null){
			etNombre.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(apellido.equals("") || apellido==null){
			etApellido.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(nombreEmpresa.equals("") || nombreEmpresa==null){
			acNombreEmpresa.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(email.equals("") || email==null){
			etEmail.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		Pattern pattern = Patterns.EMAIL_ADDRESS;
		if(!pattern.matcher(email).matches()){
			etEmail.setError(Mensaje.ERROR_EMAIL_INVALIDO);
			error = true;
		}

		/*pattern = Patterns.PHONE;
	    if(!pattern.matcher(celular).matches()){
	    	etCelular.setError(Mensaje.ERROR_TELF_INVALIDO);
			error = true;
	    }

	    if(!pattern.matcher(telfOficina).matches()){
	    	etCelular.setError(Mensaje.ERROR_TELF_INVALIDO);
			error = true;
	    }*/

		if (idEmpresa==0){
			acNombreEmpresa.setError(Mensaje.ERROR_EMPRESA_NO_VALIDA);
			error = true;
		}else{
			//Verificamos que el nombre de empresa ingresado se corresponda con alguno de la BD.

			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			Empresa empresa = empresaDao.buscarEmpresa(getActivity(), String.valueOf(idEmpresa));

			if(!nombreEmpresa.equals(empresa.getNombre()) && empresa.getNombre() !=null){
				acNombreEmpresa.setError(Mensaje.ERROR_EMPRESA_NO_VALIDA);
				error = true;
			}
		}

		/** FIN  Verificacion de errores **/

		if(!error){

			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();

			SharedPreferences prefs = this.getActivity().getSharedPreferences("Usuario",Context.MODE_PRIVATE);
			int idUsuario = prefs.getInt(Usuario.ID, 0);

			if(id!=null){

				//cada vez que hagamos una modificacion, colocamos el valor de fechaSincronizacion en null 
				//para saber que este empleado esta desactualizado en la nube
				String fechaSincronizacion = null;
				Empleado empleado = new Empleado( Integer.parseInt(id),nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa, idUsuario, fechaSincronizacion);

				//Estamos modificando un registro
				Boolean modificado = empleadoDao.modificarEmpleado(getActivity(), empleado);

				if(modificado){
					mToast = new Mensaje(inflater, getActivity(), "ok_modificar_empleado");

				}else{
					mToast = new Mensaje(inflater, getActivity(), "error_modificar_empleado");
				}

			}else{
				//Estamos creando un nuevo registro

				//cada vez que hagamos una modificacion, colocamos el valor de fechaSincronizacion en null 
				//para saber que este empleado esta desactualizado en la nube
				String fechaSincronizacion = null;

				Empleado empleado = new Empleado(nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa, idUsuario, fechaSincronizacion);
				Boolean insertado = empleadoDao.insertarEmpleado(getActivity(), empleado, idUsuario);

				if(insertado){
					mToast = new Mensaje(inflater, getActivity(), "ok_ingreso_empleado");

				}else{
					mToast = new Mensaje(inflater, getActivity(), "error_ingreso_empleado");
				}
			}


		}else{
			mToast = new Mensaje(inflater, getActivity(), "error_formulario");
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
