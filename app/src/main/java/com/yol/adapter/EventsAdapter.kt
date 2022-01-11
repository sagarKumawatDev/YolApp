package com.yol.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yol.databinding.EventListItemDesignBinding
import com.yol.model.EventDataModel
import com.yol.network.response.EventResponseModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventsAdapter(
    val mContext: Activity,
    val eventClickListener: EventClickListener
) : PagingDataAdapter<EventResponseModel.Record, EventsAdapter.ViewHolder>(Diff) {
    inner class ViewHolder(var eventListItemDesignBinding: EventListItemDesignBinding) :
        RecyclerView.ViewHolder(eventListItemDesignBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(eventDataModel: EventResponseModel.Record?, pos: Int) {

            eventListItemDesignBinding.apply {
                createdDate.text = getCreateDate(eventDataModel?.createdAt)
                eventName.text = eventDataModel?.eventName
                eventLocation.text = eventDataModel?.city
                eventEndDate.text = getDateInFormat(eventDataModel?.endDateTime, true)
                eventEndTime.text = getDateInFormat(eventDataModel?.endDateTime, false)
                eventStartDate.text = getDateInFormat(eventDataModel?.startDateTime, true)
                eventStartTime.text = getDateInFormat(eventDataModel?.startDateTime, false)
                distanceTv.text = "${eventDataModel?.distance} km"
                ageTv.text = "Age : ${eventDataModel?.minAge} Yrs - ${eventDataModel?.maxAge} Yrs"
                genderTv.text = "Gender : ${eventDataModel?.gender}"


                if (eventDataModel?.public == true) {
                    publicRB.isChecked = true
                } else {
                    privateRB.isChecked = true
                }
                shareEvent.setOnClickListener {
                    eventClickListener.shareEvent(eventDataModel)
                }
                yesForEvent.setOnClickListener {
                    activeUnActive(yesForEvent, noForEvent, mayBeForEvent)

                }
                noForEvent.setOnClickListener {
                    activeUnActive(noForEvent, yesForEvent, mayBeForEvent)
                }
                mayBeForEvent.setOnClickListener {
                    activeUnActive(mayBeForEvent, noForEvent, yesForEvent)
                }


/*
                if (eventDataModel.likes.any { it.userId == currentUserId }) {
                    yesForEvent.setTextColor(Color.BLUE)
                } else {
                    yesForEvent.setTextColor(Color.GRAY)
                }*/
                eventCount.text = "${eventDataModel?.likeCount}"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val eventListItemDesignBinding =
            EventListItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(eventListItemDesignBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), position)


    fun getDateInFormat(date: String?, isDateOrTime: Boolean): String? {

        try {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = dateFormat.parse(date)
            return when {
                isDateOrTime -> {
                    val formatter5 = SimpleDateFormat("dd MMM")
                    formatter5.format(date)
                }
                else -> {
                    val formatter5 = SimpleDateFormat("HH:MM a")
                    formatter5.format(date)
                }
            }
        } catch (e: Exception) {
            return ""
        }
    }

    fun getCreateDate(date: String?): String? {
        return try {
            val format = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US
            )
            val tempDate = format.parse(date)
            /*val cal = Calendar.getInstance(Locale.ENGLISH)
                cal.timeInMillis = time * 1000*/
            android.text.format.DateFormat.format("dd-MMM-yyyy  HH:MM a", tempDate).toString()
        } catch (e: Exception) {
            ""
        }
    }

    interface EventClickListener {
        fun shareEvent(selectedEvent: EventResponseModel.Record?)
        fun likeDislikeEvent(selectedEvent: EventResponseModel.Record?)
    }

    fun activeUnActive(
        textView: CheckedTextView,
        textView1: CheckedTextView,
        textView2: CheckedTextView,
    ) {
        textView.setTextColor(Color.BLUE)
        textView.compoundDrawablesRelative.getOrNull(0)?.setTint(Color.BLUE)

        textView1.setTextColor(Color.GRAY)
        textView1.compoundDrawablesRelative.getOrNull(0)?.setTint(Color.GRAY)

        textView2.setTextColor(Color.GRAY)
        textView2.compoundDrawablesRelative.getOrNull(0)?.setTint(Color.GRAY)
    }

    object Diff : DiffUtil.ItemCallback<EventResponseModel.Record>() {
        override fun areItemsTheSame(
            oldItem: EventResponseModel.Record,
            newItem: EventResponseModel.Record
        ): Boolean =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: EventResponseModel.Record,
            newItem: EventResponseModel.Record
        ): Boolean =
            oldItem == newItem
    }
}