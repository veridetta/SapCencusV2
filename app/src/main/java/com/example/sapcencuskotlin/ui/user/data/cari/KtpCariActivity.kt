package com.example.sapcencuskotlin.ui.user.data.cari

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.graph.GraphicOverlay
import com.example.sapcencuskotlin.helper.saveKTP
import com.example.sapcencuskotlin.model.KTPModel
import com.example.sapcencuskotlin.ocr.GenerateKTPData
import com.example.sapcencuskotlin.permissionkit.askPermissions
import com.example.sapcencuskotlin.ui.user.data.edit.EditDataActivity
import com.example.sapcencuskotlin.ui.user.ktp.result.ResultActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.marchinram.rxgallery.RxGallery
import timber.log.Timber
import java.util.PriorityQueue

class KtpCariActivity : AppCompatActivity(){
    val TAG = "KtpScanActivity"
    private lateinit var mImageView: ImageView
    private lateinit var  mSelectedImage: Bitmap
    private lateinit var mGraphicOverlay: GraphicOverlay
    private val RESULTS_TO_SHOW = 3
    lateinit var lyProses : CoordinatorLayout
    private var mImageMaxWidth: Int = 0

    private  var mImageMaxHeight: Int = 0

    private var finishScan = false

    private val sortedLabels = PriorityQueue<Map.Entry<String, Float>>(
        RESULTS_TO_SHOW, object : Comparator<Map.Entry<String, Float>> {
            override fun compare(
                o1: Map.Entry<String, Float>,
                o2: Map.Entry<String, Float>
            ): Int {
                return o1.value.compareTo(o2.value)
            }
        }
    )
    private lateinit var uriPath: Uri
    lateinit var btnCamera: Button
    lateinit var btnGallery: Button
    lateinit var textoutput: TextView
    private val GALLERY_REQUEST_CODE = 1
    private var isCamera = false
    lateinit var ktpData: GenerateKTPData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initview()
        supportActionBar?.hide()

