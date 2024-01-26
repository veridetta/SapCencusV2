package com.example.sapcencuskotlin.ui.admin.edituser

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sapcencuskotlin.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class EditUserVM(application: Application): AndroidViewModel(application) {
    private val _sendData = MutableLiveData<UserModel>()
    val sendData :MutableLiveData<UserModel> = _sendData
    private val _idle = MutableLiveData<Boolean>()
    val idle :MutableLiveData<Boolean> = _idle
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading :MutableLiveData<Boolean> = _isLoading

    fun postData(data:UserModel, context:Context){
        val fbFS = FirebaseFirestore.getInstance()
        _isLoading.postValue(true)
        _idle.postValue(false)
        CoroutineScope(Dispatchers.IO).run {
            val uid = UUID.randomUUID().toString()
            val dataSend = hashMapOf(
                "pin" to data.pin,
                "name" to data.name,
            )
            //update data
            fbFS.collection("user")
                .document(data.docid.toString())
                .update(dataSend as Map<String, Any>)
                .addOnSuccessListener {
                    _idle.postValue(true)
                    _isLoading.postValue(false)
                }
        }
    }
}