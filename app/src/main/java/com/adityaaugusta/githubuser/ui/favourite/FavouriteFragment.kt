package com.adityaaugusta.githubuser.ui.favourite

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
import com.adityaaugusta.githubuser.databinding.FragmentListBinding
import com.adityaaugusta.githubuser.ui.AdapterUser
import com.adityaaugusta.githubuser.ui.UsernameViewModelFactory
import com.adityaaugusta.githubuser.ui.detail.DetailFragment

class FavouriteFragment : Fragment() {

    private var viewModel: FavouriteViewModel? = null
    private lateinit var adapterUser: AdapterUser
    private lateinit var fragmentListBinding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentListBinding = FragmentListBinding.inflate(inflater,container,false)
        return fragmentListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory =
            activity?.application?.let { it1 -> UsernameViewModelFactory("", it1) }
        viewModel =
            viewModelFactory?.let { it1 ->
                ViewModelProvider(
                    this,
                    it1
                ).get(FavouriteViewModel::class.java)
            }

        fragmentListBinding.rvUser.layoutManager = LinearLayoutManager(context)

        bindMenu()

        viewModel?.getUserFavourite()?.observe(viewLifecycleOwner,{ it ->
            if (it.size>0){
                adapterUser = AdapterUser(it){
                    val bundle = Bundle()
                    bundle.putParcelable(DetailFragment.DATA_USER, it)
                    findNavController().navigate(R.id.action_homeContainerFragment_to_detailFragment, bundle)
                }
                fragmentListBinding.tvError.visibility = View.GONE
                fragmentListBinding.rvUser.visibility = View.VISIBLE
                fragmentListBinding.rvUser.adapter = adapterUser
            }else{
                fragmentListBinding.tvError.visibility = View.VISIBLE
                fragmentListBinding.rvUser.visibility = View.GONE
                fragmentListBinding.tvError.text = getString(R.string.user_not_found)
            }
        })
    }

    private fun bindMenu(){
        fragmentListBinding.toolbar.inflateMenu(R.menu.menu_search)
        fragmentListBinding.toolbar.navigationIcon = null
        fragmentListBinding.toolbar.title = getString(R.string.favorite)

        val item = fragmentListBinding.toolbar.menu.findItem(R.id.app_bar_search)
        val searchView: SearchView =item.actionView as SearchView
        searchView.setBackgroundColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (this@FavouriteFragment::adapterUser.isInitialized){
                    viewModel?.let {
                        if (p0 != null) {
                            it.searchUserFavourite(p0)
                        }
                    }
                }
                return true
            }
        })
    }
}