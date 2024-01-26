package com.example.sapcencuskotlin.ui.user.ktp.scan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.ui.user.ktp.result.ResultActivity
import com.example.sapcencuskotlin.graph.GraphicOverlay
import com.example.sapcencuskotlin.graph.TextGraphic
import com.example.sapcencuskotlin.ocr.GenerateKTPData
import com.example.sapcencuskotlin.permissionkit.askPermissions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.marchinram.rxgallery.RxGallery
import timber.log.Timber
import java.util.PriorityQueue


class MainActivity : AppCompatActivity(){
    val TAG = "MainActivity"
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
    lateinit var textoutput:TextView
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
                    Toast.makeText(this@MainActivity, "Mohon pilih gambar", Toast.LENGTH_SHORT).show()
                }

            }, { failed ->
                failed.message?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@MainActivity, "Granted", Toast.LENGTH_SHORT).show()
            }

            onDenied {
                Toast.makeText(this@MainActivity, "Mohon izinkan", Toast.LENGTH_SHORT).show()
            }

            onShowRationale {
                Toast.makeText(this@MainActivity, "Mohon Izinkan", Toast.LENGTH_SHORT).show()
            }

            onNeverAskAgain {
                Toast.makeText(this@MainActivity, "Mohon Izinkan", Toast.LENGTH_SHORT).show()
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
        val intent = Intent(this, ResultActivity::class.java)
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
        startActivity(intent)

    }

    @SuppressLint("CutPasteId")
    private fun showDialogResult() {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_result)
        val txtProvinsi = dialog.findViewById<TextView>(R.id.tv_provinsi)
        val txtKabKota = dialog.findViewById<TextView>(R.id.tv_kabkota)
        txtProvinsi.text = ktpData.getProvinsi()
        txtKabKota.text = ktpData.getKabupatenKota()
        val txtIdentifierNIK =
            dialog.findViewById<View>(R.id.nik).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierNama =
            dialog.findViewById<View>(R.id.nama).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierTl =
            dialog.findViewById<View>(R.id.tl).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierTgl =
            dialog.findViewById<View>(R.id.tgl).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierJK =
            dialog.findViewById<View>(R.id.jk).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierAlamat =
            dialog.findViewById<View>(R.id.alamat).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierRtRw =
            dialog.findViewById<View>(R.id.rtrw).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierKD =
            dialog.findViewById<View>(R.id.kd).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierKec =
            dialog.findViewById<View>(R.id.kec).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierAgama =
            dialog.findViewById<View>(R.id.agama).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierSP =
            dialog.findViewById<View>(R.id.sp).findViewById<TextView>(R.id.tv_identifier)
        val txtIdentifierGD =
            dialog.findViewById<View>(R.id.gd).findViewById<TextView>(R.id.tv_identifier)
        txtIdentifierNIK.text = "NIK"
        txtIdentifierNama.text = "Nama"
        txtIdentifierTl.text = "Tempat Lahir"
        txtIdentifierTgl.text = "Tanggal Lahir"
        txtIdentifierJK.text = "Jenis Kelamin"
        txtIdentifierAlamat.text = "Alamat"
        txtIdentifierRtRw.text = "RT/RW"
        txtIdentifierKD.text = "Kel/Desa"
        txtIdentifierKec.text = "Kecamatan"
        txtIdentifierAgama.text = "Agama"
        txtIdentifierSP.text = "Status Perkawinan"
        txtIdentifierGD.text = "Golongan Darah"
        val txtValueNIK = dialog.findViewById<View>(R.id.nik).findViewById<TextView>(R.id.tv_value)
        val txtValueNama =
            dialog.findViewById<View>(R.id.nama).findViewById<TextView>(R.id.tv_value)
        val txtValueTl = dialog.findViewById<View>(R.id.tl).findViewById<TextView>(R.id.tv_value)
        val txtValueTgl = dialog.findViewById<View>(R.id.tgl).findViewById<TextView>(R.id.tv_value)
        val txtValueJK = dialog.findViewById<View>(R.id.jk).findViewById<TextView>(R.id.tv_value)
        val txtValueAlamat =
            dialog.findViewById<View>(R.id.alamat).findViewById<TextView>(R.id.tv_value)
        val txtValueRtRw =
            dialog.findViewById<View>(R.id.rtrw).findViewById<TextView>(R.id.tv_value)
        val txtValueKD = dialog.findViewById<View>(R.id.kd).findViewById<TextView>(R.id.tv_value)
        val txtValueKec = dialog.findViewById<View>(R.id.kec).findViewById<TextView>(R.id.tv_value)
        val txtValueAgama =
            dialog.findViewById<View>(R.id.agama).findViewById<TextView>(R.id.tv_value)
        val txtValueSP = dialog.findViewById<View>(R.id.sp).findViewById<TextView>(R.id.tv_value)
        val txtValueGD = dialog.findViewById<View>(R.id.gd).findViewById<TextView>(R.id.tv_value)
        txtValueNIK.setText(ktpData.getNik()?.value)
        txtValueNama.setText(ktpData.getNama()?.value)
        txtValueTl.setText(ktpData.getTempatLahir()?.value)
        txtValueTgl.setText(ktpData.getTanggalLahir()?.value)
        txtValueJK.setText(ktpData.getJenisKelamin()?.value)
        txtValueAlamat.setText(ktpData.getAlamat()?.value)
        txtValueRtRw.setText(ktpData.getRtRw()?.value)
        txtValueKD.setText(ktpData.getKelDesa()?.value)
        txtValueKec.setText(ktpData.getKecamatan()?.value)
        txtValueAgama.setText(ktpData.getAgama()?.value)
        txtValueSP.setText(ktpData.getStatusPerkawinan()?.value)
        txtValueGD.setText(ktpData.getGolonganDarah()?.value)
        dialog.show()
    }

    private fun processTextRecognitionResult(texts: Text) {
        val blocks = texts.textBlocks
        //        String dataAwal = texts.getText();
        if (blocks.size == 0) {
            showToast("No text found")
            return
        }
        mGraphicOverlay.clear()
        for (i in blocks.indices) {
            val blockData = blocks[i].text
            val blockCornerPoints = blocks[i].cornerPoints
            val blockFrame = blocks[i].boundingBox
            //            Log.d("Corner point block", Arrays.toString(blockCornerPoints));
//            Log.d("frame block", blockFrame.toString());
//            Log.d("Blok ke-", blockData);
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val text = lines[j].text
                val lineCornerPoints = lines[j].cornerPoints
                val lineFrame = lines[j].boundingBox
                //                Log.d("Corner point", Arrays.toString(lineCornerPoints));
//                Log.d("frame", lineFrame.toString());
                Log.d("Baris ke-", text)
                val elements = lines[j].elements
                for (k in elements.indices) {
                    val textGraphic: GraphicOverlay.Graphic = TextGraphic(mGraphicOverlay, elements[k])
                    mGraphicOverlay.add(textGraphic)
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    // Functions for loading images from app assets.

    // Functions for loading images from app assets.
    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
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

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
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

    // Gets the targeted width / height.
    private fun getTargetedWidthHeight(): Pair<Int, Int> {
        val targetWidth: Int
        val targetHeight: Int
        val maxWidthForPortraitMode = getImageMaxWidth()
        val maxHeightForPortraitMode = getImageMaxHeight()
        targetWidth = maxWidthForPortraitMode
        targetHeight = maxHeightForPortraitMode
        return Pair(targetWidth, targetHeight)
    }
}