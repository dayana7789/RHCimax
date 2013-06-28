package com.nahmens.rhcimax.controlador;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaHistoricosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.modelo.Cotizacion;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.HistoricoSqliteDao;

public class HistoricosActivity extends Fragment {

	ListaHistoricosCursorAdapter listCursorAdapterHistoricos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_historicos, container, false);

		//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
		//tab es el correspondiente.
		AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentHistoricos);	

		//Asociamos los valores al combo box o spinner
		Spinner spinner = (Spinner) view.findViewById(R.id.spinnerHistoricos);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.historicos_filtro_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		listarHistoricos(view);


		return view;
	}

	private void listarHistoricos(View view) {
		HistoricoSqliteDao historicoDao = new HistoricoSqliteDao();
		Context contexto = getActivity();
		Cursor mCursorHistoricos = historicoDao.listarHistoricos(getActivity());

		if(mCursorHistoricos.getCount()>0){

			final ListView lvHistoricos = (ListView) view.findViewById (android.R.id.list);
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] fromCotizacion = new String[] {"idCotizacion", "loginUsuario", "nombreEmpresaCotizacion", "nombreEmpleadoCotizacion", "apellidoEmpleadoCotizacion", Cotizacion.FECHA_ENVIO, Cotizacion.FECHA_LEIDO};
			int[] toCotizacion = new int[] { R.id.textViewTitulo,  R.id.textViewExtra,  R.id.textViewFil1Col1, R.id.textViewFil2Col1, R.id.textViewFil2Col1, R.id.textViewFil1Col2, R.id.textViewFil2Col2};
			
			String[] fromTarea = new String[] { "nombreTarea", "loginUsuarioTarea", Tarea.FECHA, Tarea.HORA, "nombreEmpleadoTarea", "apellidoEmpleadoTarea", "nombreEmpresaTarea", Tarea.FECHA_FINALIZACION};
			int[] toTarea = new int[] {R.id.textViewTitulo,  R.id.textViewExtra, R.id.textViewFil1Col2, R.id.textViewFil1Col2, R.id.textViewFil2Col1, R.id.textViewFil2Col1, R.id.textViewFil1Col1, R.id.textViewFil2Col2 };

			String[] fromVisita = new String[] { "nombreEmpresaVisita",  "loginUsuarioVisita", Checkin.CHECKIN, Checkin.CHECKOUT};
			int[] toVisita = new int[] {R.id.textViewTitulo,  R.id.textViewExtra, R.id.textViewFil1Col1, R.id.textViewFil2Col1};

			
			//Creamos un array adapter para desplegar cada una de las filas
			listCursorAdapterHistoricos = new ListaHistoricosCursorAdapter(contexto, R.layout.activity_fila_historico, mCursorHistoricos, fromCotizacion, toCotizacion, 0, fromTarea, toTarea, fromVisita, toVisita);
			lvHistoricos.setAdapter(listCursorAdapterHistoricos);


		}

	}

}
