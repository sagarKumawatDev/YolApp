package com.yol.ui.timeline

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import bolts.Task.delay
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.yol.adapter.EventsAdapter
import com.yol.adapter.TimeLineInterface
import com.yol.adapter.TimelinePostsAdapter
import com.yol.databinding.FragmentSecondBinding
import com.yol.model.CreatedBy
import com.yol.model.EventDataModel
import com.yol.model.Post
import com.yol.network.response.PostResponseModel
import com.yol.utils.ApiState
import com.yol.utils.LoadingStateAdapter
import com.yol.utils.MyApplication
import com.yol.utils.ProgressDialog
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TimelineFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val timeLineViewModel: TimeLineViewModel by viewModel()
    private var pos: Int = 0
    lateinit var timelinePostsAdapter: TimelinePostsAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenStarted {
            timeLineViewModel.getPosts().collectLatest { response ->
                //delay(1000)
                println("response ${response}")
                timelinePostsAdapter.submitData(response)


            }
        }
    }

    private fun init() {
        ProgressDialog.showProgressDialog(requireActivity())

        binding.apply {
            addEventLayout.setOnClickListener {
                startActivity(Intent(requireActivity(), CreateTimeLineActivity::class.java))
            }

            timelinePostsAdapter =
                TimelinePostsAdapter(requireActivity(),MyApplication.tinyDB.getUserDetails()?.id?:"", object : TimeLineInterface {

                    override fun like(post: PostResponseModel.Record?, position: Int) {
                        pos=position
                        timeLineViewModel.likeUnLikePost(postId = post?.id?:"",isLike = true)
                    }

                    override fun unLikePost(post: PostResponseModel.Record?, position: Int) {
                        pos=position
                        timeLineViewModel.likeUnLikePost(postId = post?.id?:"",isLike = true)
                    }

                    override fun share(post: PostResponseModel.Record?, pos: Int) {
                        createLink(post)
                    }

                })
            timeLinePostRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                isNestedScrollingEnabled = false
                hasFixedSize()
                adapter = timelinePostsAdapter
                adapter = timelinePostsAdapter.withLoadStateHeaderAndFooter(
                    header = LoadingStateAdapter { timelinePostsAdapter::retry },
                    footer = LoadingStateAdapter { timelinePostsAdapter::retry }
                )

            }


        }

        timeLineViewModel.likeUnLikePost.observe(requireActivity(), {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {

                }
                ApiState.Status.SUCCESS -> {
                    it.data?.let {
                        timelinePostsAdapter.notifyItemChanged(pos)
                    }

                }
                ApiState.Status.ERROR -> {

                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun createLink(post: PostResponseModel.Record?) {
        Log.d("cc", "cc")
        val link =
            "https://play.google.com/store/apps/details?Result=eventCode=${post?.id}"
        Log.d("re==", link)
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(link))
            .setDomainUriPrefix("https://hapiness.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("com.yol")
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder("com.YOL.app")
                    .setAppStoreId("123456789")
                    .setMinimumVersion("1.0.1")
                    .build()
            )
            .buildShortDynamicLink()
            .addOnSuccessListener { shortDynamicLink ->
                ProgressDialog.hideProgressDialog()
                val mInvitationUrl = shortDynamicLink.shortLink
                val newShareBody =
                    "Hey My friend, I am sharing  you  ${post?.category} ${post?.textContent}. ${mInvitationUrl.toString()}"
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Posts")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, newShareBody)
                startActivity(
                    Intent.createChooser(
                        sharingIntent,
                        "Share Using"
                    )
                )
                // Log.d("re==", mInvitationUrl.toString())
            }
    }


}