package com.amaurypm.videogamesrf.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.BooksRFApp
import com.amaurypm.videogamesrf.data.BookRepository
import com.amaurypm.videogamesrf.data.remote.model.BookDto
import com.amaurypm.videogamesrf.databinding.FragmentBooksListBinding
import com.amaurypm.videogamesrf.ui.adapters.BooksAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.Normalizer
import java.util.Locale

class BooksListFragment : Fragment() {


    private var _binding: FragmentBooksListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: BookRepository
    private lateinit var booksAdapter: BooksAdapter
    private var libros: List<BookDto> = emptyList() // Lista de libros original

    // Variable para realizar un seguimiento del botón de radio seleccionado
    private var selectedRadioButtonId: Int = R.id.rbTittle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBooksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as BooksRFApp).repository

        // Configurar el RecyclerView y el adaptador
        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(requireContext())
            booksAdapter = BooksAdapter(emptyList()) { book ->
                book.id?.let { id ->
                    // Código para ver los detalles del libro
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            BookDetailFragment.newInstance(id)
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }
            adapter = booksAdapter
        }

        // Configurar el TextWatcher para la búsqueda
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    // Si el texto se borra, limpiar la lista
                    booksAdapter.updateData(emptyList())
                } else {
                    performSearch()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Inicialmente, selecciona la búsqueda por título por defecto
        binding.rbTittle.isChecked = true

        // Agrega OnClickListener para los botones de radio
        binding.rbTittle.setOnClickListener {
            selectedRadioButtonId = R.id.rbTittle
            performSearch()
        }

        binding.rbAuthor.setOnClickListener {
            selectedRadioButtonId = R.id.rbAuthor
            performSearch()
        }

        binding.rbSubject.setOnClickListener {
            selectedRadioButtonId = R.id.rbSubject
            performSearch()
        }

        // Cargar los libros iniciales
        loadBooks()
    }

    private fun loadBooks() {
        lifecycleScope.launch {
            val call: Call<List<BookDto>> = repository.getGamesApiary()

            call.enqueue(object : Callback<List<BookDto>> {
                override fun onResponse(
                    call: Call<List<BookDto>>,
                    response: Response<List<BookDto>>
                ) {
                    // Comentado para que no se oculte el ProgressBar
                    // binding.pbLoading.visibility = View.GONE

                    if (response.isSuccessful) {
                        libros = response.body() ?: emptyList()

                        // Mostrar Toast de conexión exitosa
                        showSuccessToast()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.toast_no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<BookDto>>, t: Throwable) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.toast_no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun performSearch() {
        val query = binding.etSearch.text?.toString()?.trim() ?: ""

        // Validar que el texto de búsqueda tenga al menos 3 letras (contando los espacios)
        if (query.replace(" ", "").length < 3) {
            // Si el texto es demasiado corto, muestra el mensaje de error
            binding.tvRecordsFound.text =
                getString(R.string.registros_encontrados, 0)
            booksAdapter.updateData(emptyList())
            return // Salir del método si no hay suficientes letras
        }

        val normalizedQuery = normalizeString(query)

        val filteredLibros = when (selectedRadioButtonId) {
            R.id.rbTittle -> {
                libros.filter { book ->
                    val normalizedTitle = normalizeString(book.titulo.orEmpty())
                    normalizedTitle.contains(normalizedQuery)
                }
            }
            R.id.rbAuthor -> {
                libros.filter { book ->
                    val normalizedAutor = normalizeString(book.autor.orEmpty())
                    normalizedAutor.contains(normalizedQuery)
                }
            }
            R.id.rbSubject -> {
                libros.filter { book ->
                    val normalizedMateria = normalizeString(book.materia.orEmpty())
                    val normalizedAutor = normalizeString(book.autor.orEmpty())
                    normalizedMateria.contains(normalizedQuery) || normalizedAutor.contains(normalizedQuery)
                }
            }
            else -> emptyList() // Por defecto, no se deben mostrar resultados
        }

        // Contar el número de registros encontrados y establecerlo en el TextView
        val numRegistrosEncontrados = filteredLibros.size
        binding.tvRecordsFound.text =
            getString(R.string.registros_encontrados, numRegistrosEncontrados)

        booksAdapter.updateData(filteredLibros)
    }

    private fun normalizeString(input: String): String {
        // Normalizar el texto eliminando acentos y convirtiendo a minúsculas
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase(Locale.ROOT)
        return normalized
    }

    private fun showSuccessToast() {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast_layout, null)

        val customToastLayout = layout.findViewById<LinearLayout>(R.id.custom_toast_layout)
        customToastLayout.visibility = View.VISIBLE

        val imageViewToast = layout.findViewById<ImageView>(R.id.imageViewToast)
        imageViewToast.setImageResource(R.drawable.baseline_check_circle_24)

        val textViewToast = layout.findViewById<TextView>(R.id.textViewToast)
        textViewToast.text = getString(R.string.connection_success_message)

        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

