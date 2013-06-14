package com.nahmens.rhcimax.controlador;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.AutocompleteEmpleadoCursorAdapter;
import com.nahmens.rhcimax.adapters.AutocompleteEmpresaCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.InstantAutoComplete;

public class DatosTareaActivity extends Fragment {

	Calendar myCalendar = Calendar.getInstance();

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		final View view = inflater.inflate(R.layout.activity_datos_tarea, container, false);

		Bundle mArgumentos = this.getArguments();

		//Si me pasaron argumentos, relleno la vista con la informacion. 
		//De lo contrario, dejo todo vacio.
		if(mArgumentos!= null){

			String idTarea = mArgumentos.getString("idTarea");
			TareaSqliteDao tareaDao = new TareaSqliteDao();
			Tarea tarea  = tareaDao.buscarTarea(getActivity(),idTarea);

			if(tarea !=null){
				llenarCamposTarea(view, tarea);

			}else{
				//Esto nunca deberia llamarse
				Mensaje mToast = new Mensaje("error_general");
				try {
					mToast.controlMensajesToast();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		setAutocompleteEmpresa(view);
		setAutocompleteEmpleado(view);

		//Creacion de dialog y listener del campo fecha
		inicializarFecha(view);

		//Creacion de dialog y listener del campo hora
		inicializarHora(view);

		// Registro del evento OnClick del buttonVerEmpresa
		ImageButton bVerEmpresa = (ImageButton)view.findViewById(R.id.imageButtonVerEmpresa);
		bVerEmpresa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setOnclickVerEmpresa(inflater, view);
			}
		});

