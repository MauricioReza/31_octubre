package com.amaurypm.videogamesrf.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.BooksRFApp
import com.amaurypm.videogamesrf.data.BookRepository
import com.amaurypm.videogamesrf.data.remote.model.BookDto
import com.amaurypm.videogamesrf.data.remote.model.MyLibraryBooksDto
import com.amaurypm.videogamesrf.databinding.FragmentMyLibraryBinding
import com.amaurypm.videogamesrf.extensions.navigateToPreviousFragment
import com.amaurypm.videogamesrf.ui.MainActivity
import com.amaurypm.videogamesrf.ui.adapters.MyLibraryBooksAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class MyLibraryFragment : Fragment() {
    private var _binding: FragmentMyLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: BookRepository
    private lateinit var myLibraryBooksAdapter: MyLibraryBooksAdapter
    private val books: MutableList<BookDto> = mutableListOf() // Declarar la lista de libros


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as BooksRFApp).repository

        myLibraryBooksAdapter = MyLibraryBooksAdapter(requireContext(), books) { book ->
            // Manejar la interacción con los elementos de la lista si es necesario
        }

        val btnLogin = view.findViewById<TextView>(R.id.tvAddABook)
        btnLogin.setOnClickListener {
            // Reemplazar el fragmento actual por el fragmento BooksListFragment
            val fragment = BooksListFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            (activity as? MainActivity)?.navigateToPreviousFragment()
        }

        binding.rvBooks.adapter = myLibraryBooksAdapter
        binding.rvBooks.layoutManager = LinearLayoutManager(requireContext())

        cargarInformacionDesdeArchivo()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarInformacionDesdeArchivo() {
        try {
            val file = File(requireContext().filesDir, "app_data.json")

            if (file.exists()) {
                val jsonString = file.readText()
                val listType = object : TypeToken<List<BookDto>>() {}.type
                val loadedBooks = Gson().fromJson<List<BookDto>>(jsonString, listType)

                val reversedLoadedBooks = loadedBooks.reversed()
                books.addAll(reversedLoadedBooks)



                if (books.isEmpty()) {
                    binding.llEmpty.visibility = View.VISIBLE
                } else {
                    binding.llEmpty.visibility = View.GONE
                }

                myLibraryBooksAdapter.notifyDataSetChanged()
            } else {
                binding.llEmpty.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
