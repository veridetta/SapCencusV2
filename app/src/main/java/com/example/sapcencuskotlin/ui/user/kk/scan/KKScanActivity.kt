package com.example.sapcencuskotlin.ui.user.kk.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlin.Pair
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ActivityKkscanBinding
import com.example.sapcencuskotlin.databinding.ActivityUserBinding
import com.example.sapcencuskotlin.graph.GraphicOverlay
import com.example.sapcencuskotlin.helper.clearKK
import com.example.sapcencuskotlin.helper.clearKTP
import com.example.sapcencuskotlin.helper.saveKK
import com.example.sapcencuskotlin.model.KKModel
import com.example.sapcencuskotlin.permissionkit.askPermissions
import com.example.sapcencuskotlin.ui.user.kk.result.ResultKKActivity
import com.example.sapcencuskotlin.ui.user.ktp.result.ResultActivity
import com.example.sapcencuskotlin.ui.user.ktp.scan.KtpScanActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.marchinram.rxgallery.RxGallery
import timber.log.Timber
import java.util.Arrays
import java.util.PriorityQueue

class KKScanActivity : AppCompatActivity() {
    val TAG = "KKScanActivity"
    lateinit var binding : ActivityKkscanBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKkscanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
        supportActionBar?.hide()

        btnCamera.setOnClickListener {
            //showBottomView()
            RxGallery.photoCapture(this).subscribe({ uriPhoto ->
                Timber.d(uriPhoto.toString())
                uriPath = uriPhoto
                Log.d("KKActivity", "onCreate: $uriPath")
                if (::uriPath.isInitialized) {
                    isCamera = true
                    val bitmap = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(uriPath)
                    )
                    mSelectedImage = bitmap
                    runTextRecognition()
                } else {
                    Toast.makeText(this@KKScanActivity, "Mohon pilih gambar", Toast.LENGTH_SHORT).show()
                }

            }, { failed ->
                failed.message?.let {
                    Toast.makeText(this@KKScanActivity, it, Toast.LENGTH_SHORT).show()
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

        btnCamera = binding.btnCamera
        btnGallery = binding.btnGalerry
        lyProses = binding.lyProses.lyProses
        lyProses.visibility = View.GONE
        binding.lyConfirm.contentConfirm.visibility = View.GONE
//        binding.lyConfirm.contentConfirm.visibility = View.VISIBLE
        binding.lyConfirm.btnYa.setOnClickListener {
            binding.lyConfirm.contentConfirm.visibility = View.GONE
        }
        binding.lyConfirm.btnTidak.setOnClickListener {
            clearKK(this)
            val intent = Intent(this, ResultKKActivity::class.java)
            startActivity(intent)
        }

    }
    private fun reqPermission() {
        askPermissions(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            onGranted {
                Toast.makeText(this@KKScanActivity, "Granted", Toast.LENGTH_SHORT).show()
            }

            onDenied {
                Toast.makeText(this@KKScanActivity, "Mohon izinkan", Toast.LENGTH_SHORT).show()
            }

            onShowRationale {
                Toast.makeText(this@KKScanActivity, "Mohon Izinkan", Toast.LENGTH_SHORT).show()
            }

            onNeverAskAgain {
                Toast.makeText(this@KKScanActivity, "Mohon Izinkan", Toast.LENGTH_SHORT).show()
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
        Log.d("Semua", texts.getText());
        for (block in texts.textBlocks) {
//            results.add(block.getText());
            for (line in block.lines) {
                results.add(line.text)
            }
        }
        //array to string
        val resultArray = results.toTypedArray()
        val lines = resultArray
        Log.d("Lines", Arrays.toString(lines))
        // Pola regex untuk mendeteksi nama lengkap
        val pattern = Regex("""\b(\w+\s\w+)\b""")

        val names = mutableListOf<String>()

        // Cari pola pada setiap baris
        var no = 0
        for (line in lines) {
            val matches = pattern.findAll(line)
            for (matchResult in matches) {
                //ambil 2 nama pertama
                if (no < 2) {
                    val name = matchResult.groupValues[1]
                    val name2 = name.replace("[0-9]".toRegex(), "")
                    names.add(name2)
                    no++
                }
            }
        }
        // Gabungkan semua nama menjadi satu string dipisah koma
        val allNames = names.joinToString(", ")
        val kk = KKModel()
        kk.nama_ayah = names[0]
        kk.nama_ibu = names[1]
        saveKK(this,kk)
        //intent
        val intent = Intent(this, ResultKKActivity::class.java)
        intent.putExtra("nama_ayah", names[0])
        intent.putExtra("nama_ibu", names[1])
        startActivity(intent)
        finish()
        Log.d("Hasilnya", allNames)
        Log.d("Hasilnya", Arrays.toString(resultArray))

    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    fun extractNames(input: String): List<String> {
        val firstTwoNames = input.take(2).map { it.toString() }

        return firstTwoNames
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
    private fun getTargetedWidthHeight(): Pair<Int, Int> {
        val targetWidth: Int
        val targetHeight: Int
        val maxWidthForPortraitMode = getImageMaxWidth()
        val maxHeightForPortraitMode = getImageMaxHeight()
        targetWidth = maxWidthForPortraitMode
        targetHeight = maxHeightForPortraitMode
        return Pair(targetWidth, targetHeight)
    }
    fun extractAllNames(data: String): String {
        val lines = data.split("\n")

        // Pola regex untuk mendeteksi nama lengkap
        val pattern = Regex("""\b(\w+\s\w+)\b""")

        val names = mutableListOf<String>()

        // Cari pola pada setiap baris
        for (line in lines) {
            val matches = pattern.findAll(line)
            for (matchResult in matches) {
                val name = matchResult.groupValues[1]
                names.add(name)
            }
        }

        // Gabungkan semua nama menjadi satu string dipisah koma
        return names.joinToString(", ")
    }
}