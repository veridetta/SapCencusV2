package com.example.sapcencuskotlin.ui.user.data.cari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ActivityCariBinding
import com.example.sapcencuskotlin.databinding.ActivityEditDataBinding
import com.example.sapcencuskotlin.ui.user.data.edit.EditDataActivity
import com.example.sapcencuskotlin.ui.user.data.edit.EditDataVM

class CariActivity : AppCompatActivity() {
    lateinit var binding: ActivityCariBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()

    }
    fun initView(){
//        vm = ViewModelProvider(this)[EditDataVM::class.java]
        binding = ActivityCariBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun initListener(){
        binding.btnCari.setOnClickListener {
            val nik = binding.etNik.text.toString()
            val intent = Intent(this, EditDataActivity::class.java)
            intent.putExtra("nik", nik)
            startActivity(intent)
        }
    }
}