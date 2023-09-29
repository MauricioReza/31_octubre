package com.amaurypm.videogamesrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amaurypm.videogamesrf.data.remote.model.BookDto
import com.amaurypm.videogamesrf.databinding.GameElementBinding


class BooksAdapter(
    private var books: List<BookDto>,
    private val onGameClicked: (BookDto) -> Unit
): RecyclerView.Adapter<BooksAdapter.ViewHolder>() {


    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        //  val ivThumbnail = binding.ivThumbnail

        fun bind(book: BookDto){
            binding.tvTitle.text = book.titulo
            binding.tvAuthor.text = book.autor // Agregar el autor
            binding.tvSubject.text = book.materia// Agregar el pie de imprenta
            binding.tvClassification.text = book.clasificación // Agregar la clasificación

            

            // Cargar la imagen utilizando Picasso (Puedes usar Glide de manera similar)
            //      Picasso.get()
            //          .load(game.thumbnail)
            //          .into(ivThumbnail)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]

        holder.bind(book)

        // Procesamiento del clic al elemento
         holder.itemView.setOnClickListener {
            onGameClicked(book)
        }


    }

    fun updateData(filteredGames: List<BookDto>) {
        books = filteredGames
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }
}
