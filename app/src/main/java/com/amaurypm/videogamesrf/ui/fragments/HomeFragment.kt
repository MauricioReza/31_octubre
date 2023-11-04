package com.amaurypm.videogamesrf.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.BooksRFApp
import com.amaurypm.videogamesrf.data.BookRepository
import com.amaurypm.videogamesrf.data.remote.model.MagazineDTO
import com.amaurypm.videogamesrf.data.remote.model.NewBooksDto
import com.amaurypm.videogamesrf.databinding.FragmentHomeBinding
import com.amaurypm.videogamesrf.ui.adapters.MagazinesAdapter
import com.amaurypm.videogamesrf.ui.adapters.NewBooksAdapter
import com.amaurypm.videogamesrf.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: BookRepository
    private lateinit var newBooksAdapter: NewBooksAdapter // Crea un adaptador para NewBooksDto
    private lateinit var magazinesAdapter: MagazinesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Obtiene una referencia al TextView
        val textView = binding.root.findViewById<TextView>(R.id.tvFirtPraragraph)

// Tu código para formatear el texto con SpannableString
        val texto = "Si eres amante de los libros y la lectura, te recomiendo visitar el sitio oficial de Interlínea: cultura editorial."
        val spannableString = SpannableString(texto)

        val startIndexLibro = texto.indexOf("libros y la lectura")
        val endIndexLibro = startIndexLibro + "libros y la lectura".length
        val startIndexMultimedia = texto.indexOf("Interlínea: cultura editorial")
        val endIndexMultimedia = startIndexMultimedia + "Interlínea: cultura editorial".length

        val context: Context = requireContext() // Esto asume que estás en un fragmento
        val colorPurple = ContextCompat.getColor(context, R.color.colorPurpleUam)
        val blueColorSpanLibro = ForegroundColorSpan(colorPurple)
        val blueColorSpanMultimedia = ForegroundColorSpan(colorPurple)

        spannableString.setSpan(blueColorSpanLibro, startIndexLibro, endIndexLibro, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(blueColorSpanMultimedia, startIndexMultimedia, endIndexMultimedia, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannableString

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<ImageButton>(R.id.etSearch)


        etSearch.setOnClickListener {
            val fragment = BooksListFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null) // Opcional, agrega a la pila de retroceso
            transaction.commit()
        }

        repository = (requireActivity().application as BooksRFApp).repository

        // Cargar nuevos libros
        lifecycleScope.launch {
            val newBooksCall: Call<List<NewBooksDto>> = repository.getNewBooksApiary()

            newBooksCall.enqueue(object : Callback<List<NewBooksDto>> {
                override fun onResponse(
                    call: Call<List<NewBooksDto>>,
                    response: Response<List<NewBooksDto>>
                ) {
                    binding.pbLoading.visibility = View.GONE

                    Log.d(Constants.LOGTAG, "Respuesta del servidor (New Books): ${response.body()}")

                    response.body()?.let { newBooks ->
                        newBooksAdapter = NewBooksAdapter(newBooks) { selectedNewBook ->
                            selectedNewBook.id?.let { id ->
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.fragment_container,
                                        NewBooksDetailFragment.newInstance(id)
                                    )
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }

                        binding.rvNewBooks.apply {
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            adapter = newBooksAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<List<NewBooksDto>>, t: Throwable) {
                    Log.d(Constants.LOGTAG, "Error (New Books): ${t.message}")

                    Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()

                    binding.pbLoading.visibility = View.GONE

                }
            })
        }

        // Cargar revistas
        lifecycleScope.launch {
            val magazinesCall: Call<List<MagazineDTO>> = repository.getMagazinesApiary()

            magazinesCall.enqueue(object : Callback<List<MagazineDTO>> {
                override fun onResponse(
                    call: Call<List<MagazineDTO>>,
                    response: Response<List<MagazineDTO>>
                ) {
                    Log.d(Constants.LOGTAG, "Respuesta del servidor (Magazines): ${response.body()}")

                    response.body()?.let { magazines ->
                        magazinesAdapter = MagazinesAdapter(magazines) { selectedMagazine ->
                            selectedMagazine.id?.let { id ->
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.fragment_container,
                                        MagazineDetailFragment.newInstance(id)
                                    )
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }

                        binding.rvNewMagazines.apply {
                            layoutManager = LinearLayoutManager(
                                requireContext(),

                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            adapter = magazinesAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<List<MagazineDTO>>, t: Throwable) {
                    Log.d(Constants.LOGTAG, "Error (Magazines): ${t.message}")

                    Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}