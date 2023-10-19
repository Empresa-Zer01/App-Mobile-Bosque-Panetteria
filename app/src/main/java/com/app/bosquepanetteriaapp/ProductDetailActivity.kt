package com.app.bosquepanetteriaapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import android.widget.Toast

class ProductDetailActivity : AppCompatActivity() {
    private val cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productdetail)
        window.statusBarColor = Color.WHITE

        // Obter o ID do produto da Intent
        val productId = intent.getIntExtra("productId", -1)

        // Obter a lista de produtos da Intent (você deve ter passado isso da HomeActivity)
        val productList = intent.getParcelableArrayListExtra<Product>("productList")

        // Verifique se productList não é nulo e se productId é válido antes de buscar os detalhes do produto
        if (productList != null && productId != -1) {
            // Agora você tem o ID do produto e a lista de produtos, você pode buscar os detalhes do produto com base nesse ID
            val product = getProductDetails(productId, productList)

            // Verifique se o produto não é nulo antes de exibir as informações
            if (product != null) {
                val productDetailImage = findViewById<ImageView>(R.id.productDetailImage)
                val productDetailName = findViewById<TextView>(R.id.productDetailName)
                val productDetailPrice = findViewById<TextView>(R.id.productDetailPrice)
                val productDetailDescription = findViewById<TextView>(R.id.productDetailDescription)
                val addToCartButton = findViewById<Button>(R.id.addToCartButton)
                val cartIcon = findViewById<ImageView>(R.id.carrinho)

                // Use o Picasso para carregar a imagem a partir da URL
                Picasso.get().load(product.prod_imagem).into(productDetailImage)

                productDetailName.text = product.prod_nome
                productDetailPrice.text = String.format("R$ %.2f", product.prod_preco)

                // Defina a descrição do produto no TextView
                productDetailDescription.text = product.prod_descricao

                addToCartButton.setOnClickListener {
                    // Adicione o produto como um item no carrinho
                    val cartItem = CartItem(product.prod_id, product.prod_nome, product.prod_preco, 1)
                    cartItems.add(cartItem)

                    Log.d("ProductDetailActivity", "Product added to cart: ${product.prod_nome}")

                    val mensagem = "Produto adicionado ao carrinho!"
                    val duracao = Toast.LENGTH_SHORT // ou Toast.LENGTH_LONG para uma duração mais longa
                    val toast = Toast.makeText(applicationContext, mensagem, duracao)
                    toast.show()
                }

                // Defina o clique na imagem do carrinho para abrir a tela do carrinho
                cartIcon.setOnClickListener {
                    openCartActivity()
                }
            } else {
                Log.e("ProductDetailActivity", "Product not found for ID: $productId")
            }
        } else {
            Log.e("ProductDetailActivity", "Product list is null or productId is invalid")
        }
    }

    // Método para buscar os detalhes do produto com base no ID e na lista de produtos
    private fun getProductDetails(productId: Int, productList: List<Product>): Product? {
        // Implemente a lógica para buscar os detalhes do produto da sua fonte de dados real (por exemplo, a lista productList)
        // Retorne o objeto Product correspondente ao ID ou null se não for encontrado
        return productList.find { it.prod_id == productId }
    }

    // Método para abrir a tela do carrinho
    private fun openCartActivity() {
        val intent = Intent(this, CartActivity::class.java)
        intent.putParcelableArrayListExtra("cartItems", ArrayList(cartItems))
        startActivity(intent)
    }
}
