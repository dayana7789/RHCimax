package com.nahmens.rhcimax.controlador;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaClientesCursorAdapter;
import com.nahmens.rhcimax.adapters.ListaEmpleadosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;

public class DatosEmpresaActivity extends Fragment {

	private LayoutInflater inflater;
	private View mView;
	private FragmentManager fragmentManager; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_datos_empresa, container, false);
		this.inflater=inflater;
		this.mView =  view;
		this.fragmentManager = this.getFragmentManager();

		final Bundle mArgumentos = this.getArguments();


		//Si me pasaron argumentos, relleno la vista con la informacion. 
		//De lo contrario, dejo todo vacio.
		if(mArgumentos!= null){

			String idEmpresa = mArgumentos.getString("idEmpresa");

			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			Empresa empresa  = empresaDao.buscarEmpresa(getActivity(),idEmpresa);


			if(empresa !=null){
				llenarCamposEmpresa(view, empresa);
				listarEmpleados(view, idEmpresa);

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



		// Registro del evento OnClick del buttonCopiar
		ImageButton bCopiar= (ImageButton)view.findViewById(R.id.imageButtonCopiar);
		bCopiar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText etDirFiscal = (EditText) mView.findViewById(R.id.textEditDirFiscEmpresa);
				EditText etDirComercial = (EditText) mView.findViewById(R.id.textEditDirComerEmpresa);

				String dirFiscal = etDirFiscal.getText().toString();
				etDirComercial.setText(dirFiscal);
			}
		});

		// Registro del evento OnClick del buttonAgregarEmpleado
		ImageButton bAgregar= (ImageButton)view.findViewById(R.id.imageButtonAgregarEmpleado);
		bAgregar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatosClienteActivity fragment = new DatosClienteActivity();

				//pasamos al fragment el id de la empresa
				fragment.setArguments(mArgumentos); 

				fragmentManager.beginTransaction()
				.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosCliente)
				.addToBackStack(null)
				.commit();
			}
		});

		// Registro del evento OnClick del buttonSalvar
		Button bSalvar = (Button)view.findViewById(R.id.buttonSalvar);
		bSalvar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String id = null;

				if(mArgumentos!=null){
					id = mArgumentos.getString("idEmpresa");
				}

				onClickSalvar(id);
			}
		});

		return view;
	}

	private void listarEmpleados(View view, String idEmpresa){
		//Cargamos la lista de empleados
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Context context = getActivity();
		Cursor mCursorEmpleados = empleadoDao.listarEmpleadosPorEmpresa(getActivity(),idEmpresa);

		if(mCursorEmpleados.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			//OJO: Aqui pasamos  Empleado.ID para no invocarlo directamente en el ListaClientesCursorAdapter
			// y lo relacionamos en el arreglo 'to' con el valor 0.
			String[] from = new String[] { Empleado.ID, Empleado.NOMBRE, Empleado.APELLIDO, Empleado.POSICION};
			int[] to = new int[] { 0, R.id.textViewNombreEmp,  R.id.textViewApellidoEmp, R.id.textViewPosicionEmp };
			ListView lvEmpleados = (ListView) view.findViewById (R.id.listViewEmpleados);

			//Creamos un array adapter para desplegar cada una de las filas
			//SimpleCursorAdapter notes = new SimpleCursorAdapter(context, R.layout.activity_fila_empleado, mCursor, from, to);
			ListaEmpleadosCursorAdapter notes = new ListaEmpleadosCursorAdapter(context, R.layout.activity_fila_empleado, mCursorEmpleados, from, to, 0,fragmentManager);
			lvEmpleados.setAdapter(notes);

		}
	}

	/*
	 * Funcion que se encarga de cargar los datos de una empresa en sus respectivos campos.
	 * 
	 * @param v View de la actividad.
	 * @param empresa Empresa cuya informacion se esta cargando.
	 */
	private void llenarCamposEmpresa(View v, Empresa empresa) {
		EditText etNombre = (EditText) v.findViewById(R.id.textEditNombEmpresa);
		EditText etTelefono = (EditText) v.findViewById(R.id.textEditTelfEmpresa);
		EditText etWeb = (EditText) v.findViewById(R.id.textEditWebEmpresa);
		EditText etRif = (EditText) v.findViewById(R.id.textEditRifEmpresa);
		EditText etDirFiscal = (EditText) v.findViewById(R.id.textEditDirFiscEmpresa);
		EditText etDirComercial = (EditText) v.findViewById(R.id.textEditDirComerEmpresa);

		etNombre.setText(empresa.getNombre());
		etTelefono.setText(empresa.getTelefono());
		etWeb.setText(empresa.getWeb());
		etRif.setText(empresa.getRif());
		etDirFiscal.setText(empresa.getDirFiscal());
		etDirComercial.setText(empresa.getDirComercial());
	}


	/*
	 * @param id Id de la empresa.
	 */
	public void onClickSalvar(String id){
		Mensaje mToast = null;
		boolean error = false;

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

		/** Verificacion de errores **/
		if(nombre.equals("") || nombre==null){
			etNombre.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}
		/** Fin Verificacion de errores **/

		if(!error){
			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();

			if(id!=null){
				//Estamos modificando un registro
				Empresa empresa = new Empresa(Integer.parseInt(id), nombre, telefono, rif, web, dirFiscal, dirComercial);

				Boolean modificado = empresaDao.modificarEmpresa(getActivity(), empresa);

				if(modificado){
					mToast = new Mensaje(inflater, getActivity(), "ok_modificar_empresa");

				}else{
					mToast = new Mensaje(inflater, getActivity(), "error_modificar_empresa");
				}
			}else{
				//Estamos creando un nuevo registro
				Empresa empresa = new Empresa(nombre, telefono, rif, web, dirFiscal, dirComercial);

				Boolean insertado = empresaDao.insertarEmpresa(getActivity(), empresa);

				if(insertado){
					mToast = new Mensaje(inflater, getActivity(), "ok_ingreso_empresa");

				}else{
					mToast = new Mensaje(inflater, getActivity(), "error_ingreso_empresa");
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
