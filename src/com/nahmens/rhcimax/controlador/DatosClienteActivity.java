package com.nahmens.rhcimax.controlador;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;

public class DatosClienteActivity extends Fragment {

	private LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_datos_cliente, container, false);
		this.inflater=inflater;

		// Registro del evento OnClick del buttonSalvar
		Button bSalvar = (Button)view.findViewById(R.id.buttonSalvar);
		bSalvar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickSalvar();
			}
		});
		return view;
	}

	public void onClickSalvar(){
		EditText etIdEmpresa = (EditText) getActivity().findViewById(R.id.textEditEmpEmpleado);
		EditText etNombre = (EditText) getActivity().findViewById(R.id.textEditNombEmpleado);
		EditText etApellido = (EditText) getActivity().findViewById(R.id.textEditApellidoEmpleado);
		EditText etPosicion = (EditText) getActivity().findViewById(R.id.textEditPosEmpleado);
		EditText etEmail = (EditText) getActivity().findViewById(R.id.textEditEmailEmpleado);
		EditText etTelfOficina = (EditText) getActivity().findViewById(R.id.textEditTelfOficEmpleado);
		EditText etCelular = (EditText) getActivity().findViewById(R.id.textEditCelularEmpleado);
		EditText etPin = (EditText) getActivity().findViewById(R.id.textEditPinEmpleado);
		EditText etLinkedin = (EditText) getActivity().findViewById(R.id.textEditLinkedinEmpleado);
		EditText etDescripcion = (EditText) getActivity().findViewById(R.id.textEditDescripEmpleado);

		int idEmpresa = Integer.parseInt(etIdEmpresa.getText().toString());
		String nombre = etNombre.getText().toString();
		String apellido = etApellido.getText().toString();
		String posicion = etPosicion.getText().toString();
		String email = etEmail.getText().toString();
		String telfOficina = etTelfOficina.getText().toString();
		String celular = etCelular.getText().toString();
		String linkedin = etLinkedin.getText().toString();
		String pin = etPin.getText().toString();
		String descripcion = etDescripcion.getText().toString();

		Empleado empleado = new Empleado(nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa);
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();

		Boolean insertado = empleadoDao.insertarEmpleado(getActivity(), empleado);







		if(insertado){
			Mensaje mToast = new Mensaje(inflater, getActivity(), "ok_ingreso_empleado");
			mToast.controlMensajes();
			
			//Toast.makeText(getActivity(), "Empleado ingresado satisfactoriamente",Toast.LENGTH_LONG).show();
		}else{
			Mensaje mToast = new Mensaje(inflater, getActivity(), "error_ingreso_empleado");
			mToast.controlMensajes();
			
			//Toast.makeText(getActivity(), "Error: el empleado no pudo ser ingresado",Toast.LENGTH_LONG).show();
		}


	}



}
