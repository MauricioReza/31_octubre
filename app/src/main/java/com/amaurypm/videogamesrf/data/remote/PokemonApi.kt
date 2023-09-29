package com.amaurypm.videogamesrf.data.remote

import com.amaurypm.videogamesrf.data.remote.model.PokemonDetailDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Creado por Amaury Perea Matsumura el 02/09/23
 */
interface PokemonApi {

    //https://pokeapi.co/api/v2/pokemon/149/

    @GET("api/v2/pokemon/{id}")
    fun getPokemonDetail(
        @Path("id") id: String?
    ): Call<PokemonDetailDto>


}