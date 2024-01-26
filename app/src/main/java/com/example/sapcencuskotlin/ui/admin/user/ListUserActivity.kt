package com.example.sapcencuskotlin.ui.admin.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.adapter.UserAdapter
import com.example.sapcencuskotlin.databinding.ActivityListUserBinding
import com.example.sapcencuskotlin.helper.saveTempUser
import com.example.sapcencuskotlin.model.UserModel
import com.example.sapcencuskotlin.ui.admin.edituser.EditUserActivity

class ListUserActivity : AppCompatActivity() {
    lateinit var vm: ListUserVM
    lateinit var binding: ActivityListUserBinding
    lateinit var adapter: UserAdapter
    lateinit var data: List<UserModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initRv()
        isLoading()

    }
    fun initView(){
        vm = ViewModelProvider(this)[ListUserVM::class.java]
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun initRv(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        adapter = UserAdapter()
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(item: UserModel) {
                val intent = Intent(this@ListUserActivity, EditUserActivity::class.java)
                saveTempUser(this@ListUserActivity, item)
                startActivity(intent)
            }

        })

        vm.getData.observe(this) { getData ->
            data = getData
            adapter.submitList(data)
        }

        vm.getData(this)
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
    override fun onResume() {
        super.onResume()
        vm.getData(this)
    }
}