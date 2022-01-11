package com.yol.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.roundToInt


/**
 * Created by dheerajpandey on 6/22/18.
 */
class Helper {
    fun getDurationString(seconds: Int): String {
        //   int hours = seconds / 3600;
        var seconds = seconds
        val minutes = seconds % 3600 / 60
        seconds = seconds % 60
        return twoDigitString(minutes) + " : " + twoDigitString(seconds)
    }

    private fun twoDigitString(number: Int): String {
        if (number == 0) {
            return "00"
        }
        return if (number / 10 == 0) {
            "0$number"
        } else number.toString()
    }

    companion object {
        private var snackbar: Snackbar? = null
        private val dialog: Dialog? = null
        private val mToast: Toast? = null
        private const val isApplied = false
        private val popupWindow: ListPopupWindow? = null
        private const val isPopUp = false
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS
        var alertIsBeingShown = false
        var alertIsBeingShownDialogBox = false
        fun replaceFragment(frameId: Int, fragment: Fragment, activity: AppCompatActivity) {
            activity.supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
        }


        /**
         * for showing the messages in the bottom
         */
        fun showSnackBar(view1: View?, message: String?) {
            try {
                snackbar = Snackbar.make(view1!!, message!!, Snackbar.LENGTH_LONG)
                snackbar!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * for showing the messages in the bottom
         */
        fun showSnackBar(view1: TextView?, message: String?) {
            try {
                snackbar = Snackbar.make(view1!!, message!!, Snackbar.LENGTH_LONG)
                snackbar!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        @JvmStatic
        fun nullCheckForInt(data: Int?): String {
            return if (data == null || data == 0) {
                "0"
            } else {
                data.toString()
            }
        }

        @JvmStatic
        fun String.isValidEmail() =
            !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

        @JvmStatic
        fun nullCheckForInt(data: Float?): String {
            return if (data == null || data == 0f) {
                "0"
            } else {
                data.toString()
            }
        }


        @JvmStatic
        fun nullCheckForIntForApi(data: Int?): Int {
            return if (data == null || data == 0) {
                0
            } else {
                data
            }
        }

        @JvmStatic
        fun nullCheckForAmount(data: String?): String {
            return if (data == null) {
                "0"
            } else {
                when (data.length) {
                    in 0..5 -> {
                        data
                    }
                    6 -> {
                        data.substring(0, 1) + " Lakh"
                    }
                    7 -> {
                        data.substring(0, 2) + " Lakh"
                    }
                    8 -> {
                        data.substring(0, 1) + " Cr"
                    }
                    9 -> {
                        data.substring(0, 2) + " Cr"
                    }
                    else ->
                        data.substring(0, data.length - 7) + " Cr"
                }

            }
        }


        @JvmStatic
        fun nullCheckForAmount(data_: Int?): String {
            var data = data_.toString()
            return when (data.length) {
                in 0..3 -> {
                    data
                }
                4 -> {
                    data.substring(0, 1) + " Thsd"
                }
                5 -> {
                    data.substring(0, 2) + " Thsd"
                }
                6 -> {
                    data.substring(0, 1) + " Lakh"
                }
                7 -> {
                    data.substring(0, 2) + " Lakh"
                }
                8 -> {
                    data.substring(0, 1) + " Cr"
                }
                9 -> {
                    data.substring(0, 2) + " Cr"
                }
                else ->
                    data.substring(0, data.length - 7) + " Cr"
            }
        }

        @JvmStatic
        fun nullCheckForAmount(data_: Long?): String {
            var data = data_.toString()
            return when (data.length) {
                in 0..3 -> {
                    data
                }
                4 -> {
                    data.substring(0, 1) + " Thsd"
                }
                5 -> {
                    data.substring(0, 2) + " Thsd"
                }
                6 -> {
                    data.substring(0, 1) + " Lakh"
                }
                7 -> {
                    data.substring(0, 2) + " Lakh"
                }
                8 -> {
                    data.substring(0, 1) + " Cr"
                }
                9 -> {
                    data.substring(0, 2) + " Cr"
                }
                else ->
                    data.substring(0, data.length - 7) + " Cr"
            }
        }

        @JvmStatic
        fun nullCheckForString(data: String?): String {
            return if (data.isNullOrEmpty()) {
                ""
            } else {
                data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase()

            }
        }

        @JvmStatic
        fun convertIntToString(data: String?): Int {
            return if (data.isNullOrEmpty()) {
                0
            } else {
                try {
                    Integer.valueOf(data.toString())
                } catch (e: Exception) {
                    0
                }
            }
        }


        @SuppressLint("SimpleDateFormat")
        @JvmStatic
        fun increaseDate(data: String?, count: Int): String {
            return if (data.isNullOrEmpty()) {
                ""
            } else {
                try {
                    var dateInString = data// Start date
                    var sdf = SimpleDateFormat("yyyy-MM-dd")
                    val c = Calendar.getInstance()
                    c.time = sdf.parse(dateInString)
                    c.add(Calendar.DATE, count)
                    sdf = SimpleDateFormat("yyyy-MM-dd")
                    val resultdate = Date(c.timeInMillis)
                    dateInString = sdf.format(resultdate)
                    return dateInString
                } catch (e: Exception) {
                    ""
                }
            }
        }

        @SuppressLint("NewApi")
        @JvmStatic
        fun nullCheckDataFormate(data: String?): String {
            return if (data.isNullOrEmpty()) {
                ""
            } else {
                var formattedDate = ""
                try {
                    val inputFormatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                    val outputFormatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd/MM/yyy", Locale.ENGLISH)
                    val date: LocalDate = LocalDate.parse(data, inputFormatter)
                    formattedDate = outputFormatter.format(date)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                formattedDate

            }
        }

        fun startDialer(activity: Activity, s: String) {
            var s = s
            s = s.replace("[^\\d.]".toRegex(), "")
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$s"))
            activity.startActivity(intent)
        }

        fun findPercentage(view1: TextView, amt: Int): Int {

            if (view1.text.toString() != "") {
                val amount = view1.text.toString().toDouble()
                val res = (amount / amt) * 100.0
                return res.roundToInt().toInt()

            } else {
                return 0
            }
        }

        fun getCurrentDateForApi(): String {
            val c: Date = Calendar.getInstance().getTime()
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return df.format(c)
        }

        @JvmStatic
        fun getCurrentDateWithMonthName(): String {
            val c: Date = Calendar.getInstance().getTime()
            val df = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
            return df.format(c)
        }

        fun getCurrentDate(): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDate = sdf.format(Date())

            return currentDate.toString()
        }


        fun getTomarrowDate(): String {
            val calendar = Calendar.getInstance()
            val today = calendar.time
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrow = calendar.time
            val todayAsString = dateFormat.format(today)
            val tomorrowAsString = dateFormat.format(tomorrow)


            return tomorrowAsString.toString()
        }

        fun getTomarrowDateForApi(): String {
            val calendar = Calendar.getInstance()
            val today = calendar.time
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrow = calendar.time
            val todayAsString = dateFormat.format(today)
            val tomorrowAsString = dateFormat.format(tomorrow)

            val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
            val d = dateFormat.parse(tomorrowAsString)
            return dateFormat1.format(d)
        }

        @JvmStatic
        fun getPreviousMonth(): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val now = LocalDate.now()
                val earlier = now.minusMonths(1) // 2015-10-24
                return earlier.month.toString()
            } else {
                return ""
            } // 2015-11-24
        }


        @JvmStatic
        fun calculateAmount(amt: Int, str: String): Int {
            return when (str) {
                "Lakh" -> {
                    amt * 100000
                }
                "Cr" -> {
                    amt * 10000000
                }
                else -> {
                    0
                }

            }
        }


        @JvmStatic
        fun calculateAmountBySpinner(str: String): Long {
            try {
                if (str.contains("Thsd")) {
                    return Integer.valueOf(str.substring(0, str.indexOf(" "))).toLong() * 1000
                }
                if (str.contains("Lakh")) {
                    return Integer.valueOf(str.substring(0, str.indexOf(" "))).toLong() * 100000
                }
                if (str.contains("Cr")) {
                    return Integer.valueOf(str.substring(0, str.indexOf(" "))).toLong() * 10000000
                } else {
                    return 0
                }
            } catch (e: java.lang.Exception) {
                return 0
            }
        }

        @JvmStatic
        fun monthsBetweenDates(startDate: String?, endDate: String?): Int {
            try {
                val sdf: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                val start = Calendar.getInstance()
                start.time = sdf.parse(startDate)
                val end = Calendar.getInstance()
                end.time = sdf.parse(endDate)
                var monthsBetween = 0
                var dateDiff = end[Calendar.DAY_OF_MONTH] - start[Calendar.DAY_OF_MONTH]
                if (dateDiff < 0) {
                    val borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH)
                    dateDiff = end[Calendar.DAY_OF_MONTH] + borrrow - start[Calendar.DAY_OF_MONTH]
                    monthsBetween--
                    if (dateDiff > 0) {
                        monthsBetween++
                    }
                } else {
                    monthsBetween++
                }
                monthsBetween += end[Calendar.MONTH] - start[Calendar.MONTH]
                monthsBetween += (end[Calendar.YEAR] - start[Calendar.YEAR]) * 12
                return monthsBetween
            } catch (e: java.lang.Exception) {
                return 1
            }
        }

        @JvmStatic
        fun getCountOfDays(createdDateString: String?, expireDateString: String?): Int {
            try {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                var createdConvertedDate: Date? = null
                var expireCovertedDate: Date? = null
                var todayWithZeroTime: Date? = null
                try {
                    createdConvertedDate = dateFormat.parse(createdDateString)
                    expireCovertedDate = dateFormat.parse(expireDateString)
                    val today = Date()
                    todayWithZeroTime = dateFormat.parse(dateFormat.format(today))
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                var cYear = 0
                var cMonth = 0
                var cDay = 0
                if (createdConvertedDate!!.after(todayWithZeroTime)) {
                    val cCal = Calendar.getInstance()
                    cCal.time = createdConvertedDate
                    cYear = cCal[Calendar.YEAR]
                    cMonth = cCal[Calendar.MONTH]
                    cDay = cCal[Calendar.DAY_OF_MONTH]
                } else {
                    val cCal = Calendar.getInstance()
                    cCal.time = todayWithZeroTime
                    cYear = cCal[Calendar.YEAR]
                    cMonth = cCal[Calendar.MONTH]
                    cDay = cCal[Calendar.DAY_OF_MONTH]
                }


                val eCal = Calendar.getInstance()
                eCal.time = expireCovertedDate
                val eYear = eCal[Calendar.YEAR]
                val eMonth = eCal[Calendar.MONTH]
                val eDay = eCal[Calendar.DAY_OF_MONTH]
                val date1 = Calendar.getInstance()
                val date2 = Calendar.getInstance()
                date1.clear()
                date1[cYear, cMonth] = cDay
                date2.clear()
                date2[eYear, eMonth] = eDay
                val diff = date2.timeInMillis - date1.timeInMillis
                val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
                return dayCount.toInt()


            } catch (e: java.lang.Exception) {
                return -1
            }
        }

        fun getDateFormate(date: String): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
            val d = dateFormat.parse(date)
            return dateFormat1.format(d)
        }

        fun TextView.getDateFormate(): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
            val d = dateFormat.parse(this.text.toString())
            return dateFormat1.format(d)
        }


        private fun capitalize(s: String?): String {
            if (s == null || s.length == 0) {
                return ""
            }
            val first = s[0]
            return if (Character.isUpperCase(first)) {
                s
            } else {
                Character.toUpperCase(first).toString() + s.substring(1)
            }
        }

        val deviceName: String
            get() {
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL
                return if (model.startsWith(manufacturer)) {
                    capitalize(model)
                } else {
                    capitalize(manufacturer) + " " + model
                }
            }


        fun setTextViewDrawableColor(editText: EditText, color: Int) {
            for (drawable in editText.compoundDrawables) {
                if (drawable != null) {
                    drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                }
            }
        }

        @SuppressLint("Range")
        fun getImagePath(uri: Uri?, activity: Activity): String {
            var cursor = activity.contentResolver.query(uri!!, null, null, null, null)
            cursor!!.moveToFirst()
            var document_id = cursor.getString(0)
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
            cursor.close()
            cursor = activity.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null
            )
            cursor!!.moveToFirst()
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor.close()
            return path
        }

