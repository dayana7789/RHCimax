package com.nahmens.rhcimax.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaEmpleadosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.UtilityScroll;

public class DatosEmpresaActivity extends Fragment {

	//Campos formulario:
	EditText etNombre;
	EditText etTelefono;
	EditText etWeb;
	EditText etRif;
	EditText etDirFiscal;
	EditText etDirComercial;
	TableLayout tlListEmpleados;

	/* Flag que permite saber si al crear una nueva empresa, esta se guardo
	 * antes de hacer click en el boton + para agregar un nuevo empleado. 
	 */
	private boolean flagGuardado;
	private Bundle mArgumentos;

	private int numContactos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_datos_empresa, container, false);

		//OJO: evitamos que la pantalla se vuelva a recrear verificando el valor
		//de savedInstanceState. De esta manera evitamos la doble llamada que se
		//realiza al metodo onCreateView, cuando cambiamos la orientacion del 
		//dispositivo.
		if (savedInstanceState==null){
			
			//inicializamos la referencia a los campos del formulario
			setReferenciaCampos(view);

			mArgumentos = this.getArguments();
			numContactos = 0;

			//Si me pasaron argumentos, relleno la vista con la informacion. 
			//De lo contrario, dejo todo vacio.
			if(mArgumentos!= null){
				flagGuardado = true;

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
			}else{
				//creacion de empresa nueva
				flagGuardado = false;
			}



			// Registro del evento OnClick del buttonCopiar
			ImageButton bCopiar= (ImageButton)view.findViewById(R.id.imageButtonCopiar);
			bCopiar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String dirFiscal = etDirFiscal.getText().toString();
					etDirComercial.setText(dirFiscal);
				}
			});

			// Registro del evento OnClick del buttonAgregarEmpleado
			ImageButton bAgregar= (ImageButton)view.findViewById(R.id.imageButtonAgregarEmpleado);
			bAgregar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if(flagGuardado){
						DatosClienteActivity fragment = new DatosClienteActivity();

						//pasamos al fragment el id de la empresa
						fragment.setArguments(mArgumentos); 

						getFragmentManager().beginTransaction()
						.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosCliente)
						.addToBackStack(null)
						.commit();

					}else{

						AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
						String[] mensArray = null;
						Mensaje mensajeDialog = new Mensaje("guardar_cambios_empresa");

						try {
							mensArray = mensajeDialog.controlMensajesDialog(null);
						} catch (Exception e) {
							e.printStackTrace();
						}

						alert.setMessage(mensArray[0]); 
						alert.setTitle(mensArray[1]); 

						alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.cancel();
							}});

						alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								String id = null;

								if(mArgumentos!=null){
									id = mArgumentos.getString("idEmpresa");
								}

								onClickSalvar(id);
							}
						});

						AlertDialog alertDialog = alert.create();
						alertDialog.show();
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

			// Registro del evento OnClick del buttonCheckin
			Button bCheckin = (Button) view.findViewById(R.id.buttonCheckin);
			bCheckin.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.e("bCheckin", "me presionaron");
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

			// Registro del evento OnClick del buttonCotizar
			Button bCotizar = (Button)view.findViewById(R.id.buttonCotizar);
			bCotizar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ServiciosActivity fragment = new ServiciosActivity();

					if(numContactos==0){
						Mensaje mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "error_empresa_sin_empleados");

						try {
							mToast.controlMensajesToast();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}else{
						//pasamos al fragment el id de la empresa
						fragment.setArguments(mArgumentos); 

						getFragmentManager().beginTransaction()
						.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentServicios)
						.addToBackStack(null)
						.commit();
					}
				}
			});
		}

		return view;
	}

	/**
	 * Funcion que almacena la referencia de los campos de tal manera que estos
	 * sean calculados una sola vez.
	 * @param view
	 */
	private void setReferenciaCampos(View v) {
		etNombre = (EditText) v.findViewById(R.id.textEditNombEmpresa);
		etTelefono = (EditText) v.findViewById(R.id.textEditTelfEmpresa);
		etWeb = (EditText) v.findViewById(R.id.textEditWebEmpresa);
		etRif = (EditText) v.findViewById(R.id.textEditRifEmpresa);
		etDirFiscal = (EditText) v.findViewById(R.id.textEditDirFiscEmpresa);
		etDirComercial = (EditText) v.findViewById(R.id.textEditDirComerEmpresa);
	}

	private void listarEmpleados(View view, String idEmpresa){
		//Cargamos la lista de empleados
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Context context = getActivity();
		Cursor mCursorEmpleados = empleadoDao.listarEmpleadosPorEmpresa(getActivity(),idEmpresa);
		numContactos = mCursorEmpleados.getCount();

		if(mCursorEmpleados.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			//OJO: Aqui pasamos  Empleado.ID para no invocarlo directamente en el ListaClientesCursorAdapter
			// y lo relacionamos en el arreglo 'to' con el valor 0.
			String[] from = new String[] { Empleado.NOMBRE, Empleado.APELLIDO, Empleado.POSICION};
			int[] to = new int[] {R.id.textViewNombreEmp,  R.id.textViewApellidoEmp, R.id.textViewPosicionEmp };


			//Creamos un array adapter para desplegar cada una de las filas
			ListaEmpleadosCursorAdapter notes = new ListaEmpleadosCursorAdapter(tlListEmpleados, context, R.layout.activity_fila_empleado, mCursorEmpleados, from, to, 0, getFragmentManager());

		}
	}

	/*
	 * Funcion que se encarga de cargar los datos de una empresa en sus respectivos campos.
	 * 
	 * @param v View de la actividad.
	 * @param empresa Empresa cuya informacion se esta cargando.
	 */
	private void llenarCamposEmpresa(View v, Empresa empresa) {
		etNombre.setText(empresa.getNombre());
		etTelefono.setText(empresa.getTelefono());
		etWeb.setText(empresa.getWeb());
		etRif.setText(empresa.getRif());
		etDirFiscal.setText(empresa.getDirFiscal());
		etDirComercial.setText(empresa.getDirComercial());
		tlListEmpleados = (TableLayout) v.findViewById (R.id.tableLayoutListaEmpleados);
	}


	/*
	 * @param id Id de la empresa.
	 */
	public void onClickSalvar(String id){
		Mensaje mToast = null;
		boolean error = false;
		LayoutInflater mInflater = getActivity().getLayoutInflater();

		Date dateFechaModif = new Date();
		String myFormat = "dd/MM/yyyy HH:mm:ss"; 
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
		String fechaModif = sdf.format(dateFechaModif);

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

		if(rif.equals("") || rif==null){
			etRif.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(telefono.equals("") || telefono==null){
			etTelefono.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(dirFiscal.equals("") || dirFiscal==null){
			etDirFiscal.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}
		/** Fin Verificacion de errores **/

		if(!error){
			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();

			SharedPreferences prefs = this.getActivity().getSharedPreferences("Usuario",Context.MODE_PRIVATE);
			int idUsuario = prefs.getInt(Usuario.ID, 0);

			if(id!=null){
				//Estamos modificando un registro

				//cada vez que hagamos una modificacion, colocamos el valor de fechaSincronizacion en null 
				//para saber que este empleado esta desactualizado en la nube
				String fechaSincronizacion = null;

				Empresa empresa = new Empresa(Integer.parseInt(id), nombre, telefono, rif, web, dirFiscal, dirComercial, fechaModif, idUsuario);

				Boolean modificado = empresaDao.modificarEmpresa(getActivity(), empresa);

				if(modificado){
					mToast = new Mensaje(mInflater, getActivity(), "ok_modificar_empresa");

				}else{
					mToast = new Mensaje(mInflater, getActivity(), "error_modificar_empresa");
				}
			}else{
				//Estamos creando un nuevo registro

				//cada vez que hagamos una modificacion, colocamos el valor de fechaSincronizacion en null 
				//para saber que este empleado esta desactualizado en la nube
				String fechaSincronizacion = null;

				Empresa empresa = new Empresa(nombre, telefono, rif, web, dirFiscal, dirComercial, idUsuario, idUsuario);

				long idFilaInsertada = empresaDao.insertarEmpresa(getActivity(), empresa);

				if(idFilaInsertada != -1){
					mToast = new Mensaje(mInflater, getActivity(), "ok_ingreso_empresa");
					mArgumentos = new Bundle();
					mArgumentos.putString("idEmpresa", ""+idFilaInsertada);
					flagGuardado = true;

				}else{
					mToast = new Mensaje(mInflater, getActivity(), "error_ingreso_empresa");
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
