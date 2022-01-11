package com.yol.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.yol.databinding.TimelinePostListItemBinding
import com.yol.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.http.Url
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.String.format
import java.net.URI
import java.net.URL
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yol.ui.timeline.TimeLineViewModel
import android.graphics.PorterDuff

import androidx.core.content.ContextCompat

import android.graphics.PorterDuffColorFilter

import android.graphics.drawable.Drawable
import android.widget.CheckedTextView

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.yol.network.response.EventResponseModel
import com.yol.network.response.PostResponseModel
import com.yol.utils.MyApplication

class TimelinePostsAdapter(
    var mContext: Activity,
    var userId: String,
    var timeLineInterface: TimeLineInterface
) : PagingDataAdapter<PostResponseModel.Record, TimelinePostsAdapter.ViewHolder>(
    Diff
) {
    inner class ViewHolder(var timelinePostListItemBinding: TimelinePostListItemBinding) :
        RecyclerView.ViewHolder(timelinePostListItemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(post: PostResponseModel.Record?, pos: Int) {
            timelinePostListItemBinding.apply {
                post?.apply {
                    typeCategory.text = "#$category"
                    notes.text = textContent
                    userName.text = "${createdBy?.name} in "
                    time.text =
                        "${getDate(createdAt)}" //+ " " + "${getDateInFormat(getDate(createdDate.toLong()),false)}"
                    if (media.isNullOrEmpty().not()) {
                        Glide.with(mContext)
                            .load(media?.get(0))
                            .override(1244,788)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.5f)
                            .centerCrop()
                            .into(idSDimage);
                        idSDimage.isVisible = true
                    }
                }
                like.setOnClickListener {
                    if (post?.likes?.any { it.id == userId} == true)
                        timeLineInterface.unLikePost(post, pos)
                    else
                        timeLineInterface.like(post, pos)

                }


                share.setOnClickListener {
                    timeLineInterface.share(post, pos)
                }
                if (post?.likes?.any { it.id == userId } == true) {
                    like.isChecked = true
                    like.setTextColor(Color.BLUE)
                    like.compoundDrawablesRelative.getOrNull(0)?.setTint(Color.BLUE)

                } else {
                    like.isChecked = false
                    like.setTextColor(Color.GRAY)
                    like.compoundDrawablesRelative.getOrNull(0)?.setTint(Color.GRAY)
                }


//                post.apply {
//                    typeCategory.text = "#$category"
//                    notes.text = textContent
//                    userName.text = "${createdBy.name} in "
//                    time.text =
//                        "${getDate(createdDate.toLong())}" //+ " " + "${getDateInFormat(getDate(createdDate.toLong()),false)}"
//                    if (!mediaContentUrl.isNullOrEmpty()) {
//                        Glide.with(mContext).asBitmap().load(mediaContentUrl)
//                            .apply(RequestOptions().override(1800, 700))
//                            .into(idSDimage)
//                    }
//                    /*timeLineViewModel.firebaseAuth.currentUser.let { currentUser ->
//                            if (likes.any { it.userId == currentUser?.uid } == true) {
//                                like.isChecked = true
//                                like.setTextColor(Color.BLUE)
//                                like.compoundDrawablesRelative.getOrNull(0)?.setTint(Color.BLUE)
//
//                            }else{
//                                like.isChecked = false
//                                like.setTextColor(Color.GRAY)
//                                like.compoundDrawablesRelative.getOrNull(0)?.setTint(Color.GRAY)
//                            }
//
//                    }*/
//                    if(post.likes.size > 0){
//                        likeCount.text = post.likes.size.toString()
//
//                    }else{
//                        likeCount.text = ""
//
//                    }
//                    like.setOnClickListener {
//                        post.likes?.let { it1 -> timeLineInterface.like(it1, post, pos) }
//                    }
//                    share.setOnClickListener {
//                        timeLineInterface.share(post, pos)
//                    }
//
//                }
//
//            }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val timelinePostListItemBinding =
            TimelinePostListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(timelinePostListItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), position)


    private fun getDate(date: String?): String? {
        return try {
            val format = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()
            )
            val tempDate = format.parse(date)
            /*val cal = Calendar.getInstance(Locale.ENGLISH)
                cal.timeInMillis = time * 1000*/
            DateFormat.format("dd-MMM-yyyy  HH:MM a", tempDate).toString()
        } catch (e: Exception) {
            ""
        }
    }


    object Diff : DiffUtil.ItemCallback<PostResponseModel.Record>() {
        override fun areItemsTheSame(
            oldItem: PostResponseModel.Record,
            newItem: PostResponseModel.Record
        ): Boolean =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: PostResponseModel.Record,
            newItem: PostResponseModel.Record
        ): Boolean =
            oldItem == newItem
    }

}