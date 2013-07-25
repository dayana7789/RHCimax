package com.nahmens.rhcimax.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.database.Cursor;

public class Utils {
	
	/**
	 * Funcion que construye un String con formato JSON a partir de
	 * un cursor.
	 * @param mCursor
	 * @param esArray Indica si queremos formar un JSONArray o un JSONObject.
	 * @return
	 */
	public String cursorToJsonString(Cursor mCursor, boolean esArray){
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
				contenido =  "\""+mCursor.getString(j)+"\"";
				
				
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
