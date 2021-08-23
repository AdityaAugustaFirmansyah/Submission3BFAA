package com.adityaaugusta.githubuser.widget

import android.app.Application
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.adityaaugusta.githubuser.R
import com.adityaaugusta.githubuser.database.UserDao
import com.adityaaugusta.githubuser.database.getDatabase
import com.adityaaugusta.githubuser.domain.User

class StackRemoteViewsFactory(private val application: Application):RemoteViewsService.RemoteViewsFactory {

    private lateinit var userDao:UserDao
    private val users = mutableListOf<User>()

    override fun onCreate() {
        userDao = getDatabase(application).userDao()
    }

    override fun onDataSetChanged() {
        for(i in userDao.getUsers()){
            users.add(User(username = i.username,avatar = i.avatar))
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = users.size

    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(application.packageName,R.layout.widget_item)
        rv.setTextViewText(R.id.textView, users[p0].username)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int =1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean =false
}