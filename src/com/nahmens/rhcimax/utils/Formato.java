package com.nahmens.rhcimax.utils;

import android.database.Cursor;

public class Formato {
	
	/**
	 * Funcion que construye un String con formato JSON a partir de
	 * un cursor.
	 * @param mCursor
	 * @return
	 */
	public String cursorToJsonString(Cursor mCursor){
		String mJsonString = "[";
		String columna = null;
		String contenido = null;
		int numColumnas = mCursor.getColumnNames().length;
		int numRegistros = mCursor.getCount();
		int i = 0;
			
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
		
		mJsonString += "]";
		
		return mJsonString;
	}

}
