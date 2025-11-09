package com.moviles.proyecto1.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentEditProductBinding
import com.moviles.proyecto1.model.Inventory
import com.moviles.proyecto1.viewmodel.InventoryViewModel
import kotlin.text.toFloat

class EditProduct : Fragment() {
  private lateinit var binding: FragmentEditProductBinding
  private val inventoryViewModel: InventoryViewModel by viewModels()
  private var product: Inventory? = null
  private var isUpdating = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentEditProductBinding.inflate(inflater)
    setupToolbar()
    setupFieldListeners()
    populateFields()
    binding.btnEditar.isEnabled = false
    binding.lifecycleOwner = this
    binding.btnEditar.setOnClickListener { pressButton() }
    return binding.root
  }

  private fun setupToolbar() {
    binding.contentToolbar.title = "Editar Producto"
    binding.contentToolbar.toolbar.setNavigationOnClickListener {
      findNavController().navigate(R.id.action_editProduct_to_detailProduct)
    }

  }

  private fun populateFields() {
    product = arguments?.getSerializable("clave", Inventory::class.java)
    product?.let { p ->
      isUpdating = true
      binding.labelNumero.text = p.id.toString()
      binding.editNombreArticulo.setText(p.name)
      binding.editPrecio.setText(p.price.toString())
      binding.editCantidad.setText(p.quantity.toString())
      isUpdating = false
      validateFields()
    }
  }

  private fun validateFields() {
    if (isUpdating) return

    val id = binding.labelNumero.text.toString().toIntOrNull()
    val nombre = binding.editNombreArticulo.text.toString().trim()
    val precio = binding.editPrecio.text.toString().toFloatOrNull()
    val cantidad = binding.editCantidad.text.toString().toIntOrNull()

    val areFilled = id != null && nombre.isNotEmpty() && precio != null && cantidad != null

    binding.btnEditar.apply {
      isEnabled = areFilled
//      setTypeface(null, if (areFilled) Typeface.BOLD else Typeface.NORMAL)
    }
  }

  private fun setupFieldListeners() {
    binding.editNombreArticulo.addTextChangedListener { validateFields() }
    binding.editPrecio.addTextChangedListener { validateFields() }
    binding.editCantidad.addTextChangedListener { validateFields() }
  }

  private fun pressButton() {
    val updatedProduct = Inventory(
      id = binding.labelNumero.text.toString().toInt(),
      name = binding.editNombreArticulo.text.toString().trim(),
      price = binding.editPrecio.text.toString().toFloat(),
      quantity = binding.editCantidad.text.toString().toInt(),
      total = inventoryViewModel.totalProduct(  //Recalcula el total
        binding.editPrecio.text.toString().toFloat(),
        binding.editCantidad.text.toString().toInt()
      )
    )

    inventoryViewModel.updateInventory(updatedProduct)

    Toast.makeText(requireContext(), "Producto actualizado", Toast.LENGTH_SHORT).show()

    findNavController().navigate(R.id.action_editProduct_to_homeInventory)
  }


}