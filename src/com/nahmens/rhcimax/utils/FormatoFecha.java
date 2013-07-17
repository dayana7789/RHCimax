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
			//e.printStackTrace();
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
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
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
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
		Calendar myCalendar = Calendar.getInstance();

		myCalendar.add(Calendar.DATE, cantidad);

		return sdf.format(myCalendar.getTime());
	}
	
	/**
	 * Funcion que calcula la fecha de hoy y suma o resta segun el 
	 * valor de cantidad.
	 * @param cantidad Cantidad a sumar o restar a la fecha
	 * @return
	 */
	public static String obtenerFechaTiempoActual() {
		String myFormat = "dd-MM-yyyy HH:mm:ss"; 
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
		Calendar myCalendar = Calendar.getInstance();

		return sdf.format(myCalendar.getTime());
	}
	
	/**
	 * Funcion que compara dos fechas
	 * @param fecha1
	 * @param fecha2
	 * @return -1 si ha ocurrido algun error
	 *          0 si Fecha1 es igual a fecha2
	 *          1 si Fecha1 es menor a fecha2
	 *          2 si Fecha1 es mayor a fecha2
	 */
	public static int compararDates(String fecha1, String fecha2){

	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	    int resultado = -1;

	    try{
		    Date date1 = format.parse(fecha1);
		    Date date2 = format.parse(fecha2);
		    
		    if (date1.compareTo(date2) == 0) {
		    	resultado = 0;
		    }else if (date1.compareTo(date2) < 0){
		    	resultado = 1;
		    }else if (date1.compareTo(date2) > 0){
		    	resultado = 2;
		    }else{
		    	resultado = -1;
		    }
		    
	    }catch (Exception e) {
			e.printStackTrace();
		}

	    return resultado;
	}
	
	/**
	 * Funcion que compara dos fechas y tiempo
	 * @param fecha1
	 * @param fecha2
	 * @return -1 si ha ocurrido algun error
	 *          0 si Fecha1 es igual a fecha2
	 *          1 si Fecha1 es menor a fecha2
	 *          2 si Fecha1 es mayor a fecha2
	 */
	public static int compararDateTimes(String fecha1, String fecha2){

	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	    int resultado = -1;

	    try{
		    Date date1 = format.parse(fecha1);
		    Date date2 = format.parse(fecha2);
		    
		    if (date1.compareTo(date2) == 0) {
		    	resultado = 0;
		    }else if (date1.compareTo(date2) < 0){
		    	resultado = 1;
		    }else if (date1.compareTo(date2) > 0){
		    	resultado = 2;
		    }else{
		    	resultado = -1;
		    }
		    
	    }catch (Exception e) {
			e.printStackTrace();
		}

	    return resultado;
	}
	
	/**
	 * Funcion que dado un string en formato de fecha,
	 * lo transforma en un Date
	 *
	 * @param fecha
	 * @return
	 */
	public static Date stringToDateTime(String fecha) {
		String myFormat = "yyyy-MM-dd HH:mm:ss"; 
		SimpleDateFormat format = new SimpleDateFormat(myFormat, Locale.ENGLISH);
		Date date = null;
		
		try {
			date = format.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}
	
	public static Date getFechaPrimerDiaDelMes() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH),
				cal.getActualMinimum(Calendar.DAY_OF_MONTH),
				cal.getMinimum(Calendar.HOUR_OF_DAY),
				cal.getMinimum(Calendar.MINUTE),
				cal.getMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	public static Date getFechaUltimoDiaDelMes() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.getActualMaximum(Calendar.DAY_OF_MONTH),
				cal.getMaximum(Calendar.HOUR_OF_DAY),
				cal.getMaximum(Calendar.MINUTE),
				cal.getMaximum(Calendar.SECOND));
		return cal.getTime();
	}	
	
	public static Date getFechaPrimerDiaDeLaSemana() {
		Calendar cal = Calendar.getInstance();
		// Set the calendar to monday of the current week
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);            
		cal.set(Calendar.MINUTE, 0);                 
		cal.set(Calendar.SECOND, 0);          
		cal.set(Calendar.MILLISECOND, 0);   

		return cal.getTime();
	}

	public static Date getFechaUltimoDiaDeLaSemana() {
		Date primerDiaSemana = getFechaPrimerDiaDeLaSemana();

		Calendar c = Calendar.getInstance();
		c.setTime(primerDiaSemana);
		c.add(Calendar.DATE, 6);  
		
		return c.getTime();
	}	
	
	
	public static long getDiferenciaDias(Date fechaInicio, Date fechaFin){
		Calendar c = Calendar.getInstance();

	    Calendar cale = Calendar.getInstance();
	    cale.setTime(fechaInicio);
	  //  int year = cale.get(Calendar.YEAR);
	  //  int month = cale.get(Calendar.MONTH)+1;
	  //  int day = cale.get(Calendar.DAY_OF_MONTH);
	    
	    Calendar cale2 = Calendar.getInstance();
	    cale2.setTime(fechaFin);
	   // int yearS = cale2.get(Calendar.YEAR);
	   // int monthS = cale2.get(Calendar.MONTH)+1;
	   // int dayS = cale2.get(Calendar.DAY_OF_MONTH);
		 
		c.setTimeInMillis(fechaFin.getTime() - fechaInicio.getTime());

	//	Log.e("diferencia dias", " "  +c.get(Calendar.DAY_OF_YEAR));
		
		return c.get(Calendar.DAY_OF_YEAR);
		
	}

}
