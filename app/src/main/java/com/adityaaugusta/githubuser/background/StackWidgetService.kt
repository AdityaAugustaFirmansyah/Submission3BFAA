package com.adityaaugusta.githubuser.background

import android.content.Intent
import android.widget.RemoteViewsService
import com.adityaaugusta.githubuser.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService(){
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory
    =StackRemoteViewsFactory(this.application)

}
