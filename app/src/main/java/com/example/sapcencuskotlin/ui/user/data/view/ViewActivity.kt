package com.example.sapcencuskotlin.ui.user.data.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.adapter.BiodataAdapter
import com.example.sapcencuskotlin.adapter.UserAdapter
import com.example.sapcencuskotlin.databinding.ActivityListUserBinding
import com.example.sapcencuskotlin.databinding.ActivityViewBinding
import com.example.sapcencuskotlin.helper.saveTempUser
import com.example.sapcencuskotlin.model.DataModel
import com.example.sapcencuskotlin.model.DataPenduduk
import com.example.sapcencuskotlin.model.UserModel
import com.example.sapcencuskotlin.ui.admin.adduser.AddUserActivity
import com.example.sapcencuskotlin.ui.admin.edituser.EditUserActivity
import com.example.sapcencuskotlin.ui.admin.user.ListUserVM
import com.example.sapcencuskotlin.ui.user.data.cari.CariActivity
import com.example.sapcencuskotlin.ui.user.data.edit.EditDataActivity

class ViewActivity : AppCompatActivity() {
    lateinit var vm: VM
    lateinit var binding: ActivityViewBinding
    lateinit var adapter: BiodataAdapter
    lateinit var data: List<DataPenduduk>
    lateinit var recyclerView: RecyclerView
    private val dataList: MutableList<DataPenduduk> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initRv()
        isLoading()
        initListener()
    }
    fun initView(){
        vm = ViewModelProvider(this)[VM::class.java]
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun initRv(){
        vm.getData.observe(this) { getData ->
            data = getData
            dataList.clear()
            dataList.addAll(data)
            adapter = BiodataAdapter(dataList, this) { barang -> editBarang(barang) }
            binding.rvPenduduk.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(this@ViewActivity, 1)

            }

            binding.rvPenduduk.adapter = adapter
            adapter.filter("")
            binding.btnCari.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    adapter.filter(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
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
        initRv()
    }
    fun initListener(){
        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, CariActivity::class.java)
            startActivity(intent)
        }
    }
    fun editBarang(barang: DataPenduduk) {
        val intent = Intent(this@ViewActivity, EditDataActivity::class.java)
        intent.putExtra("nik", barang.nik)
        startActivity(intent)
    }
}