		// Registro del evento OnClick del buttonVerEmpleado
		ImageButton bVerEmpleado = (ImageButton)view.findViewById(R.id.imageButtonVerEmpleado);
		bVerEmpleado.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setOnclickVerEmpleado(inflater, view);
			}
		});

		// Registro del evento OnClick del buttonLimpiar
		Button bLimpiar= (Button)view.findViewById(R.id.buttonLimpiar);
		bLimpiar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				limpiarFormulario(view);
			}
		});

		return view;
	}

	private void setAutocompleteEmpleado(View view) {
		AutocompleteEmpleadoCursorAdapter mAutocompleteCursor = new AutocompleteEmpleadoCursorAdapter(view);

		InstantAutoComplete textView = (InstantAutoComplete) view.findViewById(R.id.autocompleteEmpleado);
		textView.setAdapter(mAutocompleteCursor);
		//seteamos este listener definido en la clase AutocompleteEmpresaCursorAdapter
		textView.setOnItemClickListener(mAutocompleteCursor);
		//modificamos el siguiente valor para que despliegue lista a partir del ingreso de un caracter
		textView.setThreshold(1);
		
	}

	/**
	 * Funcion que prepara el campo de empresa para que sea autocomplete.
	 * @param view View de la actividad.
	 */
	private void setAutocompleteEmpresa(View view) {
		AutocompleteEmpresaCursorAdapter mAutocompleteCursor = new AutocompleteEmpresaCursorAdapter(view);

		AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autocompleteEmpresa);
		textView.setAdapter(mAutocompleteCursor);
		//seteamos este listener definido en la clase AutocompleteEmpresaCursorAdapter
		textView.setOnItemClickListener(mAutocompleteCursor);
		textView.addTextChangedListener(mAutocompleteCursor);
		//modificamos el siguiente valor para que despliegue lista a partir del ingreso de un caracter
		textView.setThreshold(1);
		
	}

	/**
	 * Funcion que se llama al hacer click en ver empleado. 
	 * Carga la pagina de datos cliente.
	 * @param inflater
	 * @param view
	 */
	protected void setOnclickVerEmpleado(LayoutInflater inflater, View view) {
		EditText etIdEmpleado = (EditText) view.findViewById(R.id.textEditHiddenIdEmpleado);
		String idEmpleado = etIdEmpleado.getText().toString();

		if(idEmpleado.equals("") || idEmpleado.equals(null)){
			Mensaje mToast = new Mensaje(inflater, getActivity(), "error_empleado_no_existe");

			try {
				mToast.controlMensajesToast();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else{

			Bundle mArgumentos = new Bundle();
			mArgumentos.putString("id",idEmpleado);
			
			Fragment fragment = new DatosClienteActivity();
			
			fragment.setArguments(mArgumentos); 
			FragmentManager manager = getFragmentManager();
			FragmentTransaction ft = manager.beginTransaction();
			ft.addToBackStack(null);
			ft.replace(android.R.id.tabcontent, fragment, AplicacionActivity.tagFragmentDatosCliente);
			ft.commit();
		}
		
	}

	/**
	 * Funcion que se llama al hacer click en ver empresa. 
	 * Carga la pagina de datos empresa.
	 * @param inflater
	 * @param view
	 */
	protected void setOnclickVerEmpresa(LayoutInflater inflater, View view) {
		EditText etIdEmpresa = (EditText) view.findViewById(R.id.textEditHiddenIdEmpresa);
		String idEmpresa = etIdEmpresa.getText().toString();

		if(idEmpresa.equals("") || idEmpresa.equals(null)){
			Mensaje mToast = new Mensaje(inflater, getActivity(), "error_empresa_no_existe");

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

	/**
	 * Funcion que se encarga de cargar los datos de una tarea en sus respectivos campos.
	 * @param view
	 * @param tarea Tarea cuya informacion se esta cargando.
	 */
	private void llenarCamposTarea(View v, Tarea tarea) {
		EditText etNombre = (EditText) v.findViewById(R.id.editTextTarea);
		EditText etEmpresa = (EditText) v.findViewById(R.id.autocompleteEmpresa);
		EditText etHiddenIdEmpresa = (EditText) v.findViewById(R.id.textEditHiddenIdEmpresa);
		EditText etEmpleado = (EditText) v.findViewById(R.id.autocompleteEmpleado);
		EditText etHiddenIdEmpleado = (EditText) v.findViewById(R.id.textEditHiddenIdEmpleado);
		EditText etFecha = (EditText) v.findViewById(R.id.editTextFecha);
		EditText etHora = (EditText) v.findViewById(R.id.editTextTime);
		EditText etDescripcion = (EditText) v.findViewById(R.id.editTextDescripcion);
		CheckBox cbFinalizada = (CheckBox) v.findViewById(R.id.checkBoxFinalizada);

		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Empleado empleado  = empleadoDao.buscarEmpleado(getActivity(),""+tarea.getIdEmpleado());

		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Empresa empresa = empresaDao.buscarEmpresa(getActivity(), ""+tarea.getIdEmpresa());

		boolean finalizado = false;

		if(tarea.getFinalizada() == 1){
			finalizado = false;
		}

		etNombre.setText(tarea.getNombre());
		etEmpresa.setText(empresa.getNombre());
		etHiddenIdEmpresa.setText(tarea.getIdEmpresa());
		etEmpleado.setText(empleado.getNombre()+" " + empleado.getApellido());
		etHiddenIdEmpleado.setText(tarea.getIdEmpleado());
		etFecha.setText(tarea.getFecha());
		etHora.setText(tarea.getHora());
		etDescripcion.setText(tarea.getDescripcion());
		cbFinalizada.setChecked(finalizado);

	}

	/**
	 * Funcion que limpia todos los campos del formulario
	 * @param view
	 */
	protected void limpiarFormulario(View view) {
		((EditText) view.findViewById(R.id.editTextTarea)).setText("");
		((EditText) view.findViewById(R.id.autocompleteEmpresa)).setText("");
		((EditText) view.findViewById(R.id.autocompleteEmpleado)).setText("");
		((EditText) view.findViewById(R.id.editTextDescripcion)).setText("");
		((EditText) view.findViewById(R.id.textEditHiddenIdEmpleado)).setText("");
		((EditText) view.findViewById(R.id.textEditHiddenIdEmpresa)).setText("");
		setHora((Button) view.findViewById(R.id.editTextTime), true);
		setFecha((Button) view.findViewById(R.id.editTextFecha), true);
	}

	/**
	 * Funcion que crea dialog, listener y setea los valores de hora
	 * @param view
	 */
	private void inicializarHora(View view) {
		final Button etTime = (Button) view.findViewById(R.id.editTextTime);
		setHora(etTime, true);

		final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				myCalendar.set(Calendar.HOUR, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);
				setHora(etTime, false);
			}
		};

		etTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(getActivity(), time,  myCalendar
						.get(Calendar.HOUR),  myCalendar
						.get(Calendar.MINUTE),false).show();
			}
		});
	}

	/**
	 * Funcion que crea dialog, listener y setea los valores de fecha
	 * @param view
	 */
	private void inicializarFecha(View view) {
		final Button etFecha = (Button) view.findViewById(R.id.editTextFecha);
		setFecha(etFecha, true);

		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				setFecha(etFecha, false);
			}
		};

		etFecha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(getActivity(), date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

	}


	/**
	 * Funcion que setea la hora, segun calendar.
	 * @param etTime Referencia al layout
	 * @param inicializar Indica si queremos obtener la hora actual
	 */
	private void setHora(Button etTime, boolean inicializar) {
		String myFormat = "hh:mm a";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		if(inicializar){
			myCalendar = Calendar.getInstance();
		}
		etTime.setText(sdf.format(myCalendar.getTime()));
	}

	/**
	 * Funcion que setea la fecha, segun calendar.
	 * @param etFecha Referencia al layout
	 * @param inicializar Indica si queremos obtener la fecha actual
	 */
	private void setFecha(Button etFecha, boolean inicializar) {
		String myFormat = "dd/MM/yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		if(inicializar){
			myCalendar = Calendar.getInstance();
		}
		etFecha.setText(sdf.format(myCalendar.getTime()));
	}
}
