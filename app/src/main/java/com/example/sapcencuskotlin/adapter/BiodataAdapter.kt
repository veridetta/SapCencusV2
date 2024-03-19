package com.example.sapcencuskotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.databinding.ItemDataBinding
import com.example.sapcencuskotlin.databinding.ItemUserBinding
import com.example.sapcencuskotlin.model.DataPenduduk
import com.example.sapcencuskotlin.model.UserModel
import java.util.Locale

class BiodataAdapter(
    private var barangList: MutableList<DataPenduduk>,
    val context: Context, 
    private val onEditClickListener: (DataPenduduk) -> Unit,
) : RecyclerView.Adapter<BiodataAdapter.ProductViewHolder>() {
    var filteredBarangList: MutableList<DataPenduduk> = mutableListOf()
    init {
        filteredBarangList.addAll(barangList)
        //log data urutan pertama dan kedua
        Log.d("Data 1", filteredBarangList[0].nama.toString())
        Log.d("Data 2", filteredBarangList[1].nama.toString())
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && filteredBarangList.isEmpty()) {
            1 // Return 1 for empty state view
        } else {
            0 // Return 0 for regular product view
        }
    }
    fun filter(query: String) {
        filteredBarangList.clear()
        if (query !== null || query !=="") {
            val lowerCaseQuery = query.toLowerCase(Locale.getDefault())
            for (product in barangList) {
                val nam = product.nama?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery)
                val nik = product.nik?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery)
                Log.d("Kunci ", lowerCaseQuery)
                if (nam == true || nik == true) {
                    filteredBarangList.add(product)
                    Log.d("Ada ", product.nama.toString())
                }
            }
        } else {
            filteredBarangList.clear()
            filteredBarangList.addAll(barangList)
        }
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filteredBarangList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentBarang = filteredBarangList[position]

        holder.tvName.text = currentBarang.nama
        holder.tvNik.text = currentBarang.nik

        holder.btnUbah.setOnClickListener { onEditClickListener(currentBarang) }

    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvNik: TextView = itemView.findViewById(R.id.tv_nik)
        val btnUbah: ImageView = itemView.findViewById(R.id.iv_edit)
    }
}