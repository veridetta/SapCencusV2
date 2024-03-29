package com.example.sapcencuskotlin.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ActivityAdminBinding
import com.example.sapcencuskotlin.databinding.ActivityUserBinding
import com.example.sapcencuskotlin.helper.clearKTP
import com.example.sapcencuskotlin.helper.clearUser
import com.example.sapcencuskotlin.helper.getUser
import com.example.sapcencuskotlin.ui.admin.edituser.EditUserActivity
import com.example.sapcencuskotlin.ui.home.HomeActivity
import com.example.sapcencuskotlin.ui.user.data.view.ViewActivity
import com.example.sapcencuskotlin.ui.user.kk.scan.KKScanActivity
import com.example.sapcencuskotlin.ui.user.ktp.result.ResultActivity
import com.example.sapcencuskotlin.ui.user.ktp.scan.KtpScanActivity

class UserActivity : AppCompatActivity() {
    lateinit var binding : ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        initView()
        initListener()
        setView()
    }
    fun initView(){
        setContentView(binding.root)
    }
    fun initListener(){
        binding.btnScan.setOnClickListener {
            binding.lyConfirm.contentConfirm.visibility = android.view.View.VISIBLE
        }
        binding.lyConfirm.btnYa.setOnClickListener {
            val intent = Intent(this, KtpScanActivity::class.java)
//            val intent = Intent(this, KKScanActivity::class.java)
            startActivity(intent)
        }
        binding.lyConfirm.btnTidak.setOnClickListener {
            binding.lyConfirm.contentConfirm.visibility = android.view.View.GONE
            clearKTP(this)
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            clearUser(this)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnData.setOnClickListener {
            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
        }
    }
    fun setView(){
        val user = getUser(this)
        binding.tvName.text = user.name
    }

}