package com.moviles.proyecto1.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentAddProductBinding
import com.moviles.proyecto1.model.Inventory
import com.moviles.proyecto1.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class AddProduct : Fragment() {

    private lateinit var binding: FragmentAddProductBinding

    private val inventoryViewModel: InventoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater)
        setupToolbar()
        binding.btnGuardar.isEnabled = false
        binding.lifecycleOwner = this



        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller()
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

        val areFilled = !codigo.isNullOrEmpty() && !nombre.isNullOrEmpty() && !precio.isNullOrEmpty() && !cantidad.isNullOrEmpty()

        btnGuardar.isEnabled = areFilled

        binding.btnGuardar.setTypeface(
            null,
            if (areFilled) Typeface.BOLD else Typeface.NORMAL
        )

    }

    private fun setupFieldListeners() {
        binding.EDcodigoProducto.editText?.addTextChangedListener { validateFields() }
        binding.EDnombreArticulo.editText?.addTextChangedListener { validateFields() }
        binding.EDprecio.editText?.addTextChangedListener { validateFields() }
        binding.EDcantidad.editText?.addTextChangedListener { validateFields() }
    }

    private fun controller(){
        setupFieldListeners()
        binding.btnGuardar.setOnClickListener {
            getInventario()
            findNavController().navigate(R.id.action_addProduct_to_homeInventory)
        }
    }

    private fun getInventario(){
        val codigo = binding.editCodigoProducto.text.toString().toInt()
        val nombre = binding.editNombreArticulo.text.toString().trim()
        val precio = binding.editPrecio.text.toString().toFloat()
        val cantidad = binding.editCantidad.text.toString().toInt()
        val totalProd = inventoryViewModel.totalProduct(precio, cantidad)
        inventoryViewModel.addProduct( codigo, nombre,precio,cantidad,totalProd)
        Toast.makeText(requireContext(), "Producto agregado", Toast.LENGTH_SHORT).show()



    }



}