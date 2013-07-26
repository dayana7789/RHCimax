package com.nahmens.rhcimax.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Cotizacion;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.database.modelo.Tarea;

public class Utils {
	
	/**
	 * Funcion que construye un String con formato JSON a partir de
	 * un cursor.
	 * @param mCursor
	 * @param esArray Indica si queremos formar un JSONArray o un JSONObject.
	 * @return
	 */
	public String cursorToJsonString(Cursor mCursor, boolean esArray){
		
		//Cursor no posee un metodo para obtener el tipo de dato de la columna
		//en los APIS viejos de android. Es por esto que segun el nombre de
		//la columna, obtenemos el tipo de dato.
		ArrayList<String> doubles = new ArrayList<String>();
		doubles.add(Empresa.LATITUD);
		doubles.add(Empresa.LONGITUD);
		doubles.add(Servicio.PRECIO);
		doubles.add(Servicio.INICIAL);

		ArrayList<String> enteros = new ArrayList<String>();
		enteros.add(Empresa.MODIFICADO);
		enteros.add(Empresa.SINCRONIZADO);
		enteros.add(Cotizacion.ENVIADO);
		enteros.add(Cotizacion.RECIBIDO);
		enteros.add(Tarea.FECHA_FINALIZACION);
		enteros.add(Cotizacion.NUM_COTIZACION);
		
		String mJsonString = null;
		String columna = null;
		String contenido = null;
		int numColumnas = mCursor.getColumnNames().length;
		int numRegistros = mCursor.getCount();
		int i = 0;
			
		if(esArray){
			mJsonString = "[";
		}else{
			mJsonString = "";
		}
		
		while (!mCursor.isAfterLast()) {
			
			mJsonString += "{";
			
			for(int j=0; j<numColumnas; j++){
				columna = "\""+mCursor.getColumnName(j)+"\"";
				
				if(doubles.contains(mCursor.getColumnName(j))){
					contenido =  ""+mCursor.getDouble(j);
					
				}else if(enteros.contains(mCursor.getColumnName(j))){
					contenido =  ""+mCursor.getInt(j);
					
				}else{
					contenido =  "\""+mCursor.getString(j)+"\"";
				}

				if(j==numColumnas-1){
					mJsonString += columna + ":" + contenido;
				}else{
					mJsonString += columna + ":" + contenido + ",";
				}
			}

			if(i==numRegistros-1){
				mJsonString += "}";
			}else{
				mJsonString += "},";
			}

			mCursor.moveToNext();
			i++;

		}
		
		if(esArray){
			mJsonString += "]";
		}else{
			mJsonString += "";
		}
		
		
		return mJsonString;
	}
	
	public String getNumeroAleatorio(){
		
		String numAleatorio = null;
		Random rand = new Random();
		int min=100; 
		int max=999;
		
		String myFormat = "yyyyMMddHHmmss"; 
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
		String fecha = sdf.format(new Date());

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt(max - min + 1) + min;
		numAleatorio = fecha + randomNum;

		return numAleatorio;
		
	}

}
