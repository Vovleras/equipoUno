package com.moviles.proyecto1.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import com.moviles.proyecto1.R
import androidx.recyclerview.widget.RecyclerView
import com.moviles.proyecto1.databinding.ItemProductBinding
import com.moviles.proyecto1.model.Inventory
import java.util.Locale
import android.util.Log


class InventoryViewHolder(binding: ItemProductBinding, navController: NavController):
    RecyclerView.ViewHolder(binding.root) {

    val bindingItem = binding
    val navController = navController
    fun setItemInventory(inventory: Inventory) {

        val localeES = Locale.Builder().setLanguageTag("es-CO").build()
        bindingItem.tvName.text = inventory.name
        bindingItem.tvID.text = "id: ${inventory.code.toString()}"
        bindingItem.tvPrice.text = String.format(localeES,"\$%,.2f", inventory.price)
        //bindingItem.tvQuantity.text = "${inventory.quantity}"

        bindingItem.cvProducts.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", inventory)
            navController.navigate(R.id.action_homeInventory_to_detailProduct, bundle)
        }
    }
}