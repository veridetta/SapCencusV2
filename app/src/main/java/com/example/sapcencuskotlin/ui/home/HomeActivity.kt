package com.example.sapcencuskotlin.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ActivityHomeBinding
import com.example.sapcencuskotlin.databinding.ActivitySplashBinding
import com.example.sapcencuskotlin.ui.pin.PinActivity

class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    var role = ""
    var init =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initSpinner()
    }
    fun initView(){
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun initSpinner(){
        val rolesArray = resources.getStringArray(R.array.role)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rolesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = binding.spRole
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(init>0){
                    role = rolesArray[position]
                    val intent = Intent(this@HomeActivity, PinActivity::class.java)
                    intent.putExtra("role", role)
                    startActivity(intent)
                }
                init++
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle ketika tidak ada yang dipilih
            }
        }
    }

}