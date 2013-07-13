package com.nahmens.rhcimax.mensaje;

import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nahmens.rhcimax.R;

public class Mensaje {

	private LayoutInflater inflater;
	private FragmentActivity contexto;
	private String tipoMensaje;
	public static final String ERROR_CAMPO_VACIO = "Este campo no puede estar vacio";
	public static final String ERROR_EMPRESA_NO_VALIDA = "Empresa no válida";
	public static final String ERROR_EMPLEADO_NO_VALIDO = "Contacto no válido";
	public static final String ERROR_EMAIL_INVALIDO =  "Email no válido";
	public static final String ERROR_TELF_INVALIDO =  "Número no válido";
	public static final String ERROR_CAMPO_OBLIGATORIO =  "Campo obligatorio";
	public static final String ERROR_CHECK_CORREO =  "Debe seleccionar al menos un correo";
	public static final String ERROR_CHECK_SERVICIO =  "Debe seleccionar al menos un servicio";
	

	/*
	 * Constructor para mensajes toast
	 * @param tipoMensaje Indica el cuál mensaje mostrar.
	 */
	public Mensaje(LayoutInflater inflater, FragmentActivity contexto,
			String tipoMensaje) {

		this.inflater = inflater;
		this.contexto = contexto;
		this.tipoMensaje = tipoMensaje;
	}

	/*
	 * Constructor para mensajes dialog
	 * @param tipoMensaje Indica el cuál mensaje mostrar.
	 */
	public Mensaje(String tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}


	/*
	 * Funcion encargada de mostrar los mensajes toast.
	 */
	public void controlMensajesToast() throws Exception{

		String mensaje = null;
		int layoutWhere = 0;

		if (tipoMensaje == "error_ingreso_empleado"){

			mensaje =  "Error: el contacto no pudo ser ingresado";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_ingreso_empleado"){

			mensaje =  "Contacto ingresado satisfactoriamente";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_ingreso_empresa"){

			mensaje =  "Error: la empresa no pudo ser ingresada";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_ingreso_empresa"){

			mensaje =  "Empresa ingresado satisfactoriamente";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_eliminado_empresa"){

			mensaje =  "Error: la empresa no pudo ser eliminada";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_eliminado_empresa"){

			mensaje =  "Empresa eliminada satisfactoriamente";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_eliminado_empleado"){

			mensaje =  "Error: el contacto no pudo ser eliminado";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_eliminado_empleado"){

			mensaje =  "Contacto eliminado satisfactoriamente";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "ok_modificar_empleado"){

			mensaje =  "Contacto modificado satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_modificar_empleado"){

			mensaje =  "Error: el contacto no pudo ser modificado";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_modificar_empresa"){

			mensaje =  "Empresa modificada satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_modificar_empresa"){

			mensaje =  "Error: la empresa no pudo ser modificada";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "error_general"){

			mensaje =  "Ha ocurrido un error inesperado. Inténtelo de nuevo.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_formulario"){

			mensaje =  "Verifique el formulario. Se han encontrado errores.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "error_empresa_no_existe"){

			mensaje =  "Empresa no existe. Verifique el nombre de la empresa ingresado.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "error_empresa_sin_empleados"){

			mensaje =  "La empresa debe poseer al menos un contacto para poder cotizar.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "error_empleado_no_existe"){

			mensaje =  "Contacto no registrado para la empresa indicada. Verifique el nombre del contacto ingresado.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_modificar_servidor"){

			mensaje =  "Nombre de servidor guardado.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_modificar_servidor"){

			mensaje =  "Error: el nombre de servidor no pudo ser modificado.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "error_sincronizado_empresa"){

			mensaje =  "Error: la empresa no pudo ser actualizada.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_sincronizado_empresa"){

			mensaje =  "Empresa actualizada satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_sincronizado_empleado"){

			mensaje =  "Error: el contacto no pudo ser actualizado.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_sincronizado_empleado"){

			mensaje =  "Contacto actualizado satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_sincronizado_tarea"){

			mensaje =  "Error: la tarea no pudo ser actualizada.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_sincronizado_tarea"){

			mensaje =  "Tarea actualizada satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_sincronizado_visita"){

			mensaje =  "Error: la visita no pudo ser actualizada.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_sincronizado_visita"){

			mensaje =  "Visita actualizada satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_sincronizado_cotizacion"){

			mensaje =  "Error: la cotización no pudo ser actualizada.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_sincronizado_cotizacion"){

			mensaje =  "Cotización actualizada satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_creacion_cotizacion"){

			mensaje =  "Error: la cotización no pudo crearse.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_creacion_cotizacion"){

			mensaje =  "Cotización creada satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_ingreso_tarea"){

			mensaje =  "Error: la tarea no pudo crearse.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_ingreso_tarea"){

			mensaje =  "Tarea creada satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "ok_eliminado_tarea"){

			mensaje =  "Tarea eliminada satisfactoriamente";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_eliminado_tarea"){

			mensaje =  "Error: la tarea no pudo ser eliminada";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_modificar_tarea"){

			mensaje =  "Tarea modificada satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_modificar_tarea"){

			mensaje =  "Error: la tarea no pudo ser modificada";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "error_empresa_no_guardada"){

			mensaje =  "Empresa no existe. Debe guardar los datos básicos de la empresa para poder hacer checkin.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_checkin"){

			mensaje =  "Checkin realizado satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_checkin"){

			mensaje =  "Error: no se pudo hacer chekin a la empresa indicada.";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else{
			throw new Exception("Mensaje invalido. Revisa el atributo tipoMensaje que utiliza el constructor de la clase Mensaje.");
		}

		View layout = inflater.inflate(layoutWhere,
				(ViewGroup) contexto.findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(mensaje);

		Toast toast = new Toast(contexto);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setView(layout);
		toast.show();
	}

	/*
	 * Funcion encargada de mostrar los mensajes dialog.
	 * @param args Argumentos que deseamos mostrar en el dialog.
	 * @return Mensaje a mostrar y titulo
	 */
	public String[] controlMensajesDialog(String args) throws Exception{
		String mensaje = null;
		String titulo = null;

		if (tipoMensaje == "eliminar_empresa"){
			
			mensaje =  "Está seguro de eliminar esta empresa: "+args+ "?. Todos los contactos asociados a la misma serán eliminados!.";
			titulo = "Eliminar Empresa";

		}else if(this.tipoMensaje == "eliminar_empleado"){
			
			mensaje =  "Está seguro de eliminar este contacto: "+args +"?.";
			titulo = "Eliminar contacto";

		}else if(this.tipoMensaje == "guardar_cambios_empresa"){
			
			mensaje =  "No puede agregar un contacto sin haber guardado la empresa. Desea guardar los cambios?.";
			titulo = "Guardar Cambios";

		}else if(this.tipoMensaje == "eliminar_tarea"){
			
			mensaje =  "Está seguro de eliminar esta tarea: "+args +"?.";
			titulo = "Eliminar Tarea";

		}else{
			throw new Exception("Mensaje invalido. Revisa el atributo tipoMensaje que utiliza el constructor de la clase Mensaje.");
		}
		
		return new String [] {mensaje, titulo};
	}

}