        /* for setting fragment in the container */
        @JvmName("setFragment1")
        @JvmStatic
        fun setFragment(
            fragment: Fragment?,
            removeStack: Boolean,
            activity: FragmentActivity,
            mContainer: Int,
            bundle: Bundle? = null
        ) {
            fragment?.arguments = bundle
            val fragmentManager = activity.supportFragmentManager
            val ftTransaction = fragmentManager.beginTransaction()
            if (removeStack) {
                val size = fragmentManager.backStackEntryCount
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                ftTransaction.replace(mContainer, fragment!!)
            } else {
                ftTransaction.replace(mContainer, fragment!!)
                ftTransaction.addToBackStack(null)
            }
            ftTransaction.commit()
        }

        @JvmStatic
        fun setFragment(
            fragment: Fragment?,
            removeStack: Boolean,
            activity: FragmentActivity,
            mContainer: Int,
            tag: String
        ) {
            val fragmentManager = activity.supportFragmentManager
            val ftTransaction = fragmentManager.beginTransaction()
            if (removeStack) {
                val size = fragmentManager.backStackEntryCount
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                ftTransaction.replace(mContainer, fragment!!)
            } else {
                ftTransaction.replace(mContainer, fragment!!, tag)
                ftTransaction.addToBackStack(null)
            }
            ftTransaction.commit()
        }

