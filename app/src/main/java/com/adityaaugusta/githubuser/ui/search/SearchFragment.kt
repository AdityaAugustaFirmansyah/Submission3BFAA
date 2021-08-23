package com.adityaaugusta.githubuser.ui.search

import android.graphics.Color
import android.os.Bundle
import android.view.*
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
import com.adityaaugusta.githubuser.utils.showView

class SearchFragment : Fragment() {

    private lateinit var fragmentListBinding: FragmentListBinding
    private lateinit var username: String
    private lateinit var adapterUser: AdapterUser
    private var viewModelSearch: SearchViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(KEY_USERNAME).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentListBinding = FragmentListBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return fragmentListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = activity?.application?.let { UsernameViewModelFactory(username, it) }
        viewModelSearch =
            factory?.let { ViewModelProvider(this, it).get(SearchViewModel::class.java) }

        fragmentListBinding.toolbar.title = getString(R.string.search)
        fragmentListBinding.rvUser.layoutManager = LinearLayoutManager(context)

        fragmentListBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        bindMenu()

        viewModelSearch?.getUsers()?.observe(viewLifecycleOwner, {
            it?.let {
                if (!it.success.isNullOrEmpty()) {
                    fragmentListBinding.toolbar.title =
                        getString(R.string.total) + " " + it.success.size
                    adapterUser = AdapterUser(it.success) {
                        val bundle = Bundle()
                        bundle.putParcelable(DetailFragment.DATA_USER, it)
                        findNavController().navigate(
                            R.id.action_searchFragment_to_detailFragment,
                            bundle
                        )
                    }
                    fragmentListBinding.rvUser.adapter = adapterUser
                    fragmentListBinding.progressBar.visibility = View.GONE
                    showView(true, fragmentListBinding.rvUser, fragmentListBinding.tvError)
                } else if (it.loading) {
                    fragmentListBinding.progressBar.visibility = View.VISIBLE
                    fragmentListBinding.rvUser.visibility = View.GONE
                } else if (it.msg.isNotEmpty()) {
                    showView(false, fragmentListBinding.rvUser, fragmentListBinding.tvError)
                    fragmentListBinding.tvError.text = it.msg
                    fragmentListBinding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun bindMenu() {
        fragmentListBinding.toolbar.inflateMenu(R.menu.menu_search)

        val item = fragmentListBinding.toolbar.menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = item.actionView as SearchView
        searchView.setBackgroundColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (this@SearchFragment::adapterUser.isInitialized) {
                    if (p0 != null) {
                        viewModelSearch?.stateSearchUser(p0, getString(R.string.user_not_found))
                    }
                }
                return true
            }
        })
    }

    companion object {
        const val KEY_USERNAME = "username"
    }
}