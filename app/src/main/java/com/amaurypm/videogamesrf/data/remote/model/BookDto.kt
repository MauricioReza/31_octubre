package com.amaurypm.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName


data class BookDto(
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
