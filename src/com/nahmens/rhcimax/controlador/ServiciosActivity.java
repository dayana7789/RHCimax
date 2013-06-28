package com.nahmens.rhcimax.controlador;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaCorreosCursorAdapter;
import com.nahmens.rhcimax.adapters.ListaServiciosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Historico;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.CotizacionSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.Cotizacion_ServicioSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.Empleado_CotizacionSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.HistoricoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.ServicioSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.Tripleta;

public class ServiciosActivity extends Fragment {

	private FragmentManager fragmentManager; 
	private LayoutInflater inflater;

	//Campos formulario
	TextView tvEmpresa;
	Button buttonFinalizar;
	TableLayout tlListCorreos;
	TableLayout tlListServicios;
	EditText etMensual;
	EditText etTotal;
	TableRow trTotal;
	Button buttonCalcular;
	EditText etDescripcion;


	/**
	 * Funcion que almacena la referencia de los campos de tal manera que estos
	 * sean calculados una sola vez.
	 * @param view
	 */
	private void setReferenciaCampos(View v) {
		tlListCorreos = (TableLayout) v.findViewById(R.id.tableLayoutListaCorreos);
		tlListServicios = (TableLayout) v.findViewById (R.id.tableLayoutListaServicios);
		tvEmpresa = (TextView) v.findViewById(R.id.textViewEmpresa);
		buttonFinalizar= (Button)  v.findViewById(R.id.buttonFinalizar);
		etMensual = (EditText) v.findViewById(R.id.textEditMensual);
		etTotal = (EditText) v.findViewById(R.id.textEditTotal);
		trTotal = (TableRow) v.findViewById(R.id.tableRowTotal);
		buttonCalcular= (Button)  v.findViewById(R.id.buttonCalcular);
		etDescripcion = (EditText) v.findViewById(R.id.textEditDescripcion);
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
		//tab es el correspondiente.
		AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentClientes);	
		
		View view = inflater.inflate(R.layout.activity_servicios, container, false);
		
		//OJO: evitamos que la pantalla se vuelva a recrear verificando el valor
		//de savedInstanceState. De esta manera evitamos la doble llamada que se
		//realiza al metodo onCreateView, cuando cambiamos la orientacion del 
		//dispositivo.
		if (savedInstanceState==null){

			this.inflater=inflater;
			this.fragmentManager = this.getFragmentManager();
			
			setReferenciaCampos(view);

			Bundle mArgumentos = this.getArguments();

			//Nos aseguramos que nos hayan pasado argumentos
			if(mArgumentos!= null){
				String idEmpresa = mArgumentos.getString("idEmpresa");

				if(idEmpresa != null){
					llenarCampos(idEmpresa, "empresa");
				}else{
					String idEmpleado = mArgumentos.getString("id");
					if(idEmpleado != null){
						llenarCampos(idEmpleado, "empleado");
					}
				}
			}
		}

