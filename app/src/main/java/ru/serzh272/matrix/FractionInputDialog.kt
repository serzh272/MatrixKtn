package ru.serzh272.matrix

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class FractionInputDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder:AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater:LayoutInflater = activity!!.layoutInflater
        builder.setView(inflater.inflate(R.layout.fraction_dialog, null))
            .setPositiveButton("Ok", {dialog, which ->  Toast.makeText(context, "Ok ${dialog.toString()}, ${which}",Toast.LENGTH_SHORT).show()})
        return builder.create()
    }
}