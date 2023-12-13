package br.edu.ifsp.scl.sdm.todolist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.sdm.todolist.R
import br.edu.ifsp.scl.sdm.todolist.databinding.ActivityMainBinding

//container para os fragments
class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb)
        supportActionBar?.title = getString(R.string.app_name)
    }
}