        btnCamera.setOnClickListener {
            //showBottomView()
            RxGallery.photoCapture(this).subscribe({ uriPhoto ->
                Timber.d(uriPhoto.toString())
                uriPath = uriPhoto
                Log.d("MainActivity", "onCreate: $uriPath")
                if (::uriPath.isInitialized) {
                    isCamera = true
                    val bitmap = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(uriPath)
                    )
                    mSelectedImage = bitmap
                    runTextRecognition()
                } else {
                    Toast.makeText(this@KtpCariActivity, "Mohon pilih gambar", Toast.LENGTH_SHORT).show()
                }

            }, { failed ->
                failed.message?.let {
                    Toast.makeText(this@KtpCariActivity, it, Toast.LENGTH_SHORT).show()
                }
            })
        }
        btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)

        }
        reqPermission()
    }
    fun initview(){

        btnCamera = findViewById(R.id.btnCamera)
        btnGallery = findViewById(R.id.btnGalerry)
        lyProses = findViewById(R.id.lyProses)
        lyProses.visibility = View.GONE

    }
    private fun reqPermission() {
        askPermissions(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            onGranted {
                Toast.makeText(this@KtpCariActivity, "Granted", Toast.LENGTH_SHORT).show()
            }

            onDenied {
                Toast.makeText(this@KtpCariActivity, "Mohon izinkan", Toast.LENGTH_SHORT).show()
            }

            onShowRationale {
                Toast.makeText(this@KtpCariActivity, "Mohon Izinkan", Toast.LENGTH_SHORT).show()
            }

            onNeverAskAgain {
                Toast.makeText(this@KtpCariActivity, "Mohon Izinkan", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val selectedImage = data?.data
                    //Glide.with(this).load(selectedImage).into(imageView)

                    val imageFilePath = getImageFilePath(selectedImage) // Mendapatkan jalur file gambar dari URI

                    if (imageFilePath != null) {
                        uriPath = selectedImage!!
                        val bitmap = BitmapFactory.decodeStream(
                            contentResolver.openInputStream(selectedImage)
                        )
                        mSelectedImage = bitmap
                        runTextRecognition()
                    } else {
                        println("Failed to get image file path.")
                    }
                }
            }
        }
    }
    private fun getImageFilePath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath = columnIndex?.let { cursor?.getString(it) }
        cursor?.close()
        return filePath
    }

    private fun runTextRecognition() {
        finishScan = false
        lyProses.visibility = View.VISIBLE
        val image = InputImage.fromBitmap(mSelectedImage, 0)
        val textRecognizerOptions = TextRecognizerOptions.Builder()
        val options = textRecognizerOptions.build()
        val recognizer = TextRecognition.getClient(options)
        recognizer.process(image)
            .addOnSuccessListener { texts ->
                //                                processTextRecognitionResult(texts);
                processTextRecognitionResultNew(texts)
                //                                ktpData = new GenerateKTPData(texts);
            }
            .addOnFailureListener { e -> // Task failed with an exception
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResultNew(texts: Text) {
        val results: MutableList<String> = ArrayList()
        //        Log.d("Semua", texts.getText());
        for (block in texts.textBlocks) {
//            results.add(block.getText());
            for (line in block.lines) {
                results.add(line.text)
            }
        }
        ktpData = GenerateKTPData(results, object : GenerateKTPData.Listener {
            override fun finishScan() {
                finishScan = true
                lyProses.visibility = View.GONE
            }
        })
        if (ktpData != null) {
            //showDialogResult()
            //intent ke resutl
            inten()

        }
//        Log.d("Hasilnya", Arrays.toString(results.toArray()));
    }
    fun inten(){
        val intent = Intent(this, EditDataActivity::class.java)
        intent.putExtra("nik", ktpData.getNik()?.value)
        intent.putExtra("nama", ktpData.getNama()?.value)
        intent.putExtra("tempatLahir", ktpData.getTempatLahir()?.value)
        intent.putExtra("tanggalLahir", ktpData.getTanggalLahir()?.value)
        intent.putExtra("jenisKelamin", ktpData.getJenisKelamin()?.value)
        intent.putExtra("alamat", ktpData.getAlamat()?.value)
        intent.putExtra("rtRw", ktpData.getRtRw()?.value)
        intent.putExtra("kelDesa", ktpData.getKelDesa()?.value)
        intent.putExtra("kecamatan", ktpData.getKecamatan()?.value)
        intent.putExtra("agama", ktpData.getAgama()?.value)
        intent.putExtra("statusPerkawinan", ktpData.getStatusPerkawinan()?.value)
        intent.putExtra("golonganDarah", ktpData.getGolonganDarah()?.value)
        intent.putExtra("pekerjaan", ktpData.getPekerjaan()?.value)
        intent.putExtra("kewarganegaraan", ktpData.getKewarganegaraan()?.value)
        val ktp = KTPModel()
        ktp.nik = ktpData.getNik()?.value
        ktp.nama_lengkap = ktpData.getNama()?.value
        ktp.jenis_kelamin = ktpData.getJenisKelamin()?.value
        ktp.agama = ktpData.getAgama()?.value
        ktp.tempat_lahir = ktpData.getTempatLahir()?.value
        ktp.tanggal_lahir = ktpData.getTanggalLahir()?.value
        ktp.pekerjaan = ktpData.getPekerjaan()?.value
        ktp.status_wni = ktpData.getKewarganegaraan()?.value
        val rtRw = ktpData.getRtRw().toString().split("/")
        if (rtRw.size > 1) {
            ktp.rw = rtRw[1]
            ktp.rt = rtRw[0]
        }
        ktp.status_kawin = ktpData.getStatusPerkawinan()?.value
        ktp.goldar = ktpData.getGolonganDarah()?.value
        saveKTP(this, ktp)
        startActivity(intent)
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun getImageMaxWidth(): Int {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxWidth = mImageView.width
        }
        return mImageMaxWidth
    }
    private fun getImageMaxHeight(): Int {
        if (mImageMaxHeight == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxHeight = mImageView.height
        }
        return mImageMaxHeight
    }

}