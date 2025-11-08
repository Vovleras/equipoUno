package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentDetailProductBinding
import com.moviles.proyecto1.viewmodel.InventoryViewModel
import kotlin.getValue
import com.moviles.proyecto1.model.Inventory
import com.moviles.proyecto1.view.dialog.DialogStandard.Companion.showDialog

class DetailProduct : Fragment() {
    private lateinit var binding: FragmentDetailProductBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProductBinding.inflate(inflater)

        showInfo()
        deleteProduct()
        setupToolbar()
        setupObservers()
        navigateEdit()

        binding.lifecycleOwner = this
        return binding.root
    }

    private fun deleteProduct(){
        val inventory = getBundle()
        inventory?.let { inv ->
            binding.btnEliminar.setOnClickListener {

                showDialog(requireContext()) {
                    inventoryViewModel.deleteProduct(inventory)
                    findNavController().navigate(R.id.action_detailProduct_to_homeInventory)
                }.show()

            }
        }
    }


    private fun showInfo(){
        val inventory = getBundle()
        if (inventory != null) {
            val id = inventory.id
            val name = inventory.name
            val price = inventory.price
            val quantity = inventory.quantity

            inventoryViewModel.calculateTotalPerProduct(id, price, quantity)
            binding.tvTituloProducto.text = name
            binding.tvValorPrecio.text = "${price}"
            binding.tvValorCantidad.text = "$quantity"
        }
    }

    private fun getBundle(): Inventory? {
        val inventory = arguments?.getSerializable("clave") as Inventory ?
        return  inventory
    }



    private fun createBundleFromProduct(): Bundle {
        val product = getBundle()
        val bundle = Bundle()
        bundle.putSerializable("clave", product)
        return bundle
    }

    private fun setupToolbar(){
        binding.contentToolbar.title = "Detalle del Producto"
        binding.contentToolbar.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_detailProduct_to_homeInventory)
        }

    }
    private fun navigateEdit(){
        binding.ibEditProduct.setOnClickListener {
            val bundle  = createBundleFromProduct()
            findNavController().navigate(R.id.action_detailProduct_to_editProduct, bundle)
        }

    }

    private fun setupObservers() {

        inventoryViewModel.deleteMessage.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        inventoryViewModel.totalPerProduct.observe(viewLifecycleOwner) { total ->
            binding.tvValorTotal.text = "$${"%.2f".format(total)}"
        }
    }



}

