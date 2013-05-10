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
	
	/*
	 * @param tipoMensaje Indica el cuál mensaje mostrar.
	 */
	public Mensaje(LayoutInflater inflater, FragmentActivity contexto,
			String tipoMensaje) {

		this.inflater = inflater;
		this.contexto = contexto;
		this.tipoMensaje = tipoMensaje;
	}


	/*
	 * Funcion encargada de mostrar los mensajes.
	 */
	public void controlMensajes(){
		
		String mensaje = null;
		int layoutWhere = 0;
		
		if (tipoMensaje == "error_ingreso_empleado"){
			mensaje =  "Error: el empleado no pudo ser ingresado";
			layoutWhere = R.layout.toast_layout_mensaje_error;
			
		}else if(this.tipoMensaje == "ok_ingreso_empleado"){
			mensaje =  "Empleado ingresado satisfactoriamente";
			layoutWhere = R.layout.toast_layout_mensaje_ok;
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

}
