package com.example.sapcencuskotlin.ui.admin.adduser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ActivityAddUserBinding
import com.example.sapcencuskotlin.databinding.ActivityListUserBinding
import com.example.sapcencuskotlin.model.UserModel
import com.example.sapcencuskotlin.ui.admin.user.ListUserVM

class AddUserActivity : AppCompatActivity() {
    lateinit var vm: AddUserVM
    lateinit var binding: ActivityAddUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        isLoading()
        initListener()

    }
    fun initView(){
        vm = ViewModelProvider(this)[AddUserVM::class.java]
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun isLoading(){
        vm.isLoading.observe(this){
            if(it){
                binding.lyLoading.contentLoading.visibility = View.VISIBLE
            }else{
                binding.lyLoading.contentLoading.visibility = View.GONE
            }
        }
    }
    fun initListener(){
        binding.btnTambah.setOnClickListener {
            val nama = binding.etName.text.toString()
            val pin = binding.etPin.text.toString()
            val c_pin = binding.etPin2.text.toString()
            if (nama.isEmpty()){
                binding.etName.error = "Nama tidak boleh kosong"
                binding.etName.requestFocus()
                return@setOnClickListener
            }
            if (pin.isEmpty()){
                binding.etPin.error = "Pin tidak boleh kosong"
                binding.etPin.requestFocus()
                return@setOnClickListener
            }
            if (c_pin.isEmpty()){
                binding.etPin2.error = "Pin tidak boleh kosong"
                binding.etPin2.requestFocus()
                return@setOnClickListener
            }
            if (pin != c_pin){
                binding.etPin2.error = "Pin tidak sama"
                binding.etPin2.requestFocus()
                return@setOnClickListener
            }
            vm.sendData.observe(this){
                if (it != null){
                    finish()
                }
            }
            val user = UserModel()
            user.name = nama
            user.pin = pin
            vm.postData( user,this)
        }
    }
}