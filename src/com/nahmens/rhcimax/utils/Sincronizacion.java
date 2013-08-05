package com.nahmens.rhcimax.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Sincronizacion{

	Context contexto;

	public Sincronizacion(Context contexto) {
		this.contexto = contexto;
	}

	public JSONArray getValores(String strUrl) throws Exception{
		JSONArray jsonArray = null; 
		URL url = new URL(strUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		StringBuilder sb = new StringBuilder();

		Log.e("Sync","Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
		Log.e("salida",sb.toString());

		jsonArray = new JSONArray(sb.toString());

		conn.disconnect();

		return jsonArray;
	}

	public JSONObject postValores (String strUrl, String strJsonArray, String fechaSync) throws Exception{
		JSONObject jsonObj = null;
		URL url = new URL(strUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("token", SesionUsuario.getToken(contexto));
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		strJsonArray = "data=" + strJsonArray;

		if(fechaSync!=null){
			strJsonArray = strJsonArray + "&fechaUltSync=" + fechaSync;
		}

		Log.e("info. enviar",""+strJsonArray);

		OutputStream os = conn.getOutputStream();
		os.write(strJsonArray.getBytes());
		os.flush();

		conn.connect();
		Log.e("conn.getResponseCode()","conn.getResponseCode()?"+conn.getResponseCode());
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		StringBuilder sb = new StringBuilder();

		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}

		
		jsonObj = new JSONObject(sb.toString());
		Log.e("salida",jsonObj.toString());

		//Log.e("error","error?"+conn.getErrorStream());
		conn.disconnect();

		return jsonObj;

	}

	/**
	 * Funcion que guarda en archivo de shared preferences las fechas de sincronizacion
	 * de cada tabla de BD.
	 * @param context
	 * @param fechas JsonArray de la forma [{nombreTabla_0: fecha_0},...,{nombreTabla_n: fecha_n}]
	 */
	public void setFechaSincronizacion(String nombreTabla) {

		SharedPreferences prefs = contexto.getSharedPreferences("Sincronizacion",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(nombreTabla, FormatoFecha.obtenerFechaTiempoActualEN());

		editor.commit();
	}

	public String getFechaSincronizacion(String nombreTabla){

		SharedPreferences prefs = contexto.getSharedPreferences("Sincronizacion",Context.MODE_PRIVATE);
		return prefs.getString(nombreTabla, null); 

	}

}


