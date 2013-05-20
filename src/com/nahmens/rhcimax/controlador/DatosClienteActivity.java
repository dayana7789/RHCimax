package com.nahmens.rhcimax.controlador;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.AutocompleteEmpresaCursorAdapter;
import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DAO.EmpresaDAO;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;

public class DatosClienteActivity extends Fragment {

	private LayoutInflater inflater;
	private ConexionBD conexion;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_datos_cliente, container, false);
		this.inflater=inflater;

		final Bundle mArgumentos = this.getArguments();

		setAutocompleteEmpresa(view);

		
		//Si me pasaron argumentos, relleno la vista con la informacion. 
		//De lo contrario, dejo todo vacio.
		if(mArgumentos!= null){
			

			String idEmpleado = mArgumentos.getString("id");

			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			Empleado empleado  = empleadoDao.buscarEmpleado(getActivity(),idEmpleado);

			if(empleado !=null){
				llenarCamposEmpleado(view, empleado);
			}else{
				Mensaje mToast = new Mensaje("error_general");
				try {
					mToast.controlMensajesToast();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

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
		return view;
	}

	private void setAutocompleteEmpresa(View view){
		conexion = new ConexionBD(view.getContext());
		//Es importante que el open de la BD, se haga desde aqui para poder garantizar su cierre
		//al momento que se destruye esta actividad.
		conexion.open();
		
		AutocompleteEmpresaCursorAdapter mAutocompleteCursor = new AutocompleteEmpresaCursorAdapter(view.getContext(), conexion);
		
		AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autocompleteEmpEmpleado);
		textView.setAdapter(mAutocompleteCursor);
		//modificamos el siguiente valor para que despliegue lista a partir del ingreso de un caracter
		textView.setThreshold(1);
	}
	
	/*
	 * Se debe garantizar el cierre de la BD, al momento
	 * en que se destruye la actividad. De lo contrario,
	 * el close no se hace bien y se muestran excepciones.
	 */
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (conexion  != null) {
        	conexion.close();
        }
    }
	
	private void llenarCamposEmpleado(View v, Empleado empleado) {
		EditText etNombre = (EditText) v.findViewById(R.id.textEditNombEmpleado);
		EditText etApellido = (EditText) v.findViewById(R.id.textEditApellidoEmpleado);
		EditText etPosicion = (EditText) v.findViewById(R.id.textEditPosEmpleado);
		EditText etEmail = (EditText) v.findViewById(R.id.textEditEmailEmpleado);
		EditText etTelfOficina = (EditText) v.findViewById(R.id.textEditTelfOficEmpleado);
		EditText etCelular = (EditText) v.findViewById(R.id.textEditCelularEmpleado);
		EditText etPin = (EditText) v.findViewById(R.id.textEditPinEmpleado);
		EditText etLinkedin = (EditText) v.findViewById(R.id.textEditLinkedinEmpleado);
		EditText etDescripcion = (EditText) v.findViewById(R.id.textEditDescripEmpleado);
		//EditText etIdEmpresa = (EditText) v.findViewById(R.id.textEditEmpEmpleado);


		etNombre.setText(empleado.getNombre());
		etApellido.setText(empleado.getApellido());
		etPosicion.setText(empleado.getPosicion());
		etEmail.setText(empleado.getEmail());
		etTelfOficina.setText(empleado.getTelfOficina());
		etCelular.setText(empleado.getCelular());
		etPin.setText(empleado.getPin());
		etLinkedin.setText(empleado.getLinkedin());
		etDescripcion.setText(empleado.getDescripcion());
		//etIdEmpresa.setText(Integer.parseInt(empleado.getIdEmpresa()));

	}

	/*
	 * @param id Id del empleado
	 */
	public void onClickSalvar(String id){
		Mensaje mToast = null;

		EditText etIdEmpresa = (EditText) getActivity().findViewById(R.id.autocompleteEmpEmpleado);
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


		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();

		if(id!=null){
			Empleado empleado = new Empleado( Integer.parseInt(id),nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa);

			//Estamos modificando un registro
			Boolean modificado = empleadoDao.modificarEmpleado(getActivity(), empleado);

			if(modificado){
				mToast = new Mensaje(inflater, getActivity(), "ok_modificar_empleado");

			}else{
				mToast = new Mensaje(inflater, getActivity(), "error_modificar_empleado");
			}

		}else{
			//Estamos creando un nuevo registro
			Empleado empleado = new Empleado(nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa);
			Boolean insertado = empleadoDao.insertarEmpleado(getActivity(), empleado);

			if(insertado){
				mToast = new Mensaje(inflater, getActivity(), "ok_ingreso_empleado");

			}else{
				mToast = new Mensaje(inflater, getActivity(), "error_ingreso_empleado");
			}
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
