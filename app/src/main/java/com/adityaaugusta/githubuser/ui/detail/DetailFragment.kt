package com.adityaaugusta.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.adityaaugusta.githubuser.R
import com.adityaaugusta.githubuser.databinding.FragmentDetailBinding
import com.adityaaugusta.githubuser.domain.User
import com.adityaaugusta.githubuser.ui.PagerAdapter
import com.adityaaugusta.githubuser.ui.UsernameViewModelFactory
import com.adityaaugusta.githubuser.ui.follower.FollowersFragment
import com.adityaaugusta.githubuser.ui.following.FollowingFragment
import com.adityaaugusta.githubuser.utils.divideNumberOneThousand
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private var viewModel: DetailViewModel? = null
    private lateinit var binding: FragmentDetailBinding
    private var user: User? = null
    private lateinit var sweetAlertDialogLoading: SweetAlertDialog
    private lateinit var sweetAlertDialogError: SweetAlertDialog
    private var favorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            user = it.getParcelable(DATA_USER)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sweetAlertDialogLoading = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialogError = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)

        sweetAlertDialogLoading.titleText = getString(R.string.loading)
        sweetAlertDialogError.titleText = getString(R.string.error)
        sweetAlertDialogError.setCancelable(false)

        sweetAlertDialogError.setConfirmClickListener {
            it.dismiss()
            findNavController().popBackStack()
        }

        sweetAlertDialogError.setCancelClickListener {
            it.dismiss()
            findNavController().popBackStack()
        }

        user?.let { it ->
            val viewModelFactory =
                activity?.application?.let { it1 -> UsernameViewModelFactory(it.username, it1) }
            viewModel =
                viewModelFactory?.let { it1 ->
                    ViewModelProvider(
                        this,
                        it1
                    ).get(DetailViewModel::class.java)
                }

            bindMenu()

            viewModel?.getState()?.observe(viewLifecycleOwner, { it1 ->
                it1?.let { it ->
                    when {
                        it.msg.isNotEmpty() -> {
                            sweetAlertDialogLoading.dismiss()
                            sweetAlertDialogError.contentText = it.msg
                            sweetAlertDialogError.show()
                            Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                        }

                        it.loading -> {
                            binding.layout.visibility = View.GONE
                            sweetAlertDialogLoading.show()
                        }

                        it.success.username.isNotEmpty() -> {
                            sweetAlertDialogLoading.dismiss()
                            binding.layout.visibility = View.VISIBLE

                            val user = it.success

                            binding.tvNameuser.text =
                                getString(R.string.name, user.name, user.username)

                            Glide.with(this).load(user.avatar).into(binding.imgViewUser)

                            binding.apply {
                                tvCompany.text = user.company

                                tvLocation.text = user.location

                                tvFollower.text = user.follower.divideNumberOneThousand()

                                tvRepository.text = user.repository.divideNumberOneThousand()

                                tvFollowing.text = user.following.divideNumberOneThousand()
                            }


                            viewModel?.getUserFavouriteDetail(user.username)
                                ?.observe(viewLifecycleOwner, {
                                    val menuItemFav = binding.toolbar2.menu.findItem(R.id.favorite)
                                    favorite = it?.username == user.username

                                    if (favorite) {
                                        menuItemFav.setIcon(R.drawable.ic_baseline_favorite_24)
                                    } else {
                                        menuItemFav.setIcon(R.drawable.ic_baseline_favorite_border_24)
                                    }
                                })

                        }
                    }

                    val pagerAdapter = this.activity?.let { it1 ->
                        val followingFragment = FollowersFragment()
                        followingFragment.arguments = arguments

                        val followersFragment = FollowingFragment()
                        followersFragment.arguments = arguments
                        PagerAdapter(
                            it1,
                            mutableListOf(followingFragment, followersFragment)
                        )
                    }

                    binding.viewpager.adapter = pagerAdapter
                    TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
                        tab.text = when (position) {
                            0 -> getString(R.string.followers)
                            1 -> getString(R.string.following)
                            else -> ""
                        }
                    }.attach()
                }
            })
        }
    }

    private fun bindMenu() {
        binding.toolbar2.inflateMenu(R.menu.menu_detail)
        val share = binding.toolbar2.menu.findItem(R.id.share)

        val favourite = binding.toolbar2.menu.findItem(R.id.favorite)

        binding.toolbar2.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        favourite.setOnMenuItemClickListener {
            user?.let {
                if (!favorite){
                    viewModel?.addFavouriteUser(it)
                }else{
                    viewModel?.deleteFavouriteUser(it)
                }
            }
            true
        }

        share.setOnMenuItemClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.lets_check)+user?.name)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
            true
        }
    }

    companion object {
        const val DATA_USER: String = "data"
    }
}