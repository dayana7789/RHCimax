package com.nahmens.rhcimax.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;

/**
 * Adaptador utilizado para mostrar lista de autocomplete de nombres 
 * de empresas haciendo uso de cursores.
 * 
 * Para poder usarlo se debe usar los ids:
 * (EditText) mView.findViewById(R.id.textEditHiddenIdEmpleado); 
 * (AutoCompleteTextView) mView.findViewById(R.id.autocompleteEmpleado);
 */
public class AutocompleteEmpleadoCursorAdapter extends CursorAdapter implements android.widget.AdapterView.OnItemClickListener {
	private Context contexto;
	private EditText etIdEmpleado;
	private EditText etIdEmpresa;


	/**
	 * Constructor. Notar que no se necesita el cursor cuando se crea el 
	 * adaptador. En cambio, los cursores son creados a medida que completaciones
	 * son necesitadas por el campo.
	 */
	public AutocompleteEmpleadoCursorAdapter(View mView){
		//llamamos a super con cursor en null
		super(mView.getContext(), null, 0);
		this.contexto = mView.getContext();
		this.etIdEmpleado = (EditText) mView.findViewById(R.id.textEditHiddenIdEmpleado); 
		this.etIdEmpresa = (EditText) mView.findViewById(R.id.textEditHiddenIdEmpresa); 
	}

	/**
	 * Called by the ListView for the AutoCompleteTextView field to display
	 * the text for a particular choice in the list.
	 *
	 * @param view
	 *            The TextView used by the ListView to display a particular
	 *            choice.
	 * @param context
	 *            The context (Activity) to which this form belongs;
	 * @param cursor
	 *            The cursor for the list of choices, positioned to a
	 *            particular row.
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor){
		String item = createItem(cursor);    
		((TextView) view).setText(item);      
	}

	/**
	 * Called by the AutoCompleteTextView field to display the text for a
	 * particular choice in the list.
	 *
	 * @param context
	 *            The context (Activity) to which this form belongs;
	 * @param cursor
	 *            The cursor for the list of choices, positioned to a
	 *            particular row.
	 * @param parent
	 *            The ListView that contains the list of choices.
	 *
	 * @return A new View (really, a TextView) to hold a particular choice.
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent){
		final LayoutInflater inflater = LayoutInflater.from(context);
		final TextView view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		return view;
	}

	/**
	 * Invoked by the AutoCompleteTextView field to get completions for the
	 * current input.
	 *
	 * NOTE: If this method either throws an exception or returns null, the
	 * Filter class that invokes it will log an error with the traceback,
	 * but otherwise ignore the problem. No choice list will be displayed.
	 * Watch those error logs!
	 *
	 * @param constraint
	 *            The input entered thus far. The resulting query will
	 *            search for Items whose description begins with this string.
	 * @return A Cursor that is positioned to the first row (if one exists)
	 *         and managed by the activity.
	 */
	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint){
		Cursor currentCursor = null;

		if (getFilterQueryProvider() != null){
			return getFilterQueryProvider().runQuery(constraint);
		}
		
		
		String args = "";

		if (constraint != null){
			args = constraint.toString();       
		}


		EmpleadoSqliteDao empleadoDao =  new EmpleadoSqliteDao();
		String idEmpresa = etIdEmpresa.getText().toString();

		//Nos aseguramos que idEmpresa no sea vacio
		if((idEmpresa.equals("")==false)){
			currentCursor = empleadoDao.listarEmpleadosPorEmpresaPorArgs(contexto, idEmpresa, args);
		}

		return currentCursor;
	}

	
	private String createItem(Cursor cursor){
		String item = cursor.getString(cursor.getColumnIndex(Empleado.NOMBRE))+" " +cursor.getString(cursor.getColumnIndex(Empleado.APELLIDO));
		return item;
	}


	/**
	 * Called by the AutoCompleteTextView field to get the text that will be
	 * entered in the field after a choice has been made.
	 *
	 * @param Cursor
	 *            The cursor, positioned to a particular row in the list.
	 * @return A String representing the row's text value. (Note that this
	 *         specializes the base class return value for this method,
	 *         which is {@link CharSequence}.)
	 */
	@Override 
	public	String convertToString(Cursor cursor) { 
		return cursor.getString(cursor.getColumnIndex(Empleado.NOMBRE))+" " +cursor.getString(cursor.getColumnIndex(Empleado.APELLIDO)); 
	}


	/**
	 * Called by the AutoCompleteTextView field when a choice has been made
	 * by the user. Aqui actualizamos el valor del text edit escondido que
	 * almacena el id de la empresa.
	 *
	 * @param listView
	 *            The ListView containing the choices that were displayed to
	 *            the user.
	 * @param view
	 *            The field representing the selected choice
	 * @param position
	 *            The position of the choice within the list (0-based)
	 * @param id
	 *            The id of the row that was chosen (as provided by the _id
	 *            column in the cursor.)
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

		// Get the cursor, positioned to the corresponding row in the result set
		Cursor cursor = (Cursor) listView.getItemAtPosition(position);

		// Obtenemos el id del empleado
		String idEmpleado = cursor.getString(0);

		// Update the parent class's TextView
		etIdEmpleado.setText(""+idEmpleado);
	}

}

