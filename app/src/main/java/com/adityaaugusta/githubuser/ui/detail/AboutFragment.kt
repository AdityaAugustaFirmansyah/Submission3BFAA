package com.adityaaugusta.githubuser.ui.detail

import android.content.Intent
import android.graphics.Color
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

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private var user: User? = null
    private lateinit var sweetAlertDialogLoading: SweetAlertDialog
    private lateinit var sweetAlertDialogError: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        user = User(username = "AdityaAugustaFirmansyah")
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
            val viewModelFactory = activity?.let { it1 ->
                UsernameViewModelFactory(it.username,
                    it1.application)
            }
            val viewModel =
                viewModelFactory?.let { it1 ->
                    ViewModelProvider(this,
                        it1
                    ).get(DetailViewModel::class.java)
                }

            bindMenu()

            binding.toolbar2.navigationIcon = null

            viewModel?.getState()?.observe(viewLifecycleOwner) {
                it.let {
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

                            binding.apply {
                                toolbar2.setTitleTextColor(Color.WHITE)
                                tvNameuser.text = getString(R.string.name,user.name,user.username)

                                Glide.with(this@AboutFragment).load(user.avatar).into(imgViewUser)

                                tvCompany.text = user.company

                                tvLocation.text = user.location

                                tvFollower.text = user.follower.divideNumberOneThousand()

                                tvRepository.text = user.repository.divideNumberOneThousand()

                                tvFollowing.text = user.following.divideNumberOneThousand()
                            }
                        }
                    }

                    val bundle = Bundle()

                    bundle.putParcelable(DetailFragment.DATA_USER,user)

                    val pagerAdapter = this.activity?.let { it1 ->
                        val followingFragment = FollowersFragment()
                        followingFragment.arguments = bundle

                        val followersFragment = FollowingFragment()
                        followersFragment.arguments = bundle
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
            }
        }
    }

    private fun bindMenu() {
        binding.toolbar2.inflateMenu(R.menu.menu_detail)
        val share = binding.toolbar2.menu.findItem(R.id.share)
        val favoorite = binding.toolbar2.menu.findItem(R.id.favorite)

        favoorite.isVisible = false

        binding.toolbar2.setNavigationOnClickListener {
            findNavController().popBackStack()
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
}