package com.amaurypm.videogamesrf.data

import com.amaurypm.videogamesrf.data.remote.BooksApi
import com.amaurypm.videogamesrf.data.remote.model.BookDetailDto
import com.amaurypm.videogamesrf.data.remote.model.BookDto
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

    fun getGamesApiary(): Call<List<BookDto>> =
        booksApi.getGamesApiary()

    fun getGameDetailApiary(id: String?): Call<BookDetailDto> =
        booksApi.getGameDetailApiary(id)


}