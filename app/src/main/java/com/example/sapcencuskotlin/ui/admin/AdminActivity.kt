package com.example.sapcencuskotlin.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ActivityAdminBinding
import com.example.sapcencuskotlin.helper.clearUser
import com.example.sapcencuskotlin.helper.getUser
import com.example.sapcencuskotlin.ui.admin.edituser.EditUserActivity
import com.example.sapcencuskotlin.ui.admin.user.ListUserActivity
import com.example.sapcencuskotlin.ui.home.HomeActivity
import com.example.sapcencuskotlin.ui.user.UserActivity

class AdminActivity : AppCompatActivity() {
    lateinit var binding : ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        initView()
        initListener()
        setView()
    }
    fun initView(){
        setContentView(binding.root)
    }
    fun initListener(){
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, ListUserActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            clearUser(this)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun setView(){
        val user = getUser(this)
        binding.tvName.text = user.name
    }
}