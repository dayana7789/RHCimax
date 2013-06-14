package com.nahmens.rhcimax.utils;

import android.content.Context;  
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/*
 * Clase utilizada para mostrar autocomplete
 * AutoCompleteTextView show suggestions when no text entered
 */
public class InstantAutoComplete extends AutoCompleteTextView {

	public InstantAutoComplete(Context context) {
		super(context);
	}

	public InstantAutoComplete(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	/*
	 * En esta funcion se retorna siempre true, para que se despliegue la
	 * lista sin importar que se haya tipeado algo en el campo o no.
	 */
	@Override
	public boolean enoughToFilter() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);

		try{
			if (focused) {
				performFiltering(getText(), 0);
			}
		}catch (Exception e) {
			// TODO: ignorar
		}
	}

}