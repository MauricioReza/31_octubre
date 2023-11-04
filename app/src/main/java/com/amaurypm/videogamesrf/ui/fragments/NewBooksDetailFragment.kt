package com.amaurypm.videogamesrf.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.BooksRFApp
import com.amaurypm.videogamesrf.data.BookRepository
import com.amaurypm.videogamesrf.data.remote.model.MagazinesDetailDTO
import com.amaurypm.videogamesrf.data.remote.model.MyLibraryBooksDto
import com.amaurypm.videogamesrf.databinding.FragmentNewBooksDetailBinding
import com.amaurypm.videogamesrf.ui.MainActivity
import com.amaurypm.videogamesrf.util.Constants
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val NEW_BOOK_ID = "newBook_id"

class NewBooksDetailFragment : Fragment() {

    private var newBookId: String? = null

    private var _binding: FragmentNewBooksDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: BookRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Navega a la actividad principal (MainActivity)
            navigateToMainActivity()
        }
    }
    private fun navigateToMainActivity() {
        // Comprueba si la actividad es MainActivity
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity

            // Llama al método de navegación en MainActivity
            mainActivity.navigateToMainFragment()
        }}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            newBookId = args.getString(NEW_BOOK_ID)

            Log.d(Constants.LOGTAG, "Id recibido: $newBookId")

            repository = (requireActivity().application as BooksRFApp).repository

            lifecycleScope.launch {

                newBookId?.let { id ->
                    //val call: Call<GameDetailDto> = repository.getGameDetail(id)
                    val call: Call<MagazinesDetailDTO> = repository.getNewBooksDetailApiary(id)

                    call.enqueue(object: Callback<MagazinesDetailDTO> {
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<MagazinesDetailDTO>,
                            response: Response<MagazinesDetailDTO>
                        ) {


                            binding.apply {
                                pbLoading.visibility = View.GONE

                                tvTitle.text = response.body()?.titulo
                                tvNumero.text = response.body()?.número
                                tvISSN.text = getString(R.string.ISSN_label) + " " + response.body()?.ISSN
                                tvPublicado.text = getString(R.string.publicado_label) + " " + response.body()?.publicado
                                tvcontent.text = getString(R.string.Content_label) + response.body()?.content

                                Glide.with(requireContext())
                                    .load(response.body()?.thumbnail)
                                    .into(thumbnail)
                            }

                        }

                        override fun onFailure(call: Call<MagazinesDetailDTO>, t: Throwable) {
                            binding.pbLoading.visibility = View.GONE

                            Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()
                        }

                    })
                }

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewBooksDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(newBookId: String) =
            NewBooksDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(NEW_BOOK_ID, newBookId)
                }
            }
    }
}