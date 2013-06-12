package com.nahmens.rhcimax.controlador;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nahmens.rhcimax.R;

public class TareasActivity extends Fragment {

	final Calendar myCalendar = Calendar.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_tareas, container, false);

		//Asociamos los valores al combo box o spinner
		inicializarSpinner(view);
		
		//Creacion de dialog y listener del campo fecha
		inicializarFecha(view);
		
		//Creacion de dialog y listener del campo hora
		inicializarHora(view);

		return view;
	}

	/**
	 * Funcion que crea dialog, listener y setea los valores de hora
	 * @param view
	 */
	private void inicializarHora(View view) {
		final Button etTime = (Button) view.findViewById(R.id.editTextTime);
		setHora(etTime);

		final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				myCalendar.set(Calendar.HOUR, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);
				setHora(etTime);
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
		setFecha(etFecha);

		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				setFecha(etFecha);
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
	 * Funcion que inicializa los valores del spinner o combo box
	 * @param view
	 */
	private void inicializarSpinner(View view) {
		Spinner spinner = (Spinner) view.findViewById(R.id.spinnerTareas);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.historicos_filtro_array, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	/**
	 * Funcion que setea la hora, segun calendar.
	 * @param etTime Referencia al layout
	 */
	private void setHora(Button etTime) {
		String myFormat = "hh:mm a";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		etTime.setText(sdf.format(myCalendar.getTime()));
	}

	/**
	 * Funcion que setea la fecha, segun calendar.
	 * @param etFecha Referencia al layout
	 */
	private void setFecha(Button etFecha) {
		String myFormat = "dd/MM/yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		etFecha.setText(sdf.format(myCalendar.getTime()));
	}

}
