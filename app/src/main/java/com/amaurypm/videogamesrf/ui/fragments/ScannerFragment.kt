package com.amaurypm.videogamesrf.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.BooksRFApp
import com.amaurypm.videogamesrf.data.BookRepository
import com.amaurypm.videogamesrf.data.remote.model.BookDto
import com.amaurypm.videogamesrf.data.remote.model.GoogleBooksApiService
import com.amaurypm.videogamesrf.data.remote.model.GoogleBooksResponse
import com.amaurypm.videogamesrf.data.remote.model.NewBooksDto
import com.amaurypm.videogamesrf.databinding.FragmentScannerBinding
import com.amaurypm.videogamesrf.extensions.navigateToPreviousFragment
import com.amaurypm.videogamesrf.ui.MainActivity
import com.amaurypm.videogamesrf.ui.adapters.BooksAdapter
import com.amaurypm.videogamesrf.ui.adapters.NewBooksAdapter
import com.amaurypm.videogamesrf.util.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ScannerFragment : Fragment() {

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: BookRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            (activity as? MainActivity)?.navigateToPreviousFragment()
        }
        binding.cbvScanner.decodeContinuous { result ->

            val scannedCode = result.text

            repository = (requireActivity().application as BooksRFApp).repository

            lifecycleScope.launch {
                try {
                    val booksCall: Call<List<BookDto>> = repository.getBooksApiary()

                    booksCall.enqueue(object : Callback<List<BookDto>> {
                        override fun onResponse(
                            call: Call<List<BookDto>>,
                            response: Response<List<BookDto>>
                        ) {
                            if (response.isSuccessful) {
                                val booksList = response.body()
//                                Log.d(Constants.LOGTAG, "Respuesta del servidor (Scanned Books): ${response.body()}")

                                if (booksList != null) {
                                    val matchedBook = booksList.find { book ->
                                        scannedCode == book.clasificación

                                    }

                                    if (matchedBook != null) {
                                        val isBookAlreadyAdded = isBookAlreadyInFile(matchedBook)

                                        if (isBookAlreadyAdded) {

                                            // El libro ya existe en la lista, muestra un diálogo
                                            AlertDialog.Builder(requireContext())
                                                .setTitle("El libro ${matchedBook.titulo} ya está en tu biblioteca")
                                                .setMessage("¿Quieres ver el registro?")
                                                .setPositiveButton("Si") { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                .setNegativeButton("No") { dialog, _ ->
                                                    binding.cbvScanner.resume()

                                                    dialog.dismiss()

                                                }
                                                .create()
                                                .show()
                                        } else {
                                            getThumbnailUrlAndSaveBook(matchedBook)
                                        }
                                    }  else {
                                        // No se encontró una coincidencia
                                        Toast.makeText(
                                            requireContext(),
                                            "No se encontró ningún libro con el código escaneado",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            } else {
                                // Manejo de errores si la respuesta no es exitosa
                                Toast.makeText(
                                    requireContext(),
                                    "Error al obtener la lista de libros desde la API",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<List<BookDto>>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                } catch (e: Exception) {
                    // Manejo de excepciones
                    e.printStackTrace()
                }
            }

            binding.cbvScanner.pause()
        }
    }
    private fun isBookAlreadyInFile(book: BookDto): Boolean {
        val fileName = "app_data.json"
        val gson = Gson()

        try {
            val inputStream = requireContext().openFileInput(fileName)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            val existingData = String(buffer)
            val listType = object : TypeToken<List<BookDto>>() {}.type
            val existingList = gson.fromJson<List<BookDto>>(existingData, listType)
            return existingList.any { it.clasificación == book.clasificación }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
    private fun getThumbnailUrlAndSaveBook(matchedBook: BookDto) {
        val isbn = matchedBook.isbn
        if (!isbn.isNullOrBlank()) {
            val key = "AIzaSyCGCD8TWnllLYAuoAcpkbfPP7HnRwk22Zs"  // Reemplaza por tu clave de API de Google

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(GoogleBooksApiService::class.java)

            val call = apiService.getBookByISBN(isbn, key)
            call.enqueue(object : Callback<GoogleBooksResponse> {
                override fun onResponse(
                    call: Call<GoogleBooksResponse>,
                    response: Response<GoogleBooksResponse>
                ) {
                    if (response.isSuccessful) {
                        val googleBooksResponse = response.body()
                        val thumbnail = googleBooksResponse?.items?.firstOrNull()?.volumeInfo?.imageLinks?.thumbnail

                        matchedBook.thumbnail = thumbnail

                        // Guardar el libro con la URL de la portada
                        saveMatchedBook(matchedBook)
                    } else {
                        // Handle error
                        // En caso de error al obtener la URL de la portada, puedes continuar sin la URL o manejarlo como desees
                        saveMatchedBook(matchedBook)
                    }
                }

                override fun onFailure(call: Call<GoogleBooksResponse>, t: Throwable) {
                    // Handle failure
                    // En caso de falla al obtener la URL de la portada, puedes continuar sin la URL o manejarlo como desees
                    saveMatchedBook(matchedBook)
                }
            })
        }
    }
    private fun saveMatchedBook(matchedBook: BookDto) {
        val fileName = "app_data.json"
        val gson = Gson()

        val existingData = try {
            val inputStream = requireContext().openFileInput(fileName)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            String(buffer)
        } catch (e: Exception) {
            ""
        }

        val listType = object : TypeToken<List<BookDto>>() {}.type

        val existingList = gson.fromJson<List<BookDto>>(existingData, listType)

        val newData = if (existingList != null) {
            existingList + matchedBook
        } else {
            listOf(matchedBook)
        }

        val combinedDataJson = gson.toJson(newData)

        try {
            val fileOutputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(combinedDataJson.toByteArray())
            fileOutputStream.close()

            // Marca el libro como no favorito
            matchedBook.isFavorite = false


            AlertDialog.Builder(requireContext())
                .setTitle("Añadimos a tu biblioteca el libro,\n${matchedBook.titulo}")
                .setMessage("¿Quiere ver el registro?")
                .setPositiveButton("Sí") { dialog, _ ->
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MyLibraryFragment())
                        .commit()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.cbvScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.cbvScanner.pause()
    }

}