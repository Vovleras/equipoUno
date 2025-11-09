package com.moviles.proyecto1.view.dialog

import android.content.Context
import android.text.SpannableString
import android.text.style.RelativeSizeSpan

import androidx.appcompat.app.AlertDialog

class DialogStandard {
    companion object{
        fun showDialog(context: Context, onConfirm: ()-> Unit): AlertDialog {

            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            val title = SpannableString("Confirmar eliminación")
            title.setSpan(RelativeSizeSpan(0.9f), 0, title.length, 0)

            val message = SpannableString("¿Estás seguro de que deseas eliminar este producto?")
            message.setSpan(RelativeSizeSpan(0.8f), 0, message.length, 0)

            builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Sí") { dialog, _ ->
                    onConfirm()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

            return builder.create()
        }


    }
}