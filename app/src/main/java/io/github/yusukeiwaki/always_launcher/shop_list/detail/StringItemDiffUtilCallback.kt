package io.github.yusukeiwaki.always_launcher.shop_list.detail

import androidx.recyclerview.widget.DiffUtil

class StringItemDiffUtilCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
