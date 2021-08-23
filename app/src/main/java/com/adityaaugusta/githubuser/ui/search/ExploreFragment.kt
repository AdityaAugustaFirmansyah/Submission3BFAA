package com.adityaaugusta.githubuser.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adityaaugusta.githubuser.R
import com.adityaaugusta.githubuser.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {
    private lateinit var fragmentExplore: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentExplore = FragmentExploreBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return fragmentExplore.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentExplore.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val bundle = Bundle()
                bundle.putString(SearchFragment.KEY_USERNAME, p0)
                findNavController().navigate(
                    R.id.action_homeContainerFragment_to_searchFragment,
                    bundle
                )
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }
}