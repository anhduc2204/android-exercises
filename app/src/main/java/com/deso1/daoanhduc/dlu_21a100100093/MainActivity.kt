package com.deso1.daoanhduc.dlu_21a100100093

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.deso1.daoanhduc.dlu_21a100100093.model.Food

class MainActivity : ComponentActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: FoodtListAdapter
    private lateinit var productList: MutableList<Food>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        productList = mutableListOf()
        adapter = FoodtListAdapter(this, productList)

        val lvProducts = findViewById<ListView>(R.id.lvProducts)
        val btnAddProduct = findViewById<Button>(R.id.btnAdd)

        lvProducts.adapter = adapter
        loadProducts()

        btnAddProduct.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        productList.clear()
        productList.addAll(databaseHelper.getAllProducts())
        adapter.notifyDataSetChanged()
    }
}