        @JvmStatic
        fun setFragment(
            fragment: Fragment?,
            removeStack: Boolean,
            activity: FragmentActivity,
            mContainer: Int,
            bundle: Bundle
        ) {
            if (fragment != null) {
                fragment.arguments = bundle
            }
            val fragmentManager = activity.supportFragmentManager
            val ftTransaction = fragmentManager.beginTransaction()
            if (removeStack) {
                val size = fragmentManager.backStackEntryCount
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                ftTransaction.replace(mContainer, fragment!!)
            } else {
                ftTransaction.replace(mContainer, fragment!!)
                ftTransaction.addToBackStack(null)
            }
            ftTransaction.commit()
        }


        fun formatDate(inputDate: String?): String {
            var outputDate: String = ""
            /*  "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        2019-06-17T11:01:24.000Z*/
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM,dd HH:mm", Locale.getDefault())
            var date: Date? = null
            try {
                date = inputFormat.parse(inputDate)
                outputDate = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return outputDate
        }

        fun formatDateTime(inputDate: String?): String {
            var outputDate: String = ""
            /*  "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        2019-06-17T11:01:24.000Z*/
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM,dd HH:mm", Locale.getDefault())
            var date: Date? = null
            try {
                date = inputFormat.parse(inputDate)
                outputDate = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return outputDate
        }

        @JvmStatic
        fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(this)
        }

        @JvmStatic
        fun getCurrentDateTime(): Date {
            return Calendar.getInstance().time
        }

        fun getDifferenceBetweenTime(str_date: String?): String {
            val cal = Calendar.getInstance()
            val tz = cal.timeZone
            val formatter: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            var date: Date? = null
            try {
                date = formatter.parse(str_date)
                return DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), 0)
                    .toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            println("Today is " + date!!.time)
            return ""
        }

