package com.nahmens.rhcimax.controlador;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.utils.SesionUsuario;

public class LogoutActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_log_out, container, false);

		SesionUsuario.cerrarSesion(getActivity());

		return view;
	}


}
