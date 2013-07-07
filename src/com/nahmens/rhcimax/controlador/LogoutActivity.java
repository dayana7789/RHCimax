package com.nahmens.rhcimax.controlador;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.sqliteDAO.CheckinSqliteDao;
import com.nahmens.rhcimax.utils.FormatoFecha;

public class LogoutActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_log_out, container, false);

		logout(getActivity());

		return view;
	}

	public static void logout(Context contexto) {
		String fecha = FormatoFecha.darFormatoDateTimeUS(new Date());

		SharedPreferences prefs = contexto.getSharedPreferences("Usuario",Context.MODE_PRIVATE);
		String idCheckin = prefs.getString("idCheckin", ""); 

		CheckinSqliteDao checkinDao = new CheckinSqliteDao();
		Checkin checkin = checkinDao.buscarCheckin(contexto, idCheckin);
		checkin.setCheckout(fecha);

		checkinDao.modificarCheckin(contexto, checkin);

	}

}
