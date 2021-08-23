package com.adityaaugusta.githubuser.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adityaaugusta.githubuser.R
import com.adityaaugusta.githubuser.databinding.FragmentHomeBinding
import com.adityaaugusta.githubuser.ui.AdapterUser
import com.adityaaugusta.githubuser.ui.detail.DetailFragment
import com.adityaaugusta.githubuser.utils.showView

class HomeFragment : Fragment() {

    private lateinit var adapterUser: AdapterUser
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        fragmentHomeBinding.rvUser.layoutManager = LinearLayoutManager(context)


        homeViewModel.getUsers().observe(viewLifecycleOwner, {
            it?.let {
                if (!it.success.isNullOrEmpty()) {
                    adapterUser = AdapterUser(it.success) {
                        val bundle = Bundle()
                        bundle.putParcelable(DetailFragment.DATA_USER, it)
                        findNavController().navigate(
                            R.id.action_homeContainerFragment_to_detailFragment,
                            bundle
                        )
                    }
                    fragmentHomeBinding.rvUser.adapter = adapterUser
                    fragmentHomeBinding.progressBar.visibility = View.GONE
                    showView(true, fragmentHomeBinding.rvUser, fragmentHomeBinding.tvError)
                } else if (it.loading) {
                    fragmentHomeBinding.progressBar.visibility = View.VISIBLE
                    fragmentHomeBinding.rvUser.visibility = View.GONE
                } else if (it.msg.isNotEmpty()) {
                    showView(false, fragmentHomeBinding.rvUser, fragmentHomeBinding.tvError)
                    fragmentHomeBinding.tvError.text = it.msg
                    fragmentHomeBinding.progressBar.visibility = View.GONE
                }
            }
        })
        bindMenu()
    }

    private fun bindMenu() {
        fragmentHomeBinding.toolbar3.inflateMenu(R.menu.menu_tollbar_home)

        val item = fragmentHomeBinding.toolbar3.menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = item.actionView as SearchView
        val settingItem = fragmentHomeBinding.toolbar3.menu.findItem(R.id.setting)

        settingItem.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_homeContainerFragment_to_settingFragment)
            true
        }

        searchView.setBackgroundColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (this@HomeFragment::adapterUser.isInitialized) {
                    p0?.let {
                        homeViewModel.stateSearchUser(
                            it,
                            getString(R.string.user_not_found)
                        )
                    }
                }
                return true
            }
        })
    }
}