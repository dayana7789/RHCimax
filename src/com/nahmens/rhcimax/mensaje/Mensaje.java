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
	public static final String ERROR_EMPRESA_NO_VALIDA = "Empresa no valida";

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

			mensaje =  "Error: el empleado no pudo ser ingresado";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_ingreso_empleado"){

			mensaje =  "Empleado ingresado satisfactoriamente";
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

			mensaje =  "Error: el empleado no pudo ser eliminado";
			layoutWhere = R.layout.toast_layout_mensaje_error;

		}else if(this.tipoMensaje == "ok_eliminado_empleado"){

			mensaje =  "Empleado eliminado satisfactoriamente";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "ok_modificar_empleado"){

			mensaje =  "Empleado modificado satisfactoriamente.";
			layoutWhere = R.layout.toast_layout_mensaje_ok;

		}else if(this.tipoMensaje == "error_modificar_empleado"){

			mensaje =  "Error: el empleado no pudo ser modificado";
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

		}else{
			throw new Exception("Mensaje invalido. Revisa el atributo tipoMensaje que utiliza el constructor de la clase Mensaje.");
		}

		View layout = inflater.inflate(layoutWhere,
				(ViewGroup) contexto.findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(mensaje);

		Toast toast = new Toast(contexto);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setView(layout);
		toast.show();
	}

	/*
	 * Funcion encargada de mostrar los mensajes dialog.
	 * @return Mensaje a mostrar y titulo
	 */
	public String[] controlMensajesDialog() throws Exception{
		String mensaje = null;
		String titulo = null;

		if (tipoMensaje == "eliminar_empresa"){
			
			mensaje =  "Está seguro de eliminar esta empresa?. Todos los empleados asociados a la misma serán eliminados!.";
			titulo = "Eliminar Empresa";

		}else if(this.tipoMensaje == "eliminar_empleado"){
			
			mensaje =  "Está seguro de eliminar este empleado?.";
			titulo = "Eliminar Empleado";

		}else if(this.tipoMensaje == "guardar_cambios_empresa"){
			
			mensaje =  "No puede agregar un empleado sin haber guardado la empresa. Desea guardar los cambios?.";
			titulo = "Guardar Cambios";

		}else{
			throw new Exception("Mensaje invalido. Revisa el atributo tipoMensaje que utiliza el constructor de la clase Mensaje.");
		}
		
		return new String [] {mensaje, titulo};
	}

}
