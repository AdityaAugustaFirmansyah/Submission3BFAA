package com.adityaaugusta.githubuser.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adityaaugusta.githubuser.databinding.ItemUserBinding
import com.adityaaugusta.githubuser.domain.User
import com.bumptech.glide.Glide

class UserViewHolder(itemView: View, private val mListener: (User) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private var itemUserBinding: ItemUserBinding? = ItemUserBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
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
