package com.adityaaugusta.githubuser.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.adityaaugusta.githubuser.database.UserDao
import com.adityaaugusta.githubuser.database.getDatabase
import com.adityaaugusta.githubuser.entity.AUTHORITY
import com.adityaaugusta.githubuser.entity.TABLE_NAME

class GithubUserProvider : ContentProvider() {

    companion object{
        private const val GITHUB_USER = 1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userDao: UserDao
    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, GITHUB_USER)
    }

    override fun onCreate(): Boolean {
        userDao = getDatabase(context?.applicationContext as Application).userDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return if (sUriMatcher.match(uri) == GITHUB_USER) userDao.selectAll() else null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}