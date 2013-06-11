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

public class TareasActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        
        View view = inflater.inflate(R.layout.activity_log_out, container, false);
        
        return view;
    }

}
