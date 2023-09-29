package com.amaurypm.videogamesrf.application

import android.app.Application
import com.amaurypm.videogamesrf.data.BookRepository
import com.amaurypm.videogamesrf.data.remote.RetrofitHelper


class BooksRFApp: Application() {

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy{
        BookRepository(retrofit)
    }

}