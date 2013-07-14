package com.nahmens.rhcimax.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.nahmens.rhcimax.controlador.LoginActivity;

public class Sincronizacion extends AsyncTask<String, Float, String> {

	Context contexto;
	
	final CharSequence TEXT_ERROR = "Ha ocurrido un error con la sincronización: ";
	final CharSequence TEXT_OK = "Sincronización finalizada";
	final int DURATION = Toast.LENGTH_LONG;

	public Sincronizacion(Context contexto) {
		super();
		this.contexto = contexto;
	}

	public void getValores() throws Exception{

		try {

			URL url = new URL("http://190.203.108.202:8080/vasaweb-1.0/getTest");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
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
				Log.e("Sync",output);
			}
			Log.e("salida",sb.toString());

			//JSONObject jObject = new JSONObject(sb.toString());

			JSONArray userArray = new JSONArray(sb.toString());
			String uid = null;
			for (int i = 0; i < userArray.length(); ++i) {
				JSONObject userObject = userArray.getJSONObject(i);
				uid = userObject.getString("asset_name");
				Log.e("result", ""+uid);

			}

			conn.disconnect();

		} catch (MalformedURLException e) {
			Toast toast = Toast.makeText(contexto, TEXT_ERROR + e.toString(), DURATION);
			toast.show();
			e.printStackTrace();

		} catch (IOException e) {
			Toast toast = Toast.makeText(contexto, TEXT_ERROR + e.toString(), DURATION);
			toast.show();
			e.printStackTrace();

		} catch (JSONException e) {
			Toast toast = Toast.makeText(contexto, TEXT_ERROR + e.toString(), DURATION);
			toast.show();
			e.printStackTrace();
		}

	}

	public void postValores () throws Exception{

		try {

			URL url = new URL("http://190.203.108.202:8080/vasaweb-1.0/createTest");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String input ="{\"assetId\":\"1234\", \"name\":\"dayana\"}";
			Log.e("resukt",""+input);
			//String input = "{\"qty\":100,\"name\":\"iPad 4\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;

			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				Log.e("sync post",output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {
			Toast toast = Toast.makeText(contexto, TEXT_ERROR + e.toString(), DURATION);
			toast.show();
			e.printStackTrace();

		} catch (IOException e) {
			Toast toast = Toast.makeText(contexto, TEXT_ERROR + e.toString(), DURATION);
			toast.show();
			e.printStackTrace();

		}

	}

	protected void onPreExecute() {
		LoginActivity.dialog.show(); //Mostramos el diálogo antes de comenzar
	}



	protected void onPostExecute(String result) {
		LoginActivity.dialog.dismiss(); //lo ocultamos al terminar
		
		if(result.equals("OK")){

			Toast toast = Toast.makeText(contexto, TEXT_OK, DURATION);
			toast.show();
		}else{

			Toast toast = Toast.makeText(contexto, TEXT_ERROR + result, DURATION);
			toast.show();
		}

	}


	protected String doInBackground(String... params) {

		try {
			this.postValores();
			
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		
		return "OK";
	}

}


