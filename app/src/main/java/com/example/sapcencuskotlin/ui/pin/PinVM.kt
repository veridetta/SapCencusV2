package com.example.sapcencuskotlin.ui.pin

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sapcencuskotlin.helper.saveUser
import com.example.sapcencuskotlin.helper.showSnackbar
import com.example.sapcencuskotlin.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class PinVM(application: Application): AndroidViewModel(application) {
    private val _idle = MutableLiveData<Boolean>()
    val idle : MutableLiveData<Boolean> = _idle
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading
    fun checkPin(context: Context,userModel: UserModel){
        val fbFS = FirebaseFirestore.getInstance()
        _isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).run {
            fbFS.collection("user")
                .whereEqualTo("pin",userModel.pin)
                .limit(1)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        _isLoading.postValue(false)
                        _idle.postValue(false)
                        showSnackbar(context,"Terjadi kesalahan, silahkan coba lagi")
                        return@addSnapshotListener
                    }
                    //data pertama
                    val data = value?.documents?.get(0)
                    val hasil = data?.toObject(UserModel::class.java)
                    hasil?.docid = data?.id
                    hasil?.isLogin = true
                    saveUser(context,hasil!!)
                    _idle.postValue(true)
                    _isLoading.postValue(false)
                }
        }


    }
}