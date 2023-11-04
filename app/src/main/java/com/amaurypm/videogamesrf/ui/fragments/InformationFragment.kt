package com.amaurypm.videogamesrf.ui.fragments
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.databinding.FragmentInformationBinding
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class InformationFragment : Fragment() {
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!

    private val isbn = "9789873831072"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.d("InformationFragment", "ISBN: $isbn")
        // Cargar la portada del libro usando Glide
        loadBookCover()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadBookCover() {
        Log.d("InformationFragment", "Cargando la portada del libro")

        val key =
            "AIzaSyCGCD8TWnllLYAuoAcpkbfPP7HnRwk22Zs" // Reemplaza con tu clave de API de Google Books
        val apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn&key=$key"

        try {
            val url = URL(apiUrl)
            val conn = url.openConnection() as HttpsURLConnection
            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            val response = reader.readText()

            // Agrega un registro para imprimir la respuesta JSON
            Log.d("InformationFragment", "Respuesta JSON: $response")
            Log.d("InformationFragment", "ISBN: $apiUrl")

            val coverUrl = obtenerUrlPortada(response)

            if (coverUrl != null) {
                // Cargar la portada del libro usando Glide
                Glide.with(this)
                    .load(coverUrl)
                    .error(R.drawable.image_default) // Imagen de respaldo
                    .into(binding.portadaImageView)
            } else {
                Log.d("InformationFragment", "No se encontró la URL de la portada.")
                // Cargar la imagen de respaldo en caso de que no haya URL de portada
                Glide.with(this)
                    .load(R.drawable.image_default)
                    .into(binding.portadaImageView)
            }
        } catch (e: Exception) {
            Log.e("InformationFragment", "Error al cargar la portada: ${e.message}")
            Log.d("InformationFragment", "ISBN: $apiUrl")

            // Cargar la imagen de respaldo en caso de error
            Glide.with(this)
                .load(R.drawable.image_default)
                .into(binding.portadaImageView)
        }
    }


    private fun obtenerUrlPortada(jsonResponse: String): String {
        val jsonObject = JSONObject(jsonResponse)

        if (jsonObject.has("items")) {
            val items = jsonObject.getJSONArray("items")
            if (items.length() > 0) {
                val item = items.getJSONObject(0)
                if (item.has("volumeInfo")) {
                    val volumeInfo = item.getJSONObject("volumeInfo")
                    if (volumeInfo.has("imageLinks")) {
                        val imageLinks = volumeInfo.getJSONObject("imageLinks")

                        // Verifica si la URL de la miniatura (thumbnail) existe
                        if (imageLinks.has("smallThumbnail")) {
                            val smallThumbnail = imageLinks.getString("smallThumbnail")
                            Log.d("InformationFragment", "URL de miniatura obtenida: $smallThumbnail")
                            return smallThumbnail
                        }
                    }
                }
            }
        }

        Log.d("InformationFragment", "No se encontró la URL de la portada.")
        return ""
    }


}