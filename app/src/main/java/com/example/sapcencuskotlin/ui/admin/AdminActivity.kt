package com.example.sapcencuskotlin.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ActivityAdminBinding
import com.example.sapcencuskotlin.helper.clearUser
import com.example.sapcencuskotlin.ui.admin.edituser.EditUserActivity

class AdminActivity : AppCompatActivity() {
    lateinit var binding : ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        initView()
        initListener()
    }
    fun initView(){
        setContentView(binding.root)
    }
    fun initListener(){
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            clearUser(this)
            finish()
        }
    }
}