        /**
         * This method is to return duration in minute and seconds for the playing time
         *
         * @param seconds specifies the total millisceonds played
         */
        fun formatPlayingDuration(seconds: Int): String {
            // int seconds = milliseconds / 1000;
            val minutes = seconds / 60
            val displayedSeconds = seconds % 60
            return if (minutes == 0) "00:" + addZero(
                displayedSeconds
            ) else addZero(minutes) + ":" + addZero(
                displayedSeconds
            )
        }

        /**
         * This method is to return number with zero or not zero i.e. to make a number in two digit
         *
         * @param number specifies the number that needs to be foramtted
         */
        private fun addZero(number: Int): String {
            return if (number < 10) "0$number" else "" + number
        }

        fun isInternetOn(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var netInfo: NetworkInfo? = null
            if (cm != null) {
                netInfo = cm.activeNetworkInfo
            }
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

        /*------------------------------------------- Method to print Hash key-----------------------------------------------------*/
        fun printHashKey(pContext: Context) {
            try {
                val info = pContext.packageManager.getPackageInfo(
                    pContext.packageName,
                    PackageManager.GET_SIGNATURES
                )
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val hashKey = String(Base64.encode(md.digest(), 0))
                    Log.i(ContentValues.TAG, "printHashKey() Hash Key: $hashKey")
                }
            } catch (e: NoSuchAlgorithmException) {
                Log.e(ContentValues.TAG, "printHashKey()", e)
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "printHashKey()", e)
            }
        }

        /*-------------------------------------xxxxxxxxxxxxxxxxxx----------------------------------------------------------------------*/
        fun checkWhatsAppInstalledOrNot(activity: Activity): Boolean {
            val uri = "com.whatsapp"
            val pm = activity.packageManager
            val app_installed: Boolean
            app_installed = try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
            return app_installed
        }

        @JvmStatic
        fun getRandomString(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }

        @JvmStatic
        fun dateFormate(data: String): String? {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
            try {
                val d = dateFormat.parse(data)
                return dateFormat1.format(d).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        @JvmStatic
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }


//        @JvmStatic
//        fun DatePickerDialogBox(context: Context?, activity: Activity, eText: TextView) {
//            Helper.hideKeyboard(activity)
//            if (alertIsBeingShown) return;
//            var dialogView = LayoutInflater.from(context).inflate(R.layout.date_picker, null)
//            alertIsBeingShown = true
//            val dialogBinding: DatePickerBinding = DataBindingUtil.bind(dialogView)!!
//            var dialog = Dialog(context!!)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(dialogBinding.root)
//            dialog.setCancelable(true)
//
//            dialogBinding.datePicker1.minDate = System.currentTimeMillis() - 1000;
//
//            dialogBinding.tvCancel.setOnClickListener {
//                alertIsBeingShown = false
//                dialog.dismiss()
//
//            }
//            dialogBinding.tvOkay.setOnClickListener {
//                alertIsBeingShown = false
//                eText.text =
//                    dialogBinding.datePicker1.dayOfMonth.toString() + "/" + (dialogBinding.datePicker1.month + 1) + "/" + dialogBinding.datePicker1.year
//                dialog.dismiss()
//
//            }
//            dialog.show()
//            val lp = WindowManager.LayoutParams()
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT
//            lp.gravity = Gravity.CENTER
//            dialog.window!!.attributes = lp
//            dialog.window!!.setWindowAnimations(R.style.AnimationCenterPopUp)
//            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//            dialog.window!!
//                .setBackgroundDrawable(
//                    ColorDrawable(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.seme_transparent
//                        )
//                    )
//                )
//        }
//
//        @JvmStatic
//        fun DatePickerDialogBoxWithYYMMDD(context: Context?, activity: Activity, eText: TextView) {
//            Helper.hideKeyboard(activity)
//            if (alertIsBeingShown) return;
//            var dialogView = LayoutInflater.from(context).inflate(R.layout.date_picker, null)
//            alertIsBeingShown = true
//            val dialogBinding: DatePickerBinding = DataBindingUtil.bind(dialogView)!!
//            var dialog = Dialog(context!!)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(dialogBinding.root)
//            dialog.setCancelable(true)
//
//            dialogBinding.datePicker1.minDate = System.currentTimeMillis() - 1000;
//
//            dialogBinding.tvCancel.setOnClickListener {
//                alertIsBeingShown = false
//                dialog.dismiss()
//
//            }
//            dialogBinding.tvOkay.setOnClickListener {
//                alertIsBeingShown = false
//                eText.text =
//                    dialogBinding.datePicker1.year.toString()+"-"+(dialogBinding.datePicker1.month + 1)+"-"+dialogBinding.datePicker1.dayOfMonth.toString()
//                dialog.dismiss()
//
//            }
//            dialog.show()
//            val lp = WindowManager.LayoutParams()
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT
//            lp.gravity = Gravity.CENTER
//            dialog.window!!.attributes = lp
//            dialog.window!!.setWindowAnimations(R.style.AnimationCenterPopUp)
//            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//            dialog.window!!
//                .setBackgroundDrawable(
//                    ColorDrawable(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.seme_transparent
//                        )
//                    )
//                )
//        }
//
//        @JvmStatic
//        fun DatePickerDialogBoxForDOB(context: Context?, activity: Activity, eText: TextView) {
//            Helper.hideKeyboard(activity)
//            if (alertIsBeingShown) return;
//            var dialogView = LayoutInflater.from(context).inflate(R.layout.date_picker, null)
//            alertIsBeingShown = true
//            val dialogBinding: DatePickerBinding = DataBindingUtil.bind(dialogView)!!
//            var dialog = Dialog(context!!)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(dialogBinding.root)
//            dialog.setCancelable(true)
//            dialogBinding.datePicker1.maxDate = System.currentTimeMillis();
//            dialogBinding.tvCancel.setOnClickListener {
//                alertIsBeingShown = false
//                dialog.dismiss()
//
//            }
//            dialogBinding.tvOkay.setOnClickListener {
//                alertIsBeingShown = false
//                eText.text =
//                    dialogBinding.datePicker1.dayOfMonth.toString() + "/" + (dialogBinding.datePicker1.month + 1) + "/" + dialogBinding.datePicker1.year
//                dialog.dismiss()
//
//            }
//            dialog.show()
//            val lp = WindowManager.LayoutParams()
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT
//            lp.gravity = Gravity.CENTER
//            dialog.window!!.attributes = lp
//            dialog.window!!.setWindowAnimations(R.style.AnimationCenterPopUp)
//            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//            dialog.window!!
//                .setBackgroundDrawable(
//                    ColorDrawable(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.seme_transparent
//                        )
//                    )
//                )
//        }
//
//        @JvmStatic
//        fun isEditTextContainEmail(argEditText: EditText): Boolean {
//            return try {
//                val pattern: Pattern =
//                    Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
//                val matcher: Matcher = pattern.matcher(argEditText.text)
//                matcher.matches()
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//                false
//            }
//        }

//        @JvmStatic
//        fun AlertDialogBox(context: Activity) {
//            Helper.hideKeyboard(context)
//            if (alertIsBeingShown)
//                return
//            val dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
//            val dialogBinding: AlertDialogBinding = DataBindingUtil.bind(dialogView)!!
//            val dialog = Dialog(context!!)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(dialogBinding.root)
//            dialog.setCancelable(true)
//            alertIsBeingShown = true
//            dialogBinding.tvOkay.setOnClickListener {
//                alertIsBeingShown = false
//                dialog.dismiss()
//                //                startActivity(new Intent(getActivity(), WoInfoActivity.class));
//            }
//            dialog.show()
//            val lp = WindowManager.LayoutParams()
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT
//            lp.gravity = Gravity.CENTER
//            dialog.window!!.attributes = lp
//            dialog.window!!.setWindowAnimations(R.style.AnimationCenterPopUp)
//            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//            dialog.window!!
//                .setBackgroundDrawable(
//                    ColorDrawable(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.seme_transparent
//                        )
//                    )
//                )
//        }
//
//        @JvmStatic
//        fun AlertDialogBox(context: Activity, text: String) {
//            Helper.hideKeyboard(context)
//            if (alertIsBeingShown) return
//            val dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
//            val dialogBinding: AlertDialogBinding = DataBindingUtil.bind(dialogView)!!
//            val dialog = Dialog(context)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(dialogBinding.root)
//            dialog.setCancelable(true)
//            alertIsBeingShown = true
//            dialogBinding.textView.text = text
//            val cldr = Calendar.getInstance()
//            dialogBinding.tvOkay.setOnClickListener {
//                alertIsBeingShown = false
//                dialog.dismiss()
//                //                startActivity(new Intent(getActivity(), WoInfoActivity.class));
//            }
//            dialog.show()
//            val lp = WindowManager.LayoutParams()
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT
//            lp.gravity = Gravity.CENTER
//            dialog.window!!.attributes = lp
//            dialog.window!!.setWindowAnimations(R.style.AnimationCenterPopUp)
//            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//            dialog.window!!
//                .setBackgroundDrawable(
//                    ColorDrawable(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.seme_transparent
//                        )
//                    )
//                )
//        }
//    }
    }
}