package com.yol.ui.timeline

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.robertlevonyan.components.picker.*
import com.yol.R
import com.yol.databinding.ActivityCreateTimeLineActitvityBinding
import com.yol.utils.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class CreateTimeLineActivity : AppCompatActivity(), BottomSheet.OnItemClickListenerBottomSheet {
    private lateinit var binding: ActivityCreateTimeLineActitvityBinding
    private val timeLineViewModel: TimeLineViewModel by viewModel()
    private var mediaContentUrl: String = ""
    private var category: String = ""
    private var bottomSheet: BottomSheet? = null
    private var mediaList: ArrayList<Uri> = ArrayList()
    private var isCAMERA:Boolean = true


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTimeLineActitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
        bottomSheet = BottomSheet(this, this)
        binding.apply {

            userName.text = MyApplication.tinyDB.getUserDetails()?.name ?: ""
            iconBack.setOnClickListener {
                finish()
            }

            rlPost.setOnClickListener {


                if (isValid()) {
                    binding.apply {

                        bottomSheet?.show(this@CreateTimeLineActivity.supportFragmentManager, "")


                    }

                } else {
                    Toast.makeText(
                        this@CreateTimeLineActivity,
                        "Please fill all details",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }


        binding.browseFile.setOnClickListener {
            if (checkPermission()) {
                selectImage()
            } else {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    )
                )
            }
        }

        //Create post observer


        timeLineViewModel.post.observe(this,
            {
                Log.d("TAG", it.data.toString())
                when (it.status) {
                    ApiState.Status.LOADING -> {
                        ProgressDialog.showProgressDialog(this)
                    }
                    ApiState.Status.SUCCESS -> {
                        ProgressDialog.hideProgressDialog()

                        it.data?.let { data ->
                            when (data) {
                                true -> finish()

                                false -> {
                                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                                        .show()
                                }

                            }
                        }
                    }
                    ApiState.Status.ERROR -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(this, it.error?.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })

    }

    private fun isValid(): Boolean {
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                && permissions[Manifest.permission.CAMERA] == true
            ) {
                selectImage()
            } else {

            }
        }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storageRead = ContextCompat.checkSelfPermission(
                this@CreateTimeLineActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val storageWrite = ContextCompat.checkSelfPermission(
                this@CreateTimeLineActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val camera = ContextCompat.checkSelfPermission(
                this@CreateTimeLineActivity,
                Manifest.permission.CAMERA
            )
            storageRead == PackageManager.PERMISSION_GRANTED && storageWrite == PackageManager.PERMISSION_GRANTED &&
                    camera == PackageManager.PERMISSION_GRANTED
        } else
            true
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectImage() {
        pickerDialog {
            setTitle(R.string.app_name)
            setTitleTextSize(22f)
            setTitleTextSize(22f)
            setItems(
                setOf(
                    ItemModel(
                        ItemType.ITEM_CAMERA,
                        itemBackgroundColor = getColor(R.color.brown_main)
                    ),
                    ItemModel(
                        ItemType.ITEM_GALLERY,
                        itemBackgroundColor = getColor(R.color.brown_main)
                    ),
//                            ItemModel(ItemType.ITEM_VIDEO_GALLERY , itemBackgroundColor = Color.RED),
//                            ItemModel(ItemType.ITEM_FILES , itemBackgroundColor = Color.RED),
                )
            )
            setListType(PickerDialog.ListType.TYPE_GRID)
        }.setPickerCloseListener { type, uris ->
            when (type) {
                ItemType.ITEM_CAMERA -> {
                    isCAMERA = true
                    mediaList = ArrayList()
                    mediaList.addAll(uris)
                    binding.iconPicker.setImageBitmap(
                        MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            uris[0]
                        )
                    )
                    println(uris.toTypedArray().contentToString())
                }
                ItemType.ITEM_GALLERY -> {
                    isCAMERA = false
                    mediaList = ArrayList()
                    mediaList.addAll(uris)
                    binding.iconPicker.setImageBitmap(
                        MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            uris[0]
                        )
                    )
                    println(uris.toTypedArray().contentToString())

                }
                ItemType.ITEM_VIDEO -> {
//                            ivPreview.load(uris.first()) {
//                                fetcher(VideoFrameUriFetcher(this@MainActivity))
//                            }
//                            ivPreview.setOnClickListener {
//                                Intent(Intent.ACTION_VIEW)
//                                    .apply {
//                                        setDataAndType(uris.first(), "video/mp4")
//                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                                    }
//                                    .let { startActivity(it) }
//                            }
                }
                ItemType.ITEM_VIDEO_GALLERY -> {
                    println(uris.toTypedArray().contentToString())
//                            ivPreview.load(uris.first()) {
//                                fetcher(VideoFrameUriFetcher(this@MainActivity))
//                            }
//                            ivPreview.setOnClickListener {
//                                Intent(Intent.ACTION_VIEW)
//                                    .apply {
//                                        setDataAndType(uris.first(), "video/mp4")
//                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                                    }
//                                    .let { startActivity(it) }
//                            }
                }
                ItemType.ITEM_FILES -> println(uris.toTypedArray().contentToString())
            }
        }.show()
    }

    override fun onItemClick(name: String, position: Int) {
        bottomSheet?.dismiss()
        category = name
        createPost()
    }

    private fun createPost() {
        binding.apply {
            val selectedOption: Int = radioButtonGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(selectedOption)

            val mediaType: MediaType? = "multipart/form-data".toMediaTypeOrNull()

            val params: MutableMap<String, RequestBody> = HashMap()
            params["textContent"] = RequestBody.create(mediaType, notes.text.toString())
            params["category"] = RequestBody.create(mediaType, category)
            params["postInZone "] =RequestBody.create(mediaType, checkbox.isChecked.toString())
            params["public"] = RequestBody.create(mediaType, radioButton.text.equals("Public").toString())

            var file:File? = null
            var filePart:MultipartBody.Part? = null
            if (mediaList.isNotEmpty()) {
                if (isCAMERA) {
                    file = File(
                        MyApplication.getRealPathFromURI(
                            this@CreateTimeLineActivity,
                            mediaList[0]
                        ) ?: ""
                    )
                } else {
                    file = File(Constants.getPath(this@CreateTimeLineActivity, mediaList[0]) ?: "")

                }

                filePart = MultipartBody.Part.createFormData(
                    "media",
                    file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }
            timeLineViewModel.createPost(params = params, filePart)
        }

    }
}