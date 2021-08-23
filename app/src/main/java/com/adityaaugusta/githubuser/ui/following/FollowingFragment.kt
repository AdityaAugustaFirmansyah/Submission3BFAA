package com.adityaaugusta.githubuser.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adityaaugusta.githubuser.databinding.FragmentListBinding
import com.adityaaugusta.githubuser.domain.User
import com.adityaaugusta.githubuser.ui.UsernameViewModelFactory
import com.adityaaugusta.githubuser.ui.AdapterUser
import com.adityaaugusta.githubuser.ui.detail.DetailFragment
import com.adityaaugusta.githubuser.utils.showView

class FollowingFragment : Fragment() {

    private lateinit var fragmentListBinding: FragmentListBinding
    private var user:User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(DetailFragment.DATA_USER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentListBinding = FragmentListBinding.inflate(inflater)
        return fragmentListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentListBinding.toolbar.visibility = View.GONE

        user?.let { it ->
            val factory = activity?.let { it1 ->
                UsernameViewModelFactory(it.username,
                    it1.application)
            }
            val search = factory?.let { it1 ->
                ViewModelProvider(this,
                    it1
                ).get(FollowingViewModel::class.java)
            }
            fragmentListBinding.rvUser.layoutManager = LinearLayoutManager(context)


            search?.getState()?.observe(viewLifecycleOwner,{
                it?.let {
                    if (!it.success.isNullOrEmpty()){
                        fragmentListBinding.rvUser.visibility = View.VISIBLE
                        val adapterUser = AdapterUser(it.success) {}
                        fragmentListBinding.rvUser.adapter = adapterUser
                        fragmentListBinding.progressBar.visibility = View.GONE
                        showView(true,fragmentListBinding.rvUser,fragmentListBinding.tvError)
                    }else if (it.loading){
                        fragmentListBinding.progressBar.visibility = View.VISIBLE
                        fragmentListBinding.rvUser.visibility = View.GONE
                    }else if (it.msg.isNotEmpty()){
                        showView(false,fragmentListBinding.rvUser,fragmentListBinding.tvError)
                        fragmentListBinding.tvError.text = it.msg
                        fragmentListBinding.progressBar.visibility = View.GONE
                        Toast.makeText(context,it.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}