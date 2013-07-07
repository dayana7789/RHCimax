package com.nahmens.rhcimax.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatoFecha {
	
	/**
	 * Funcion que transforma una fecha y tiempo gringa en una espanhola
	 * @param fecha que queremos dar formato
	 * @return fecha con formato espanhol
	 */
	public static String darFormatoDateTimeES(String fecha){

		Date v_date=null;
		try {
			v_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat formatter = null;

		formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
		return formatter.format(v_date);
		
	}
	
	/**
	 * Funcion que transforma una fecha gringa en una espanhola
	 * @param fecha
	 * @return fecha con formato espanhol
	 */
	public static String darFormatoDateES(String fecha){

		Date v_date=null;
		try {
			v_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat formatter = null;

		formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		return formatter.format(v_date);
		
	}
	
	
	/**
	 * Funcion que da formato gringo a una fecha.
	 * @param date
	 * @return fecha con formato gringo
	 */
	public static String darFormatoDateTimeUS(Date date){
		String myFormat = "yyyy-MM-dd HH:mm:ss"; 
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		String fecha = sdf.format(date);
		
		return fecha;
	}
	
	
	/**
	 * Funcion que transforma una fecha espanhola a una gringa.
	 * @param fecha 
	 * @return  con formato gringo
	 */
	public static String darFormatoDateUS(String fecha){

		Date v_date=null;
		try {
			v_date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat formatter = null;

		formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		return formatter.format(v_date);
		
	}
	
	/**
	 * Funcion que da formato gringo a una fecha.
	 * @param date
	 * @return
	 */
	public static String darFormatoDateUS(Date date){
		String myFormat = "yyyy-MM-dd"; 
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		String fecha = sdf.format(date);
		
		return fecha;
	}

}
