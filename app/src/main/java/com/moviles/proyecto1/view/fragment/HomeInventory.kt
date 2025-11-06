package com.moviles.proyecto1.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviles.proyecto1.R
import com.moviles.proyecto1.databinding.FragmentHomeInventoryBinding
import com.moviles.proyecto1.model.Inventory
import com.moviles.proyecto1.view.adapter.RecyclerAdapter

class HomeInventory : Fragment() {
    private lateinit var binding: FragmentHomeInventoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeInventoryBinding.inflate(inflater)
        //navigationHomeInventoryToDetails()
        navigationHomeInventoryToAdd()
        listInventory()
        toolBar()
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun listInventory() {
        var listProducts = mutableListOf(
            Inventory(4,"zapatos", 100.0f, 5),
            Inventory(5,"camisas", 100.0f, 3),
        )

        val recycler = binding.rvProducts
        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerAdapter(listProducts,  findNavController())
        recycler.adapter = adapter
        adapter.notifyDataSetChanged()
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
        Toast.makeText(context,"Salir", Toast.LENGTH_SHORT).show()
    }

}