package com.amaurypm.videogamesrf.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


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
    @SerializedName("colección")
    var colección: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("disponibilidad")
    var disponibilidad: String? = null,
    @SerializedName("isbn")
    var isbn: String? = null,
    @SerializedName("materia")
    var materia: String? = null,

    var isFavorite: Boolean = false,
    var showCheckBox: Boolean = false


)
