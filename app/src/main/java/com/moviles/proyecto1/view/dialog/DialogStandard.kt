package com.moviles.proyecto1.view.dialog

import android.content.Context

import androidx.appcompat.app.AlertDialog

class DialogStandard {
    companion object{
        fun showDialog(context: Context, onConfirm: ()-> Unit): AlertDialog {

            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este producto?")
                .setPositiveButton("Si") { dialog, _ ->
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