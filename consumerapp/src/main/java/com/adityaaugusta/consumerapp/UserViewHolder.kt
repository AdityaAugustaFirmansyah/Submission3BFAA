package com.adityaaugusta.consumerapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adityaaugusta.consumerapp.databinding.ItemUserBinding
import com.adityaaugusta.consumerapp.domain.User
import com.bumptech.glide.Glide

class UserViewHolder(itemView: View, private val mListener: (User) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private var itemUserBinding: ItemUserBinding? = ItemUserBinding.bind(itemView)

    fun bindItem(user: User) {
        itemUserBinding?.let {
            Glide.with(itemView).load(user.avatar).into(it.imgView)
            it.tvUserName.text = user.username
        }
        itemUserBinding?.let {
            it.layout.setOnClickListener {
                mListener.invoke(user)
            }
        }
    }
}
