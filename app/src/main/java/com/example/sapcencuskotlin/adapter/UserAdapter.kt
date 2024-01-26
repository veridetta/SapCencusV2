package com.example.sapcencuskotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sapcencuskotlin.databinding.ItemUserBinding
import com.example.sapcencuskotlin.model.UserModel

class UserAdapter :ListAdapter<UserModel, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var filteredList: List<UserModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = filteredList[position]
        holder.bind(user)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(user)
        }
    }

    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user:UserModel) {
            binding.apply {
                tvName.text = user.name
            }
        }
    }
    override fun submitList(list: List<UserModel>?) {
        super.submitList(list)
        filteredList = list ?: emptyList()
    }

    interface OnItemClickListener {
        fun onItemClick(item: UserModel)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}