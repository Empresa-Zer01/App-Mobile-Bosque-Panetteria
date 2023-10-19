package com.app.bosquepanetteriaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView

class CartItemAdapter(
    private val context: Context,
    private val resource: Int,
    private val objects: List<CartItem>,
    private val deleteItemCallback: (CartItem) -> Unit
) : ArrayAdapter<CartItem>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView

        if (itemView == null) {
            val inflater = LayoutInflater.from(context)
            itemView = inflater.inflate(resource, parent, false)
        }

        val currentItem = getItem(position)

        val itemNameTextView = itemView!!.findViewById<TextView>(R.id.itemName)
        val itemPriceTextView = itemView.findViewById<TextView>(R.id.itemPrice)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)

        if (currentItem != null) {
            itemNameTextView.text = currentItem.productName
            itemPriceTextView.text = String.format("R$ %.2f", currentItem.productPrice)

            // Defina o ouvinte de clique para o botão de exclusão
            deleteButton.setOnClickListener {
                deleteItemCallback(currentItem)
            }
        }

        return itemView
    }
}
