package com.amaurypm.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Creado por Amaury Perea Matsumura el 02/09/23
 */

/*
data class BookDetailDto(


    @SerializedName("title")
    var title: String? = null,
    @SerializedName("Ubicacion")
    var Ubicacion: String? = null,
    @SerializedName("Coleccion")
    var Coleccion: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("long_desc")
    var longDesc: String? = null


)
*/
data class BookDetailDto(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("titulo")
    var titulo: String? = null,
    @SerializedName("autor")
    var autor: String? = null,
    @SerializedName("ubicación")
    var ubicación: String? = null,
    @SerializedName("clasificación")
    var clasificación: String? = null,
    @SerializedName("coleccion")
    var coleccion: String? = null,
    // @SerializedName("thumbnail")
    // var thumbnail: String? = null,
    @SerializedName("disponibilidad")
    var disponibilidad: String? = null,
    @SerializedName("materia")
    var materia: String? = null,

    //SELECTED
    @SerializedName("isSelected")
    var isSelected: Boolean = false


)