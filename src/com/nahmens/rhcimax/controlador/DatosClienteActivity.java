package com.nahmens.rhcimax.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

	//campos formulario:
	EditText etNombre;
	EditText etApellido;
	EditText etPosicion;
	EditText etEmail;
	EditText etTelfOficina;
	EditText etCelular;
	EditText etPin;
	EditText etLinkedin;
	EditText etDescripcion;
	EditText etIdEmpresa;
	AutoCompleteTextView acNombreEmpresa;
	
	/* Flag que permite saber si el empleado ya fue 
	 * insertado cuando se presiona el boton salvar dos veces.
	 * Evita que se duplique el registro.
	 */
	@SuppressWarnings("unused")
	private boolean flagGuardado;
	private Bundle mArgumentos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_datos_cliente, container, false);
		
		//inicializamos la referencia a los campos del formulario
		setReferenciaCampos(view);

		setAutocompleteEmpresa(view);

		mArgumentos = this.getArguments();
		flagGuardado = false;

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

					//Si el empleado tiene una empresa asociada, la buscamos
					if(empleado.getIdEmpresa() != 0){

						Empresa empresa = empresaDao.buscarEmpresa(getActivity(), String.valueOf(empleado.getIdEmpresa()));
						llenarCamposEmpleado(view, empleado,empresa.getNombre());

						// si no, mostramos el campo de empresa en blanco
					}else{
						llenarCamposEmpleado(view, empleado,"");
					}

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

				String idEmpresa = etIdEmpresa.getText().toString();

				if(idEmpresa.equals("") || idEmpresa.equals(null) || idEmpresa.equals("0") ){
					Mensaje mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "error_empresa_no_existe");

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

				getFragmentManager().beginTransaction()
				.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentServicios)
				.addToBackStack(null)
				.commit();
			}
		});
		return view;
	}

	/**
	 * Funcion que almacena la referencia de los campos de tal manera que estos
	 * sean calculados una sola vez.
	 * @param view
	 */
	private void setReferenciaCampos(View v) {
		etNombre = (EditText) v.findViewById(R.id.textEditNombEmpleado);
		etApellido = (EditText) v.findViewById(R.id.textEditApellidoEmpleado);
		etPosicion = (EditText) v.findViewById(R.id.textEditPosEmpleado);
		etEmail = (EditText) v.findViewById(R.id.textEditEmailEmpleado);
		etTelfOficina = (EditText) v.findViewById(R.id.textEditTelfOficEmpleado);
		etCelular = (EditText) v.findViewById(R.id.textEditCelularEmpleado);
		etPin = (EditText) v.findViewById(R.id.textEditPinEmpleado);
		etLinkedin = (EditText) v.findViewById(R.id.textEditLinkedinEmpleado);
		etDescripcion = (EditText) v.findViewById(R.id.textEditDescripEmpleado);
		etIdEmpresa = (EditText) v.findViewById(R.id.textEditHiddenIdEmpresa);
		acNombreEmpresa = (AutoCompleteTextView) v.findViewById(R.id.autocompleteEmpresa);
	}

	/*
	 * Funcion que prepara el campo de empresa para que sea autocomplete.
	 * @param view View de la actividad.
	 */
	private void setAutocompleteEmpresa(View view){
		AutocompleteEmpresaCursorAdapter mAutocompleteCursor = new AutocompleteEmpresaCursorAdapter(view);
		
		acNombreEmpresa.setAdapter(mAutocompleteCursor);
		//seteamos este listener definido en la clase AutocompleteEmpresaCursorAdapter
		acNombreEmpresa.setOnItemClickListener(mAutocompleteCursor);
		acNombreEmpresa.addTextChangedListener(mAutocompleteCursor);
		//modificamos el siguiente valor para que despliegue lista a partir del ingreso de un caracter
		acNombreEmpresa.setThreshold(1);
	}


	/*
	 * Funcion que se encarga de cargar los datos de un empleado en sus respectivos campos.
	 * 
	 * @param v View de la actividad.
	 * @param emplado Empleado cuya informacion se esta cargando.
	 */
	private void llenarCamposEmpleado(View v, Empleado empleado, String nombreEmpresa) {
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
		int idEmpresa = 0;
		LayoutInflater mInflater = getActivity().getLayoutInflater();
		
		Date dateFechaModif = new Date();
		String myFormat = "dd/MM/yyyy HH:mm:ss"; 
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
		String fechaModif = sdf.format(dateFechaModif);

		//este try catch es para evitar errores de tipo de campo
		try{
			idEmpresa = Integer.parseInt(etIdEmpresa.getText().toString());
		}catch (Exception e) {
		}

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


		if(email.equals("") || email==null){
			etEmail.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		Pattern pattern = Patterns.EMAIL_ADDRESS;
		if(!pattern.matcher(email).matches()){
			etEmail.setError(Mensaje.ERROR_EMAIL_INVALIDO);
			error = true;
		}

		//Si  el nombre de la empresa no es vacio..
		if(!nombreEmpresa.equals("") && nombreEmpresa!=null){
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
			//Si el nombre de la empresa es vacio, le asignamos idEmpresa=0
		}else{
			idEmpresa = 0;
		}

		/** FIN  Verificacion de errores **/

		if(!error){

			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();

			SharedPreferences prefs = this.getActivity().getSharedPreferences("Usuario",Context.MODE_PRIVATE);
			int idUsuario = prefs.getInt(Usuario.ID, 0);
			

			if(id!=null){
				//Estamos modificando un registro

				Empleado empleado = new Empleado(Integer.parseInt(id),nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa, fechaModif, idUsuario);
				
				Boolean modificado = empleadoDao.modificarEmpleado(getActivity(), empleado);

				if(modificado){
					mToast = new Mensaje(mInflater, getActivity(), "ok_modificar_empleado");

				}else{
					mToast = new Mensaje(mInflater, getActivity(), "error_modificar_empleado");
				}

			}else{
				//Estamos creando un nuevo registro

				Empleado empleado = new Empleado(nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa, idUsuario, idUsuario);
				long idFilaInsertada = empleadoDao.insertarEmpleado(getActivity(), empleado);

				if(idFilaInsertada != -1){
					mToast = new Mensaje(mInflater, getActivity(), "ok_ingreso_empleado");
					mArgumentos = new Bundle();
					mArgumentos.putString("id", ""+idFilaInsertada);

					flagGuardado = true;

				}else{
					mToast = new Mensaje(mInflater, getActivity(), "error_ingreso_empleado");
					flagGuardado = false;
				}
			}


		}else{
			mToast = new Mensaje(mInflater, getActivity(), "error_formulario");
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
