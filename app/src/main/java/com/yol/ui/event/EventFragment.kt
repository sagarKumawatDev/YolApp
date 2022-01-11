package com.yol.ui.event

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

import com.yol.adapter.EventsAdapter
import com.yol.databinding.FragmentEventBinding
import com.yol.model.EventDataModel
import com.yol.network.response.EventResponseModel
import com.yol.utils.LoadingStateAdapter
import com.yol.utils.ProgressDialog
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val eventViewModel: EventViewModel by viewModel()
    private var mInvitationUrl: Uri? = null
    lateinit var eventsAdapter: EventsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        ProgressDialog.showProgressDialog(requireActivity())

        binding.apply {
            addEventLayout.setOnClickListener {
                startActivity(Intent(requireActivity(), CreateEventActivity::class.java))
            }

            eventsAdapter =
                EventsAdapter(requireActivity(), object : EventsAdapter.EventClickListener {
                    override fun shareEvent(selectedEvent: EventResponseModel.Record?) {
                        createLink(selectedEvent)
                    }

                    override fun likeDislikeEvent(selectedEvent: EventResponseModel.Record?) {

                    }

                })
            eventRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = eventsAdapter
                adapter = eventsAdapter.withLoadStateHeaderAndFooter(
                    header = LoadingStateAdapter { eventsAdapter::retry },
                    footer = LoadingStateAdapter { eventsAdapter::retry }
                )

            }


        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenStarted {
            eventViewModel.getEvents().collectLatest { response ->
                println("response ${response}")
                eventsAdapter.submitData(response)

            }
        }
    }

    fun createLink(selectedEvent: EventResponseModel.Record?) {
        Log.d("cc", "cc")
        val link =
            "https://play.google.com/store/apps/details?Result=eventCode=${selectedEvent?.id}"
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
                mInvitationUrl = shortDynamicLink.shortLink
                val newShareBody =
                    "Hey My friend, I am inviting  you for ${selectedEvent?.eventName}. ${mInvitationUrl.toString()}"
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Event")
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
