package com.nahmens.rhcimax.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
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

	public JSONObject postValores (String strUrl, String strJsonArray) throws Exception{
		URL url = new URL(strUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		JSONObject jsonObject = null;
		
		Log.e("resultado",""+strJsonArray);


		OutputStream os = conn.getOutputStream();
		os.write(strJsonArray.getBytes());
		os.flush();
		
		conn.connect();

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
			Log.e("salida",output);
			sb.append(output);
			
		}
		
		Log.e("salida",sb.toString());
		jsonObject = new JSONObject(sb.toString());

		conn.disconnect();
		
		return jsonObject;

	}

}


