package com.deso1.daoanhduc.dlu_21a100100093

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.ComponentActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddFoodActivity : ComponentActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var imgProduct: ImageView
    private var imageUri: Uri? = null
    private var savedImagePath: String = ""
    private var productId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        databaseHelper = DatabaseHelper(this)

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtPrice = findViewById<EditText>(R.id.edtPrice)
        val edtUnit = findViewById<EditText>(R.id.edtUnit)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnUploadImage = findViewById<Button>(R.id.btnUploadImage)
        imgProduct = findViewById(R.id.imgProduct)

        // Nhận dữ liệu sản phẩm nếu đang chỉnh sửa
        productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            val product = databaseHelper.getProductById(productId!!)
            edtName.setText(product.name)
            edtPrice.setText(product.price.toString())
            edtUnit.setText(product.unit)
            savedImagePath = product.imagePath
            if (product.imagePath.isNotEmpty()) {
                val imgFile = File(product.imagePath)
                if (imgFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    imgProduct.setImageBitmap(bitmap)
                }
            }
            btnSave.text = "Cập nhật sản phẩm"
        }

        btnUploadImage.setOnClickListener {
            openImageChooser()
        }

        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            val price = edtPrice.text.toString().toDoubleOrNull() ?: 0.0
            val unit = edtUnit.text.toString()

            if (productId != -1) {
                // Cập nhật sản phẩm
                databaseHelper.updateProduct(productId!!, name, price, unit, savedImagePath)
            } else {
                // Thêm sản phẩm mới
                databaseHelper.addProduct(name, price, unit, savedImagePath)
            }
            finish() // Quay lại MainActivity
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imgProduct.setImageURI(imageUri)

            // Lưu ảnh vào bộ nhớ ứng dụng
            savedImagePath = saveImageToInternalStorage(imageUri!!)
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val fileName = "product_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }

        return file.absolutePath
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
