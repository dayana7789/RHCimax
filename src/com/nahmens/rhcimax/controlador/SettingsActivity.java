package com.nahmens.rhcimax.controlador;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Configuracion;
import com.nahmens.rhcimax.database.sqliteDAO.ConfiguracionSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


public class SettingsActivity extends Fragment {

	private LayoutInflater inflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_settings, container, false);
		this.inflater=inflater;
		
		//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
		//tab es el correspondiente.
		AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentSettings);	
		
		llenarCamposConfiguracion(view);

		// Registro del evento OnClick del buttonGuardar		
		ImageButton bGuardar = (ImageButton) view.findViewById(R.id.imageButtonGuardar);
		bGuardar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickSalvar();
			}

		});

		return view;
	}

	private void llenarCamposConfiguracion(View view) {
		EditText etServer = (EditText) view.findViewById(R.id.editTextServer);
		ConfiguracionSqliteDao configDao = new ConfiguracionSqliteDao();
		Configuracion configuracion  = configDao.buscarPorKey(getActivity(),Configuracion.NOMBRE_SERVIDOR);
		
		etServer.setText(configuracion.getValue());
	}

	private void onClickSalvar() {
		EditText etServer = (EditText) getActivity().findViewById(R.id.editTextServer);
		String server = etServer.getText().toString();
		boolean error = false;
		Mensaje mToast = null;
		
		/** Verificacion de errores **/
		if(server.equals("") || server==null){
			etServer.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}
		
		if(!error){
			ConfiguracionSqliteDao configDao = new ConfiguracionSqliteDao();
			Configuracion configuracion = new Configuracion(Configuracion.NOMBRE_SERVIDOR, server);
			Boolean modificado = configDao.modificarKeyValue(getActivity(), configuracion);
			
			if(modificado){
				mToast = new Mensaje(inflater, getActivity(), "ok_modificar_servidor");

			}else{
				mToast = new Mensaje(inflater, getActivity(), "error_modificar_servidor");
			}
			
			try {
				mToast.controlMensajesToast();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			mToast = new Mensaje(inflater, getActivity(), "error_formulario");
		}
	}
}