		return view;
	}


	/**
	 * Funcion que llena la informacion de servicios y clientes.
	 * @param id Se refiere al idEmpresa o al idEmpleado segun el tipoCliente.
	 * @param tipoCliente Posibles valores: empleado o empresa.
	 */

	private void llenarCampos(String id, String tipoCliente){

		if(tipoCliente.equals("empresa")){
			llenarCamposCliente(id, null);

		}else if(tipoCliente.equals("empleado")){
			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			Empleado empleado = empleadoDao.buscarEmpleado(getActivity(), id);

			llenarCamposCliente(""+empleado.getIdEmpresa(), id);

			id = ""+empleado.getIdEmpresa();

		}else{
			Log.e("ServiciosActivity: llenarCampos(..)", "Tipo de cliente no valido: " + tipoCliente);
		}

		llenarCamposServicios(id,tipoCliente);
	}

	/**
	 * Funcion que llena en pantalla los datos relacionados a los servicios:
	 * lista de servicios.
	 * 
	 * @param idEmpresa Identificador de la empresa
	 * @param tipoCliente Posibles valores: empresa o empleado
	 */
	private void llenarCamposServicios(final String idEmpresa, String tipoCliente) {
		ServicioSqliteDao servicioDao = new ServicioSqliteDao();
		Cursor mCursorServicios = servicioDao.listarServicios(getActivity());

		//Lista de servicios
		if(mCursorServicios.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Servicio.NOMBRE};
			int[] to = new int[] { R.id.checkBoxServicio};

			//Creamos un array adapter para desplegar cada una de las filas
			ListaServiciosCursorAdapter notes = new ListaServiciosCursorAdapter(tlListServicios, getActivity(), R.layout.activity_fila_servicio, mCursorServicios, from, to, 0, buttonFinalizar);

			buttonCalcular.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					HashMap<Integer, Tripleta> servSeleccionados = ListaServiciosCursorAdapter.getServiciosSeleccionados();
					double total = 0;
					double mensual = 0;
					Tripleta par = null;
					double medida = 0;

					for (Map.Entry<Integer, Tripleta> entry : servSeleccionados.entrySet()) {
						par = entry.getValue();

						if(par.getBooleano()){
							int idServicio = entry.getKey();
							ServicioSqliteDao servicioDao = new ServicioSqliteDao();
							Servicio servicio = servicioDao.buscarServicio(v.getContext(), ""+idServicio);

							if(!par.getMedida().equals("")){
								medida = Double.parseDouble(par.getMedida());
							}else{
								medida = 0;
							}

							if(servicio.getUnidadMedicion().equals("ninguno")){

								//	if(servicio.getPrecio() != 0){
								mensual = mensual + servicio.getPrecio();
								//		}else{
								total = total + servicio.getInicial();
								//		}

							}else{
								mensual = mensual + (medida*servicio.getPrecio());
								total = total + (medida*servicio.getInicial());
							}

						}
					}

					total = total + mensual;

					etMensual.setText(""+mensual);
					etTotal.setText(""+total);

					etTotal.requestFocus();

				}});


			buttonFinalizar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){

					Mensaje mToast = null;

					//Obtenemos al usuario
					SharedPreferences prefs = getActivity().getSharedPreferences("Usuario",Context.MODE_PRIVATE);
					int idUsuario = prefs.getInt(Usuario.ID, 0);

					//creamos una cotizacion
					CotizacionSqliteDao cotizacionDao = new CotizacionSqliteDao();
					long idCotizacion = cotizacionDao.insertarCotizacion(getActivity(), ""+idUsuario, ""+idEmpresa, etDescripcion.getText().toString());

					//creamos un registro en la tabla empleadoCotizacion
					boolean hayErrorResultEC = crearEmpleadoCotizacion(idCotizacion);

					if(hayErrorResultEC){
						mToast = new Mensaje(inflater, getActivity(), "error_creacion_cotizacion");
						try {
							mToast.controlMensajesToast();
						} catch (Exception e) {
							e.printStackTrace();
						}

						//Si ocurrio un error, eliminamos la cotizacion que recien agregamos
						//para evitar que haya inconsistencias en la BD
						cotizacionDao.eliminarCotizacion(getActivity(), ""+idCotizacion);

					}else{

						//creamos un registro en la tabla cotizacionServicio
						boolean hayErrorResultCS = crearCotizacionServicio(idCotizacion);

						if(hayErrorResultCS){
							mToast = new Mensaje(inflater, getActivity(), "error_creacion_cotizacion");
							try {
								mToast.controlMensajesToast();
							} catch (Exception e) {
								e.printStackTrace();
							}

							//Si ocurrio un error, eliminamos la cotizacion que recien agregamos
							//para evitar que haya inconsistencias en la BD
							cotizacionDao.eliminarCotizacion(getActivity(), ""+idCotizacion);

							//finalmente.. si no errores de ningun tipo..
						}else{
							
							//creamos un historico de la cotizacion
							int idCot = (int)idCotizacion;
							Historico historico = new Historico("cotizacion", idCot , 0, 0);
							HistoricoSqliteDao historicoDao = new HistoricoSqliteDao();
							historicoDao.insertarHistorico(getActivity(), historico);

							mToast = new Mensaje(inflater, getActivity(), "ok_creacion_cotizacion");
							try {
								mToast.controlMensajesToast();
							} catch (Exception e) {
								e.printStackTrace();
							}

							//Redirigimos la pantalla a FragmentClientes
							cambiarFragmento();

							//seteamos la lista de correos seleccionados a null de la vista servicios
							ListaCorreosCursorAdapter.setCorreosSeleccionados(null);

							//seteamos la lista de servicios seleccionados a null de la vista servicios
							ListaServiciosCursorAdapter.setServiciosSeleccionados(null);
						}
					}
				}


				/**
				 * Funcion que carga el fragmento FragmentClientes
				 */
				private void cambiarFragmento() {
					ClientesActivity fragment = new ClientesActivity();

					Bundle mArgumentos =  new Bundle();
					mArgumentos.putString(AplicacionActivity.tagCuadroColor, AplicacionActivity.tagRojo);

					//pasamos al fragment la notificacion de cambio de color
					//del cuadro de color
					fragment.setArguments(mArgumentos); 

					fragmentManager.beginTransaction()
					.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentClientes)
					.commit();
				}

				/**
				 * Funcion que itera sobre la estructura correosSeleccionados y crea nuevos 
				 * registros en la tabla EmpleadoCotizacion.
				 * @param idCotizacion
				 * @return true si todo se ha ingresado correctamente a la BD.
				 *         false caso contrario.
				 */
				private boolean crearEmpleadoCotizacion(long idCotizacion) {
					HashMap<Integer, Boolean> correosSeleccionados = ListaCorreosCursorAdapter.getCorreosSeleccionados();
					boolean bool = false;
					int idEmpleado = 0;
					long idEmplCot = 0;
					boolean error = false;
					Empleado_CotizacionSqliteDao emplCotDao = new Empleado_CotizacionSqliteDao();

					for (Map.Entry<Integer, Boolean> entry : correosSeleccionados.entrySet()) {
						bool = entry.getValue();

						//si este correo fue seleccionado..
						if(bool){
							idEmpleado = entry.getKey();

							idEmplCot = emplCotDao.insertar(getActivity(), ""+idEmpleado, ""+idCotizacion);

							if(idEmplCot ==-1){
								error = true;
								Log.e("ServiciosActivity: onclick button finalizar", "Problema al insertar empleado_cotizacion en BD: idEmpleado: " + idEmpleado + " idCotizacion: " + idCotizacion);
							}
						}
					}
					return error;
				}

				/**
				 * Funcion que itera sobre la estructura servSeleccionados y crea nuevos 
				 * registros en la tabla CotizacionServicio.
				 * @param idCotizacion
				 * @return true si todo se ha ingresado correctamente a la BD.
				 *         false caso contrario.
				 */
				private boolean crearCotizacionServicio(long idCotizacion) {

					HashMap<Integer, Tripleta> servSeleccionados = ListaServiciosCursorAdapter.getServiciosSeleccionados();
					Tripleta par = null;
					int idServicio = 0;
					String medida = null;
					long idCotServ = 0;
					Cotizacion_ServicioSqliteDao cotServDao = new Cotizacion_ServicioSqliteDao();
					boolean error = false;

					for (Map.Entry<Integer, Tripleta> entry : servSeleccionados.entrySet()) {
						par = entry.getValue();

						//si este servicio fue seleccionado..
						if(par.getBooleano()){
							idServicio = entry.getKey();
							medida = par.getMedida();
							idCotServ = cotServDao.insertar(getActivity(), ""+idServicio, ""+idCotizacion, medida);

							if(idCotServ ==-1){
								error = true;
								Log.e("ServiciosActivity: onclick button finalizar", "Problema al insertar cotizacion_servicio en BD: idServicio:" + idServicio + " idCotizacion:" + idCotizacion);
							}
						}
					}
					return error;
				}
			});
		}
	}

	/**
	 * Funcion que llena en pantalla los datos relacionados al cliente:
	 * lista de correos de todos los empleados.
	 * 
	 * @param idEmpresa Identificador de la empresa
	 * @param idEmpleado Identificador del empleado. 
	 *                   Si idEmpleado==null, se marcan todos los correos (checkbox).
	 *                   Si no, se marca solo el correo de idEmpleado.
	 */
	private void llenarCamposCliente(String idEmpresa, String idEmpleado) {
		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Empresa empresa  = empresaDao.buscarEmpresa(getActivity(),idEmpresa);
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Cursor mCursorEmpleados = null;
		
		if(empresa !=null){
			tvEmpresa.setText(empresa.getNombre());
			mCursorEmpleados = empleadoDao.listarEmpleadosPorEmpresa(getActivity(), idEmpresa);
		}else{
			tvEmpresa.setText("");
			mCursorEmpleados = empleadoDao.buscarEmpleadoCursor(getActivity(), idEmpleado);
		}

		//Lista de correos de empleados
		if(mCursorEmpleados.getCount()>0){

			String[] from = new String[] { Empleado.NOMBRE, Empleado.APELLIDO, Empleado.EMAIL};
			int[] to = new int[] { R.id.textViewServNombreEmp,R.id.textViewServApellidoEmp, R.id.checkBoxServEmail};

			//Creamos un adaptador personalizado para desplegar cada una de las filas
			ListaCorreosCursorAdapter notes = null;

			if(idEmpleado==null){
				notes = new ListaCorreosCursorAdapter(tlListCorreos, getActivity(), R.layout.activity_fila_correo, mCursorEmpleados, from, to, 0, null,buttonFinalizar);
			}else{
				notes = new ListaCorreosCursorAdapter(tlListCorreos, getActivity(), R.layout.activity_fila_correo, mCursorEmpleados, from, to, 0, idEmpleado, buttonFinalizar);
			}
		}
	}
}
