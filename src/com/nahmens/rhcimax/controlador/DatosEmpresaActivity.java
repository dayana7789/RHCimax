package com.nahmens.rhcimax.controlador;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;

public class DatosEmpresaActivity extends Fragment {

	private LayoutInflater inflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_datos_empresa, container, false);
		this.inflater=inflater;

		// Registro del evento OnClick del buttonSalvar
		Button bClient = (Button)view.findViewById(R.id.buttonSalvar);
		bClient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickSalvar();
			}
		});

		return view;
	}

	public void onClickSalvar(){
		EditText etNombre = (EditText) getActivity().findViewById(R.id.textEditNombEmpresa);
		EditText etTelefono = (EditText) getActivity().findViewById(R.id.textEditTelfEmpresa);
		EditText etWeb = (EditText) getActivity().findViewById(R.id.textEditWebEmpresa);
		EditText etRif = (EditText) getActivity().findViewById(R.id.textEditRifEmpresa);
		EditText etDirFiscal = (EditText) getActivity().findViewById(R.id.textEditDirFiscEmpresa);
		EditText etDirComercial = (EditText) getActivity().findViewById(R.id.textEditDirComerEmpresa);

		String nombre = etNombre.getText().toString();
		String telefono = etTelefono.getText().toString();
		String web = etWeb.getText().toString();
		String rif = etRif.getText().toString();
		String dirFiscal = etDirFiscal.getText().toString();
		String dirComercial = etDirComercial.getText().toString();

		Empresa empresa = new Empresa(nombre, telefono, rif, web, dirFiscal, dirComercial);
		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();

		Boolean insertado = empresaDao.insertarEmpresa(getActivity(), empresa);

		if(insertado){
			Mensaje mToast = new Mensaje(inflater, getActivity(), "ok_ingreso_empresa");
			try {
				mToast.controlMensajes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Mensaje mToast = new Mensaje(inflater, getActivity(), "error_ingreso_empresa");
			try {
				mToast.controlMensajes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
