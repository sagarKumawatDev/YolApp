package com.yol.utils

import android.content.res.TypedArray

import android.view.ViewGroup

import com.google.android.material.bottomsheet.BottomSheetBehavior

import android.widget.Toast

import androidx.databinding.DataBindingUtil

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color

import com.google.android.material.bottomsheet.BottomSheetDialog

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yol.R
import com.yol.adapter.CategoryListAdapter
import com.yol.databinding.LayoutBottomSheetBinding

var arraylist = listOf<String>("Ambition","Commitments","Confession","conscience","Dilemmas",
    "Inhibitions","Document","Family","Friends","health","outings","wallet")

class BottomSheet(var mContext: Context, var  onItemClickListenerBottme: OnItemClickListenerBottomSheet) : BottomSheetDialogFragment(),
    CategoryListAdapter.OnItemClickListener {
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    var bi: LayoutBottomSheetBinding? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        bottomSheet.setOnShowListener { setupBottomSheet(it) }

        //inflating layout
        val view: View = View.inflate(context, R.layout.layout_bottom_sheet, null)

        //binding views to data binding.
        bi = DataBindingUtil.bind(view)

        //setting layout with bmContextottom sheet
        bottomSheet.setContentView(view)

        var  categoryListAdapter = CategoryListAdapter(mContext,arraylist,this)
        bi?.list?.adapter = categoryListAdapter


        //hiding app bar at the start
        return bottomSheet
    }
    private fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet = bottomSheetDialog.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
    }
    override fun onStart() {
        super.onStart()
//        bottomSheetBehavior!!.state = STATE_COLLAPSED
    }

    override fun onItemClick(name: String, position: Int) {
     onItemClickListenerBottme.onItemClick(name, position)
    }

    interface OnItemClickListenerBottomSheet {
        fun onItemClick(name: String, position: Int)
    }


}