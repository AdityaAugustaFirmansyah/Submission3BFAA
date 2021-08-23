package com.adityaaugusta.githubuser.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adityaaugusta.githubuser.R
import com.adityaaugusta.githubuser.domain.User

class AdapterUser(private var users:List<User>, private val mListener:(User)->Unit): RecyclerView.Adapter<UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return UserViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindItem(users[position])
    }
    override fun getItemCount(): Int = users.size
}