package com.amaurypm.videogamesrf.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.BooksRFApp
import com.amaurypm.videogamesrf.data.BookRepository
import com.amaurypm.videogamesrf.data.remote.model.BookDetailDto
import com.amaurypm.videogamesrf.databinding.FragmentBookDetailBinding
import com.amaurypm.videogamesrf.util.Constants
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookDetailFragment : Fragment() {

    companion object {
        private const val EXTRA_GAME_ID = "game_id"

        @JvmStatic
        fun newInstance(gameId: String) =
            BookDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_GAME_ID, gameId)
                }
            }
    }

    private var gameId: String? = null
    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: BookRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            gameId = args.getString(EXTRA_GAME_ID)

            Log.d(Constants.LOGTAG, "Id recibido: $gameId")

            repository = (requireActivity().application as BooksRFApp).repository

            lifecycleScope.launch {
                gameId?.let { id ->
                    val call: Call<BookDetailDto> = repository.getGameDetailApiary(id)

                    call.enqueue(object : Callback<BookDetailDto> {
                        override fun onResponse(
                            call: Call<BookDetailDto>,
                            response: Response<BookDetailDto>
                        ) {
                            binding.apply {
                                pbLoading.visibility = View.GONE
                                tvTitle.text = response.body()?.titulo
                                tvLongDesc.text = response.body()?.autor

                                tvUbicacion.text = getString(R.string.ubicacion_label) + " " + response.body()?.clasificación
                                tvColeccion.text = getString(R.string.coleccion_label) + " " + response.body()?.coleccion

                           //     Glide.with(requireContext())
                             //       .load(response.body()?.image)
                               //     .into(ivImage)
                            }
                        }

                        override fun onFailure(call: Call<BookDetailDto>, t: Throwable) {
                            binding.pbLoading.visibility = View.GONE
                            val noConnectionText = getString(R.string.toast_no_connection)
                            Toast.makeText(
                                requireActivity(),
                                noConnectionText,
                                Toast.LENGTH_SHORT
                            ).show()
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
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
