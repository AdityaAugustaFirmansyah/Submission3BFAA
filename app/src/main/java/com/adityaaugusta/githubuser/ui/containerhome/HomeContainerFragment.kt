package com.adityaaugusta.githubuser.ui.containerhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adityaaugusta.githubuser.R
import com.adityaaugusta.githubuser.databinding.FragmentHomeContainerBinding
import com.adityaaugusta.githubuser.ui.detail.AboutFragment
import com.adityaaugusta.githubuser.ui.favourite.FavouriteFragment
import com.adityaaugusta.githubuser.ui.search.ExploreFragment
import com.adityaaugusta.githubuser.ui.home.HomeFragment

class HomeContainerFragment : Fragment() {

    private lateinit var fragmentHomeContainerBinding: FragmentHomeContainerBinding
    private  var idSelected:Int = R.id.home

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeContainerBinding = FragmentHomeContainerBinding.inflate(inflater)
        return fragmentHomeContainerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (idSelected) {
            R.id.home -> {
                loadFragment(HomeFragment())
            }
            R.id.about -> {
                loadFragment(AboutFragment())
            }
            R.id.explore -> {
                loadFragment(ExploreFragment())
            }
            R.id.favorite -> {
                loadFragment(FavouriteFragment())
            }
        }

        fragmentHomeContainerBinding.btnNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    idSelected =it.itemId
                }
                R.id.explore -> {
                    loadFragment(ExploreFragment())
                    idSelected =it.itemId
                }
                R.id.about -> {
                    loadFragment(AboutFragment())
                    idSelected =it.itemId
                }
                R.id.favorite -> {
                    loadFragment(FavouriteFragment())
                    idSelected =it.itemId
                }
            }

            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(fragmentHomeContainerBinding.frameHome.id, fragment)
            .commit()
    }
}