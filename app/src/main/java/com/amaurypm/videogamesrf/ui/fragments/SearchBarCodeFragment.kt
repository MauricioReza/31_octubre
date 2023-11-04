package com.amaurypm.videogamesrf.ui.fragments

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.amaurypm.videogamesrf.R

class SearchBarCodeFragment : Fragment() {

    private lateinit var editText: String
    private lateinit var errorMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar los argumentos configurados en MyDialogFragment
        val args = arguments
        if (args != null) {
            editText = args.getString("editText", "")
            errorMessage = args.getString("errorMessage", "")
        } else {
            // Si no se proporcionan argumentos, puedes asignar un valor por defecto o manejar esta situación de otra manera.
            editText = ""
            errorMessage = ""
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_search_bar_code, container, false)

        // Acceder a las vistas en el diseño del fragmento
        val editText = view.findViewById<EditText>(R.id.editText)
        val errorMessageTextView = view.findViewById<TextView>(R.id.errorMessage)

        // Aplicar el filtro de entrada a editText
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val builder = StringBuilder(dest)
            builder.replace(dstart, dend, source.subSequence(start, end).toString())
            val inputText = builder.toString()
            if (!inputText.matches("L\\d{6}".toRegex())) {
                errorMessageTextView.visibility = View.VISIBLE
            } else {
                errorMessageTextView.visibility = View.GONE
            }
            null
        }
        editText.filters = arrayOf(filter)

        return view
    }
}
