package com.amaurypm.videogamesrf.data.remote


import com.amaurypm.videogamesrf.data.remote.model.BookDetailDto
import com.amaurypm.videogamesrf.data.remote.model.BookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Creado por Amaury Perea Matsumura el 02/09/23
 */
interface BooksApi {

    @GET
    fun getGames(
        @Url url: String?
    ): Call<List<BookDto>>
    //getGames("cm/games/games_list.php")


    @GET("cm/games/game_detail.php")
    fun getGameDetail(
        @Query("id") id: String?/*,
        @Query("name") name: String?*/
    ): Call<BookDetailDto>
    //getGameDetail("21347","amaury")
    //cm/games/game_detail.php?id=21347&name=amaury

    //Para Apiary
    @GET("BibliotecaUAM")
    fun getGamesApiary(): Call<List<BookDto>>

    //games/game_detail/21357

    @GET("Libros/LibrosDetalle/{id}")
    fun getGameDetailApiary(
        @Path("id") id: String?/*,
        @Path("name") name: String?*/
    ): Call<BookDetailDto>

    //getGameDetailApiary("21357","Amaury")
    //games/game_detail/21347/Amaury




}