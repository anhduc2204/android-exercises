package com.deso1.daoanhduc.dlu_21a100100093

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.deso1.daoanhduc.dlu_21a100100093.model.Food
import java.io.File

class FoodtListAdapter(private val context: Context, private val productList: List<Food>) : BaseAdapter() {

    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Any = productList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_food_item, parent, false)

        val imgProduct = view.findViewById<ImageView>(R.id.imgProduct)
        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
//        val txtUnit = view.findViewById<TextView>(R.id.txtUnit)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)

        val product = productList[position]

        txtName.text = product.name
        txtPrice.text = "${product.price} ${product.unit}"
//        txtUnit.text = product.unit

        // Kiểm tra nếu có đường dẫn ảnh, đọc từ bộ nhớ trong
        if (product.imagePath.isNotEmpty()) {
            val imgFile = File(product.imagePath)
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                imgProduct.setImageBitmap(bitmap)
            } else {
                imgProduct.setImageResource(R.drawable.image_default) // Ảnh mặc định nếu không tìm thấy
            }
        } else {
            imgProduct.setImageResource(R.drawable.image_default)
        }

        // Xử lý sự kiện nhấn nút sửa
        btnEdit.setOnClickListener {
            val intent = Intent(context, AddFoodActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            context.startActivity(intent)
        }

        return view
    }
}
