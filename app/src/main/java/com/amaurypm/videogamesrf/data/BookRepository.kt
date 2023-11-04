package com.amaurypm.videogamesrf.data

import com.amaurypm.videogamesrf.data.remote.BooksApi
import com.amaurypm.videogamesrf.data.remote.model.BookDetailDto
import com.amaurypm.videogamesrf.data.remote.model.BookDto
import com.amaurypm.videogamesrf.data.remote.model.MagazineDTO
import com.amaurypm.videogamesrf.data.remote.model.MagazinesDetailDTO
import com.amaurypm.videogamesrf.data.remote.model.MyLibraryBooksDto
import com.amaurypm.videogamesrf.data.remote.model.NewBooksDto
import retrofit2.Call
import retrofit2.Retrofit

/**
 * Creado por Amaury Perea Matsumura el 02/09/23
 */
class BookRepository(private val retrofit: Retrofit) {

    private val booksApi: BooksApi = retrofit.create(BooksApi::class.java)

    fun getGames(url: String): Call<List<BookDto>> =
        booksApi.getGames(url)

    fun getGameDetail(id: String?): Call<BookDetailDto> =
        booksApi.getGameDetail(id)

    fun getBooksApiary(): Call<List<BookDto>> =
        booksApi.getBooksApiary()

    fun getBookDetailApiary(id: String?): Call<BookDetailDto> =
        booksApi.getBookDetailApiary(id)

    fun getMagazinesApiary(): Call<List<MagazineDTO>> =
        booksApi.getMagazinesApiary()

    fun getMagazineDetailApiary(id: String?): Call<MagazinesDetailDTO> =
        booksApi.getMagazineDetailApiary(id)

    //funciones para los libros nuevos
    fun getNewBooksApiary(): Call<List<NewBooksDto>> =
        booksApi.getNewBooksApiary()

    fun getNewBooksDetailApiary(id: String?): Call<MagazinesDetailDTO> =
        booksApi.getNewBooksDetailApiary(id)
}