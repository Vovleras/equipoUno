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
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels

class HomeInventory : Fragment() {
    private lateinit var binding: FragmentHomeInventoryBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (verifySession()) {
            binding = FragmentHomeInventoryBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = this
            binding.root
        } else {
            Toast.makeText(context,"No ha iniciado sesión", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeInventory_to_login)
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationHomeInventoryToAdd()
        listInventory()
        toolBar()
        progressDB()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().moveTaskToBack(true)
        }
    }

    private fun listInventory() {

        inventoryViewModel.getListInventory()
        inventoryViewModel.listInventory.observe(viewLifecycleOwner) { listInventory ->
            Log.d("HomeInventory", "Lista de inventario actual: $listInventory")
            val recycler = binding.rvProducts
            recycler.layoutManager = LinearLayoutManager(context)
            val adapter = RecyclerAdapter(listInventory,  findNavController())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun navigationHomeInventoryToAdd(){
        binding.btnAdd.setOnClickListener {
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