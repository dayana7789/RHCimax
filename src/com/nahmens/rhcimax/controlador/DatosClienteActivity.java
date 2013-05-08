package com.nahmens.rhcimax.controlador;

import com.nahmens.rhcimax.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class DatosClienteActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
	
        View view = inflater.inflate(R.layout.activity_datos_cliente, container, false);
        
        //Utilizado para saber que fragment se esta ejecutando
        AplicacionActivity.layout_activo = AplicacionActivity.tagFragmentDatosCliente;
        return view;
    }
	
	

}
