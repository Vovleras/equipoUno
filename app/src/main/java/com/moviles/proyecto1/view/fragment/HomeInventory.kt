package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentHomeInventoryBinding
import com.moviles.proyecto1.model.Inventory
import com.moviles.proyecto1.viewmodel.InventoryViewModel
import com.moviles.proyecto1.view.adapter.RecyclerAdapter
import android.content.Context
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels

class HomeInventory : Fragment() {
    private lateinit var binding: FragmentHomeInventoryBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeInventoryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (verifySession()) {
            navigationHomeInventoryToAdd()
            listInventory()
            toolBar()
        } else {
            Toast.makeText(context,"No ha iniciado sesión", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeInventory_to_login)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().moveTaskToBack(true)
        }
    }

    private fun listInventory() {
        //Agregar productos mientras, eliminar cuando este add

        val dato1 = Inventory(4,"zapatos", 100.0f, 5)
        val dato2 = Inventory(5,"camisas", 2000000.459f, 3)
        inventoryViewModel.saveInventory(dato1)
        inventoryViewModel.saveInventory(dato2)

        // Borrar hasta aqui

        inventoryViewModel.getListInventory()
        inventoryViewModel.listInventory.observe(viewLifecycleOwner) { listInventory ->
            val recycler = binding.rvProducts
            recycler.layoutManager = LinearLayoutManager(context)
            val adapter = RecyclerAdapter(listInventory,  findNavController())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun navigationHomeInventoryToAdd(){
        binding.btnAdd.setOnClickListener {
            val dato = Inventory(20,"pantalones", 80000.567f, 3) //Borrar
            inventoryViewModel.saveInventory(dato)
            findNavController().navigate(R.id.action_homeInventory_to_addProduct)
        }
    }

    private fun toolBar(){
        binding.homeToolbar.title = "Inventario"
        binding.homeToolbar.navIcon = true
        binding.homeToolbar.exitIcon = true
        binding.homeToolbar.btnExit.setOnClickListener {
            exit()
        }
    }

    private fun exit() {
        val sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        Toast.makeText(context,"Sesión Cerrada", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_homeInventory_to_login)
    }

    private fun verifySession(): Boolean {
        val session = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return session.getBoolean("is_logged_in", false)
    }

    private fun progressDB() {
        inventoryViewModel.progresState.observe(viewLifecycleOwner){status ->
            binding.progress.isVisible = status
        }
    }

}