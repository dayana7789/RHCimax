package com.nahmens.rhcimax.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	
	/**
	 * Funcion que calcula la fecha de hoy y suma o resta segun el 
	 * valor de cantidad.
	 * @param cantidad Cantidad a sumar o restar a la fecha
	 * @return
	 */
	public static String obtenerFecha(int cantidad) {
		//OJO: este es el formato que se necesita para poder
		//hacer la comparacion
		String myFormat = "yyyy-MM-dd"; 
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		Calendar myCalendar = Calendar.getInstance();

		myCalendar.add(Calendar.DATE, cantidad);

		return sdf.format(myCalendar.getTime());
	}

}
