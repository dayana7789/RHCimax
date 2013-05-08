package com.nahmens.rhcimax.controlador;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.R.layout;
import com.nahmens.rhcimax.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class DatosEmpresaActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_datos_empresa, container, false);

		return view;
	}

}
