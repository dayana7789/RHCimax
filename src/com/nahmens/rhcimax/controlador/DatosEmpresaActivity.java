package com.nahmens.rhcimax.controlador;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaEmpleadosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Historico;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.CheckinSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.HistoricoSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.FormatoFecha;
import com.nahmens.rhcimax.utils.SesionUsuario;

public class DatosEmpresaActivity extends Fragment {

	//Campos formulario:
	EditText etNombre;
	EditText etTelefono;
	EditText etWeb;
	EditText etRif;
	EditText etDirFiscal;
	EditText etDirComercial;
	TableLayout tlListEmpleados;
	TextView tvErrorPermiso;
	Button bSalvar;
	ImageButton bCopiar;

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

		//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
		//tab es el correspondiente.
		AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentClientes);	

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
				Log.e("aqui datos","idEmpresa: "+ idEmpresa);
				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				Empresa empresa  = empresaDao.buscarEmpresa(getActivity(),idEmpresa);

				if(empresa !=null){
					llenarCamposEmpresa(view, empresa);
					listarEmpleados(view, idEmpresa);

					mArgumentos.putString("nombreEmpresa", empresa.getNombre());

				}else{
					//Esto nunca deberia llamarse
					Mensaje mToast = new Mensaje("error_general");
					try {
						mToast.controlMensajesToast();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
				//verificamos los permisos del usuario para saber si este puede o no
				//modificar informacion
				ArrayList<String> permisos = SesionUsuario.getPermisos(getActivity());		
				
				if(permisos.contains(Permiso.MODIFICAR_PROPIOS)){
					Boolean esCliente = empresaDao.esClienteDelUsuario(getActivity(), idEmpresa, SesionUsuario.getIdUsuario(getActivity()));
					//verificamos si este es cliente del usuario
					if(!esCliente){
						setModoNoEditable();
					}
				}
				
			}else{
				//creacion de empresa nueva
				flagGuardado = false;
			}


			// Registro del evento OnClick del buttonCopiar
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
						.addToBackStack(AplicacionActivity.tagFragmentDatosCliente)
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
			TextView tvTareas = (TextView) view.findViewById(R.id.textViewButtonTareas);
			ImageView ivTareas = (ImageView) view.findViewById(R.id.imageViewButtonTareas);

			View.OnClickListener activityLauncherTareas = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String nombreEmpresa = null;

					if(mArgumentos!=null){
						nombreEmpresa = mArgumentos.getString("nombreEmpresa");
					}

					onButtonTareaSelected(nombreEmpresa);
				} 
			};

			bTareas.setOnClickListener(activityLauncherTareas);
			tvTareas.setOnClickListener(activityLauncherTareas);
			ivTareas.setOnClickListener(activityLauncherTareas);

			// Registro del evento OnClick del LinearLayoutButtonHistoricos
			LinearLayout bHistoricos = (LinearLayout) view.findViewById(R.id.LinearLayoutButtonHistoricos);
			TextView tvHistoricos = (TextView) view.findViewById(R.id.textViewButtonHistoricos);
			ImageView ivHistoricos = (ImageView) view.findViewById(R.id.imageViewButtonHistoricos);

			View.OnClickListener activityLauncherHistoricos = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String nombreEmpresa = null;

					if(mArgumentos!=null){
						nombreEmpresa = mArgumentos.getString("nombreEmpresa");
					}

					onButtonHistoricoSelected(nombreEmpresa);
				} 
			};

			bHistoricos.setOnClickListener(activityLauncherHistoricos);
			tvHistoricos.setOnClickListener(activityLauncherHistoricos);
			ivHistoricos.setOnClickListener(activityLauncherHistoricos);


			// Registro del evento OnClick del buttonCheckin
			Button bCheckin = (Button) view.findViewById(R.id.buttonCheckin);
			bCheckin.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if(!flagGuardado){
						Mensaje mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "error_empresa_no_guardada");

						try {
							mToast.controlMensajesToast();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}else{

						Mensaje mToast = null;
						LayoutInflater mInflater = getActivity().getLayoutInflater();

						String idEmpresa = mArgumentos.getString("idEmpresa");
						//Buscamos el id del checkin en la sesion del usuario para asignarle las coordenadas a la empresa
						String idCheckin = SesionUsuario.getIdCheckin(getActivity());

						CheckinSqliteDao checkinDao = new CheckinSqliteDao();
						Checkin checkin = checkinDao.buscarCheckin(getActivity(), idCheckin);

						HistoricoSqliteDao historicoDao = new HistoricoSqliteDao();
						Historico historico = historicoDao.buscarHistoricoPorCheckinPorEmpresa(getActivity(), idCheckin, idEmpresa);


						//verificamos si ya se registro un checkin en esta sesion
						if(historicoDao.existeCheckinVisita(getActivity(), idCheckin)){

							//registramos el check out
							SesionUsuario.cerrarSesion(getActivity());

							//y creamos una sesion nueva
							Usuario usu = new Usuario(SesionUsuario.getIdUsuario(getActivity()), "", "", SesionUsuario.getCorreo(getActivity()), SesionUsuario.getIdRol(getActivity()),"");
							Log.e("sesion nueva","nueva maldita seas");
							SesionUsuario.iniciarSesion(getActivity(), usu);

						}

						//Buscamos la empresa que le vamos asignar las coordenadas
						EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
						Empresa empresa  = empresaDao.buscarEmpresa(getActivity(),idEmpresa);
						empresa.setLatitud(checkin.getLatitud());
						empresa.setLongitud(checkin.getLongitud());

						boolean modificado = empresaDao.modificarEmpresa(getActivity(), empresa);



						if(modificado){
							mToast = new Mensaje(mInflater, getActivity(), "ok_checkin");

							//registramos la visita como historico
							idCheckin =  SesionUsuario.getIdCheckin(getActivity());
							historico = new Historico("visita", null , null, idEmpresa, idCheckin, SesionUsuario.getIdUsuario(getActivity()));
							historicoDao = new HistoricoSqliteDao();
							historicoDao.insertarHistorico(getActivity(), historico);

							try {
								mToast.controlMensajesToast();
							} catch (Exception e) {
								e.printStackTrace();
							}

							//y lo enviamos a la pagina de historicos
							HistoricosActivity fragment = new HistoricosActivity();

							getFragmentManager().beginTransaction()
							.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentHistoricos)
							.addToBackStack(AplicacionActivity.tagFragmentHistoricos)
							.commit();


						}else{
							mToast = new Mensaje(mInflater, getActivity(), "error_checkin");

							try {
								mToast.controlMensajesToast();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}
				}
			});

			// Registro del evento OnClick del buttonSalvar
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
						.addToBackStack(AplicacionActivity.tagFragmentServicios)
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
		tvErrorPermiso = (TextView) v.findViewById(R.id.textViewErrorPermiso);
		bSalvar = (Button)v.findViewById(R.id.buttonSalvar);
		bCopiar= (ImageButton)v.findViewById(R.id.imageButtonCopiar);
	}
	
	/**
	 * Funcion que se encarga de cargar los datos de un empleado en sus respectivos campos.
	 * 
	 * @param v View de la actividad.
	 * @param emplado Empleado cuya informacion se esta cargando.
	 */
	private void setModoNoEditable() {
		etNombre.setEnabled(false);
		etTelefono.setEnabled(false);
		etWeb.setEnabled(false);
		etRif.setEnabled(false);
		etDirFiscal.setEnabled(false);
		etDirComercial.setEnabled(false);
		tvErrorPermiso.setVisibility(View.VISIBLE);
		bSalvar.setEnabled(false);
		bCopiar.setEnabled(false);
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
			@SuppressWarnings("unused")
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

		String fechaModif = FormatoFecha.darFormatoDateTimeUS(new Date());

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

			String idUsuario = SesionUsuario.getIdUsuario(getActivity());

			if(id!=null){
				//Estamos modificando un registro
				Empresa empresa = new Empresa(id, nombre, telefono, rif, web, dirFiscal, dirComercial, fechaModif, idUsuario);
				Boolean modificado = empresaDao.modificarEmpresa(getActivity(), empresa);

				if(modificado){

					mToast = new Mensaje(mInflater, getActivity(), "ok_modificar_empresa");

				}else{
					mToast = new Mensaje(mInflater, getActivity(), "error_modificar_empresa");
				}
			}else{
				//Estamos creando un nuevo registro
				Empresa empresa = new Empresa(nombre, telefono, rif, web, dirFiscal, dirComercial, idUsuario, idUsuario);
				String idFilaInsertada = empresaDao.insertarEmpresa(getActivity(), empresa);

				if(idFilaInsertada.equals("-1") == false){
					mToast = new Mensaje(mInflater, getActivity(), "ok_ingreso_empresa");
					mArgumentos = new Bundle();
					mArgumentos.putString("idEmpresa", idFilaInsertada);
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

	/**
	 * Metodo que se llama al seleccionar el boton tareas
	 * @param idEmpleado
	 */
	public void onButtonTareaSelected(String nombreEmpresa) {

		TareasActivity fragment = new TareasActivity();

		if(nombreEmpresa!=null){
			Bundle arguments = new Bundle();
			arguments.putString("nombreEmpresa", nombreEmpresa);
			//pasamos al fragment el nombre de la empresa para hacer la busqueda
			fragment.setArguments(arguments); 
		}

		getFragmentManager().beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentTareas)
		.addToBackStack(AplicacionActivity.tagFragmentTareas)
		.commit();
	}

	/**
	 * Metodo que se llama al seleccionar el boton historicos
	 * @param idEmpleado
	 */
	public void onButtonHistoricoSelected(String nombreEmpresa) {

		HistoricosActivity fragment = new HistoricosActivity();

		if(nombreEmpresa!=null){
			Bundle arguments = new Bundle();
			arguments.putString("nombreEmpresa", nombreEmpresa);
			//pasamos al fragment el nombre de la empresa para hacer la busqueda
			fragment.setArguments(arguments); 
		}

		getFragmentManager().beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentHistoricos)
		.addToBackStack( AplicacionActivity.tagFragmentHistoricos)
		.commit();
	}

	/**
	 * Funcion que muestra mensaje de alerta cuando ya se realizo
	 * checkin para una empresa en una misma sesion
	 */
	public void mensajeAlertaCheckin(){

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		String[] mensArray = null;
		Mensaje mensajeDialog = new Mensaje("checkin_ya_realizado");
		String nombreMensaje = null;

		try {
			mensArray = mensajeDialog.controlMensajesDialog(nombreMensaje);
		} catch (Exception e) {
			e.printStackTrace();
		}

		alert.setMessage(mensArray[0]); 
		alert.setTitle(mensArray[1]); 

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

}
