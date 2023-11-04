package com.amaurypm.videogamesrf.data.remote


import com.amaurypm.videogamesrf.data.remote.model.BookDetailDto
import com.amaurypm.videogamesrf.data.remote.model.BookDto
import com.amaurypm.videogamesrf.data.remote.model.MagazineDTO
import com.amaurypm.videogamesrf.data.remote.model.MagazinesDetailDTO
import com.amaurypm.videogamesrf.data.remote.model.MyLibraryBooksDto
import com.amaurypm.videogamesrf.data.remote.model.NewBooksDto
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

    @GET("BibliotecaUAM-X/Revistas/Numeros_actuales")
    fun getMagazinesApiary(): Call<List<MagazineDTO>>
    @GET("BibliotecaUAM-X/Revistas_detail/{id}")
    fun getMagazineDetailApiary(
        @Path("id") id: String?/*,
        @Path("name") name: String?*/
    ): Call<MagazinesDetailDTO>

    @GET("BibliotecaUAM")
    fun getBooksApiary(): Call<List<BookDto>>

    //games/game_detail/21357
    @GET("games/game_detail/{id}")
    fun getBookDetailApiary(
        @Path("id") id: String?/*,
        @Path("name") name: String?*/
    ): Call<BookDetailDto>

    //aaaaaaaa
    @GET("BibliotecaUAM-X/Libros/Novedades")
    fun getNewBooksApiary(): Call<List<NewBooksDto>>
    @GET("BibliotecaUAM-X/Nuevos_Libros_detail/{id}")
    fun getNewBooksDetailApiary(
        @Path("id") id: String?/*,
        @Path("name") name: String?*/
    ): Call<MagazinesDetailDTO>


}