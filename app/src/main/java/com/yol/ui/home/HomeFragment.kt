package com.yol.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.robertlevonyan.components.picker.ItemModel
import com.robertlevonyan.components.picker.ItemType
import com.robertlevonyan.components.picker.PickerDialog
import com.robertlevonyan.components.picker.pickerDialog
import com.yol.R
import com.yol.databinding.FragmentFirstBinding
import com.yol.ui.auth.SplashScreen
import com.yol.ui.timeline.CreateTimeLineActivity
import com.yol.utils.ApiState
import com.yol.utils.Constants
import com.yol.utils.MyApplication
import com.yol.utils.ProgressDialog
import com.yol.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import kotlinx.coroutines.delay
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment(), ForeCastListAdapter.OnItemClickListener {

    private var _binding: FragmentFirstBinding? = null
    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    private var uplaodBitmap: Bitmap? = null
    private var selfiCheck = false
   // private var forecastModel = ForecastModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
        binding.apply {
            captureIcon.setOnClickListener {

                if (checkPermission()) {
                    if (!selfiCheck) selectImage() else startActivity(
                        Intent(
                            requireActivity(),
                            HomeActivity::class.java
                        )
                    )

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
            today.setOnClickListener {

                if (selfiCheck) {
                    ForeCastDialog.newInstance(
                        "My Forecast for today?",
                        true,
                        "",
                        requireActivity(),
                        mainActivityViewModel
                    ).show(requireActivity().supportFragmentManager, "")
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Please capture selfie first!!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
            yesterday.setOnClickListener {

                if (selfiCheck) {
                    ForeCastDialog.newInstance(
                        "How was my yesterday?",
                        false,
                        "",
                        requireActivity(),
                        mainActivityViewModel
                    )
                        .show(requireActivity().supportFragmentManager, "")
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Please capture selfie first!!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            logout.setOnClickListener {
                MyApplication.tinyDB.clear()
                unloadKoinModules(MyApplication.appModules)
                loadKoinModules(MyApplication.appModules)
               // delay(300)
                startActivity(Intent(requireActivity(), SplashScreen::class.java))
            }
            createdTimeLine.setOnClickListener {
                startActivity(Intent(requireActivity(), CreateTimeLineActivity::class.java))

            }
            mindShare.setOnClickListener {
                startActivity(Intent(requireActivity(), HomeActivity::class.java).putExtra("key",1))
            }
            mindMap.setOnClickListener {
                startActivity(Intent(requireActivity(), HomeActivity::class.java).putExtra("key",0))
            }

        }
        mainActivityViewModel.unloadedContentUrl.observe(viewLifecycleOwner, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(requireActivity())
                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()

                    it.data?.let {
                    //    mainActivityViewModel.addUserforecastSelfie(it)
                        Toast.makeText(
                            requireActivity(),
                            "selfie uploaded successfully",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(requireActivity(), it.error?.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
        mainActivityViewModel.claperStatus.observe(viewLifecycleOwner, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(requireActivity())
                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()

                    it.data?.let {

                            userUpdate(it)
                    }
                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(requireActivity(), it.error?.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

        mainActivityViewModel.clapperProgressBar.observe(viewLifecycleOwner, {
            Log.d("TAG", it.data.toString())
            when (it.status) {
                ApiState.Status.LOADING -> {
                    ProgressDialog.showProgressDialog(requireActivity())
                }
                ApiState.Status.SUCCESS -> {
                    ProgressDialog.hideProgressDialog()


                }
                ApiState.Status.ERROR -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(requireActivity(), it.error?.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
       mainActivityViewModel.getForeCast()
     ///   getCleper()
      //  mainActivityViewModel.getUserDetails()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(name: ClapperBoardForecast, position: Int) {

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
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val storageWrite = ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val camera = ContextCompat.checkSelfPermission(
                requireActivity(),
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
                        itemBackgroundColor = requireActivity().getColor(R.color.brown_main)
                    ),
                    ItemModel(
                        ItemType.ITEM_GALLERY,
                        itemBackgroundColor = requireActivity().getColor(R.color.brown_main)
                    ),
//                            ItemModel(ItemType.ITEM_VIDEO_GALLERY , itemBackgroundColor = Color.RED),
//                            ItemModel(ItemType.ITEM_FILES , itemBackgroundColor = Color.RED),
                )
            )
            setListType(PickerDialog.ListType.TYPE_GRID)
        }.setPickerCloseListener { type, uris ->
            when (type) {
                ItemType.ITEM_CAMERA -> {
                    uplaodBitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            uris[0]
                        )


                    val filePath: String = getPath(requireActivity(),uris.firstOrNull()).toString()
                    val imageFile = File(filePath)
                    if (imageFile.exists()) {
                        // do something if it exists
                        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "media",
                            imageFile.name,
                            RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
                        )

                             mainActivityViewModel.saveForecast(filePart)
                        //    mainActivityViewModel.uploadImage(uplaodBitmap)
                        println(uris.toTypedArray().contentToString())
                    }
                }
                ItemType.ITEM_GALLERY -> {
                    uplaodBitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            uris[0]
                        )
                    val filePath = Constants.getPath(requireActivity(),uris.first())
                    val imageFile = File(filePath)
                    if (imageFile.exists()) {
                        // do something if it exists
                        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "media",
                            imageFile.name,
                            RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
                        )

                        mainActivityViewModel.saveForecast(filePart)
                        //    mainActivityViewModel.uploadImage(uplaodBitmap)
                        println(uris.toTypedArray().contentToString())
                    }
                   // mainActivityViewModel.uploadImage(uplaodBitmap)
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
//    private fun getRealPathFromURI(contentURI: Uri?): String {
//        val result: String?
//        val cursor: Cursor? = contentURI?.let { requireActivity().getContentResolver().query(it, null, null, null, null) }
//        if (cursor == null) {
//            result = contentURI?.path
//        } else {
//            cursor.moveToFirst()
//            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//            result = cursor.getString(idx)
//            cursor.close()
//        }
//        return result.toString()
//    }

    private fun realPathFromImageUri(uri: Uri): String? {
        val contentUri = uri
        val filePathColum: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor:Cursor? = requireActivity().contentResolver.query(contentUri, filePathColum, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColum[0])
        val picturePath = columnIndex?.let { cursor?.getString(it) }
        return picturePath
    }

    fun getPath(context: Context, uri: Uri?): String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            uri?.let { context.getContentResolver().query(it, proj, null, null, null) }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }


//    fun getCleper() {
//        mainActivityViewModel.get()
//        lifecycleScope.launch {
//            mainActivityViewModel.claperStatus.observe(requireActivity(), {
//                Log.d("TAG", it.data.toString())
//                when (it.status) {
//                    ApiState.Status.LOADING -> {
//                        ProgressDialog.showProgressDialog(requireActivity())
//                    }
//                    ApiState.Status.SUCCESS -> {
//                        ProgressDialog.hideProgressDialog()
//
//                        it.data?.let { data ->
//                            mainActivityViewModel.isclaperStatus = data.claperstatus?.toInt()
//                            //  mainActivityViewModel.getUserforecastSelfie()
//                            userUpdate()
//
//
//                        }
//                    }
//                    ApiState.Status.ERROR -> {
//                        ProgressDialog.hideProgressDialog()
//                    }
//                }
//            })
//        }
//    }

    private fun userUpdate(forecastModel: ForecastModel) {
        Glide.with(requireActivity()).asBitmap().load(forecastModel.forecast.media)
            .into(binding.captureIcon)
        selfiCheck = forecastModel.forecast.media.isNullOrEmpty().not()
            when (forecastModel.isInTime) {
                true -> {

                    if (forecastModel.isCleverForecast == 1) {
                        binding?.captureIcon.isVisible = false
                        binding?.selfieTv.isVisible = false
                        binding?.createdTimeLine.isVisible = true

                        binding.today.isVisible = false
                        binding.yesterday.isVisible = false
                        binding.imageView4.setImageBitmap(null)
                        binding.top.background = resources.getDrawable(R.drawable.green)
                        binding.captureIcon.isClickable = true
                        binding?.userMessages.isVisible = false
                        binding.rlFocecast.isVisible = true
                        binding.todayFocecast.text = "${Constants.forecastList[forecastModel.forecast.today.toInt()]}"
                        binding.yesterdayFocecast.text  ="${Constants.forecastList[forecastModel.forecast.yesterday.toInt()]}"

                        MyApplication.tinyDB.putInt(
                            Constants.SharedPref.USER_FOCECAST_STATUS,
                            1
                        )
                    } else {
                        binding?.captureIcon.isVisible = true
                        binding?.selfieTv.isVisible = true
                        binding?.createdTimeLine.isVisible = false
                        binding.today.isVisible = true
                        binding.yesterday.isVisible = true
                        binding.captureIcon.isClickable = true
                       if( !forecastModel.forecast.media.isNullOrEmpty().not()) binding.captureIcon.setImageResource(R.drawable.vector_smart_object)
                        binding.rlFocecast.isVisible = false
                        binding?.userMessages.isVisible = true
                        binding.top.background = resources.getDrawable(R.drawable.clapper_board)
                        binding.yesterday.isVisible = true
                        MyApplication.tinyDB.putInt(
                            Constants.SharedPref.USER_FOCECAST_STATUS,
                            0
                        )
                    }

                }
                false -> {
                    if (forecastModel.isCleverForecast == 1) {
                        binding?.captureIcon.isVisible = false
                        binding?.selfieTv.isVisible = false
                        binding?.createdTimeLine.isVisible = true
                        binding.today.isVisible = false
                        binding.yesterday.isVisible = false
                        binding.imageView4.setImageBitmap(null)
                        binding.top.background = resources.getDrawable(R.drawable.green)
                        binding.captureIcon.isClickable = true
                        binding?.userMessages.isVisible = false
                        binding.rlFocecast.isVisible = true
                        binding.todayFocecast.text = "${Constants.forecastList[forecastModel.forecast.today.toInt()]}"
                        binding.yesterdayFocecast.text  ="${Constants.forecastList[forecastModel.forecast.yesterday.toInt()]}"
                        MyApplication.tinyDB.putInt(
                            Constants.SharedPref.USER_FOCECAST_STATUS,
                            1
                        )


                    } else {
                        MyApplication.tinyDB.putInt(
                            Constants.SharedPref.USER_FOCECAST_STATUS,
                            2
                        )
                        binding?.captureIcon.isVisible = false
                        binding?.selfieTv.isVisible = false
                        binding?.createdTimeLine.isVisible = true
                        binding.today.isVisible = false
                        binding.yesterday.isVisible = false
                        binding.captureIcon.isClickable = true
                        binding.rlFocecast.isVisible = true
                        binding?.userMessages.isVisible = true
                        binding.top.background = resources.getDrawable(R.drawable.gray)
                        mainActivityViewModel.todayFocecast?.let {
                            binding.todayFocecast.text = "Missed"

                        }
                        mainActivityViewModel.yesterdayFocecast?.let {
                            binding.yesterdayFocecast.text  = "Missed"

                        }
                    }
                }
            }
        }


}