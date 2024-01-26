package com.example.sapcencuskotlin.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.sapcencuskotlin.databinding.ActivitySplashBinding
import com.example.sapcencuskotlin.helper.showSnackbar
import com.example.sapcencuskotlin.ui.admin.AdminActivity
import com.example.sapcencuskotlin.ui.home.HomeActivity
import com.example.sapcencuskotlin.ui.user.UserActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class SplashActivity : AppCompatActivity() {
    lateinit var viewModel : SplashVM
    lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initFirebase()
        observe()

    }

    fun initView(){
        viewModel = ViewModelProvider(this)[SplashVM::class.java]
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun initFirebase(){
        val firebase = FirebaseApp.initializeApp(this)
        Firebase.initialize(context = this)
    }

    fun observe(){
        viewModel.isLogin.observe(this){
            if (it){
                showSnackbar(this, "Login Success")
            }else{
                showSnackbar(this, "Login Failed")
                val intent  = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        viewModel.getUser.observe(this){
            if(it.isLogin){
                if(it.role == "admin"){
                    showSnackbar(this, "Welcome Admin")
                    val intent  = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    showSnackbar(this, "Welcome User")
                    val intent  = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }else{
                showSnackbar(this, "Login Failed")
                val intent  = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        viewModel.checkLogin(this)
    }
}