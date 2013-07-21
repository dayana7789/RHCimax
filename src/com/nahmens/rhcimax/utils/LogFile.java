package com.nahmens.rhcimax.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;


public class LogFile {

	String nombreArchivo;
	Context context;


	public LogFile(Context context,String nombreArchivo) {
		this.context = context;
		this.nombreArchivo ="log_"+nombreArchivo+".txt";

	}

	public void appendLog(String text){       

		/*File logFile = new File(context.getExternalFilesDir(null), nombreArchivo);


		if (!logFile.exists()){
			try{
				logFile.createNewFile();
			} 
			catch (IOException e){
				e.printStackTrace();
			}
		}
		try{
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
			buf.append(text);
			buf.newLine();
			buf.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}*/
	}

}
