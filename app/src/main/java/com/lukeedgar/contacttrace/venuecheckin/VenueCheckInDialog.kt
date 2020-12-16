package com.lukeedgar.contacttrace.venuecheckin

import android.R
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class VenueCheckInDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
                .setIcon(R.drawable.checkbox_on_background)
                .setTitle("Thank you for checking in!")
                .setPositiveButton(R.string.ok) { dialog, whichButton ->
                    (activity as VenueCheckin).doPostiveClick()
                }
                .create()
    }

    companion object {
        fun newInstance(title: Int): VenueCheckInDialog {
            val frag = VenueCheckInDialog()
            val args = Bundle()
            args.putInt("title", title)
            frag.setArguments(args)
            return frag
        }
    }
}