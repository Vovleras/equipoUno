package com.moviles.proyecto1.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentAddProductBinding
import com.moviles.proyecto1.databinding.FragmentDetailProductBinding

class AddProduct : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater)
        setupToolbar()
        binding.btnGuardar.isEnabled = false
        binding.lifecycleOwner = this

        setupFieldListeners()


        return binding.root

    }

    private fun setupToolbar(){
        binding.contentToolbar.title = "Agregar Producto"
        binding.contentToolbar.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_addProduct_to_homeInventory)
        }

    }

    private fun validateFields() {
        val codigo = binding.editCodigoProducto.text?.toString()?.trim()
        val nombre = binding.editNombreArticulo.text?.toString()?.trim()
        val precio = binding.editPrecio.text?.toString()?.trim()
        val cantidad = binding.editCantidad.text?.toString()?.trim()

        val btnGuardar = binding.btnGuardar

        val areFilled = !codigo.isNullOrEmpty() &&
                !nombre.isNullOrEmpty() &&
                !precio.isNullOrEmpty() &&
                !cantidad.isNullOrEmpty()

        btnGuardar.apply {
            isEnabled = areFilled
            setTypeface(null, if (areFilled) Typeface.BOLD else Typeface.NORMAL)
        }

    }

    private fun setupFieldListeners() {
        binding.EDcodigoProducto.editText?.addTextChangedListener { validateFields() }
        binding.EDnombreArticulo.editText?.addTextChangedListener { validateFields() }
        binding.EDprecio.editText?.addTextChangedListener { validateFields() }
        binding.EDcantidad.editText?.addTextChangedListener { validateFields() }
    }



}