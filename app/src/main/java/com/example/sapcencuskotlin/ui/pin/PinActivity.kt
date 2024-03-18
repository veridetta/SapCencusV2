package com.example.sapcencuskotlin.ui.pin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ActivityPinBinding
import com.example.sapcencuskotlin.databinding.ActivitySplashBinding
import com.example.sapcencuskotlin.helper.getUser
import com.example.sapcencuskotlin.helper.showSnackbar
import com.example.sapcencuskotlin.model.UserModel
import com.example.sapcencuskotlin.ui.admin.AdminActivity
import com.example.sapcencuskotlin.ui.splash.SplashVM
import com.example.sapcencuskotlin.ui.user.UserActivity

class PinActivity : AppCompatActivity() {
    lateinit var viewModel : PinVM
    lateinit var binding : ActivityPinBinding
    var role = ""
    var pin = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        isLoading()
        initIntent()
        initListener()
    }
    fun initView(){
        viewModel = ViewModelProvider(this)[PinVM::class.java]
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun isLoading(){
        viewModel.isLoading.observe(this){
            if(it){
                binding.lyLoading.contentLoading.visibility = View.VISIBLE
            }else{
                binding.lyLoading.contentLoading.visibility = View.GONE
            }
        }
    }
    fun initIntent(){
        role = intent.getStringExtra("role").toString()
        binding.tvRole.text = role
    }
    fun initListener(){
        binding.etPin.isEnabled = false
        binding.btn1.setOnClickListener {
            pin += "1"
            binding.etPin.setText(pin)
        }
        binding.btn2.setOnClickListener {
            pin += "2"
            binding.etPin.setText(pin)
        }
        binding.btn3.setOnClickListener {
            pin += "3"
            binding.etPin.setText(pin)
        }
        binding.btn4.setOnClickListener {
            pin += "4"
            binding.etPin.setText(pin)
        }
        binding.btn5.setOnClickListener {
            pin += "5"
            binding.etPin.setText(pin)
        }
        binding.btn6.setOnClickListener {
            pin += "6"
            binding.etPin.setText(pin)
        }
        binding.btn7.setOnClickListener {
            pin += "7"
            binding.etPin.setText(pin)
        }
        binding.btn8.setOnClickListener {
            pin += "8"
            binding.etPin.setText(pin)
        }
        binding.btn9.setOnClickListener {
            pin += "9"
            binding.etPin.setText(pin)
        }
        binding.btn0.setOnClickListener {
            pin += "0"
            binding.etPin.setText(pin)
        }
        binding.btnClear.setOnClickListener {
            pin = ""
            binding.etPin.setText(pin)
        }
        binding.btnOk.setOnClickListener {
            val user = UserModel()
            user.pin = pin
            user.role = role
            viewModel.checkPin(this, user)
            viewModel.idle.observe(this){
                if(it){
                    val userData = getUser(this)
                    if(userData.isLogin){
                        if(userData.role != role){
                            showSnackbar(this, "Login Failed")
                        }
                        if(userData.role == "admin"){
                            showSnackbar(this, "Welcome ${userData.name}")
                            val intent  = Intent(this, AdminActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            showSnackbar(this, "Welcome ${userData.name}")
                            val intent  = Intent(this, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }else{
                        showSnackbar(this, "Login Failed")
                    }
                }
            }
        }
    }
}