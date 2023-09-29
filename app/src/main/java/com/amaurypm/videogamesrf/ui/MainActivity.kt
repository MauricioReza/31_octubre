package com.amaurypm.videogamesrf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.databinding.ActivityMainBinding
import com.amaurypm.videogamesrf.ui.fragments.BooksListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /*private lateinit var repository: GameRepository
    private lateinit var retrofit: Retrofit*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BooksListFragment())
                .commit()
        }
        val fab = findViewById<FloatingActionButton>(R.id.AddBook)

        fab.setOnClickListener {
            // Mostrar el fragmento de diálogo
            val fragmentManager = supportFragmentManager
            val dialogFragment = MyDialogFragment()
            dialogFragment.show(fragmentManager, "MyDialogFragment")
        }


        /*retrofit = RetrofitHelper().getRetrofit()

        repository = GameRepository(retrofit)

        lifecycleScope.launch {
            val call: Call<List<GameDto>> = repository.getGames("cm/games/games_list.php")

            call.enqueue(object: Callback<List<GameDto>>{
                override fun onResponse(
                    call: Call<List<GameDto>>,
                    response: Response<List<GameDto>>
                ) {
                    Log.d(Constants.LOGTAG, "Respuesta del servidor ${response.body()}")
                }

                override fun onFailure(call: Call<List<GameDto>>, t: Throwable) {
                    //Manejo del error
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })

        }*/
    }
}