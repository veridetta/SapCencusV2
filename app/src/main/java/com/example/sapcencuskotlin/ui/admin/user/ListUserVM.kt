package com.example.sapcencuskotlin.ui.admin.user

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sapcencuskotlin.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ListUserVM(application: Application): AndroidViewModel(application) {
    private val _getData = MutableLiveData<MutableList<UserModel>>()
    val getData :MutableLiveData<MutableList<UserModel>> = _getData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading :MutableLiveData<Boolean> = _isLoading
    val list = mutableListOf<UserModel>()
    private val _idle = MutableLiveData<Boolean>()
    val idle :MutableLiveData<Boolean> = _idle
    fun getData(context:Context){
        val fbFS = FirebaseFirestore.getInstance()
        _isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).run {
            fbFS.collection("user")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        _isLoading.postValue(false)
                        return@addSnapshotListener
                    }

                    list.clear()
                    for (item in value!!) {
                        val data = item.toObject(UserModel::class.java)
                        data.docid = item.id
                        list.add(data)
                    }
                    _getData.postValue(list)
                    _isLoading.postValue(false)
                }
            _isLoading.postValue(false)
        }

    }
}