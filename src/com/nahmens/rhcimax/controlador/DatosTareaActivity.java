package com.nahmens.rhcimax.controlador;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.nahmens.rhcimax.R;

public class DatosTareaActivity extends Fragment {

	Calendar myCalendar = Calendar.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		final View view = inflater.inflate(R.layout.activity_datos_tarea, container, false);

		//Creacion de dialog y listener del campo fecha
		inicializarFecha(view);

		//Creacion de dialog y listener del campo hora
		inicializarHora(view);

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

	/**
	 * Funcion que limpia todos los campos del formulario
	 * @param view
	 */
	protected void limpiarFormulario(View view) {
		((EditText) view.findViewById(R.id.editTextTarea)).setText("");
		((EditText) view.findViewById(R.id.autocompleteEmpresa)).setText("");
		((EditText) view.findViewById(R.id.autocompleteEmpleado)).setText("");
		((EditText) view.findViewById(R.id.textEditDescripcion)).setText("");
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
