package com.app.bosquepanetteriaapp

import android.os.Parcel
import android.os.Parcelable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray

data class Product(val prod_id: Int, val prod_nome: String, val prod_imagem: String, val prod_preco: Double, val prod_descricao: String) : Parcelable {
    // Construtor Parcelable
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    // Método para fazer a solicitação à API e obter a lista de produtos
    companion object {
        fun fetchProducts(apiUrl: String): List<Product> {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(apiUrl)
                .build()

            val productList = mutableListOf<Product>()

            try {
                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseData: String = response.body?.string() ?: ""
                    val jsonArray = JSONArray(responseData)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val prod_id = jsonObject.getInt("prod_id")
                        val prod_nome = jsonObject.getString("prod_nome")
                        val prod_imagem = jsonObject.getString("prod_imagem")
                        val prod_preco = jsonObject.getDouble("prod_preco")
                        val prod_descricao = jsonObject.getString("prod_descricao")

                        val product = Product(prod_id, prod_nome, prod_imagem, prod_preco, prod_descricao)
                        productList.add(product)
                    }
                } else {
                    // Tratar erro de resposta da API, se necessário
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Tratar erro de rede ou parsing JSON, se necessário
            }

            return productList
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(parcel: Parcel): Product {
                return Product(parcel)
            }

            override fun newArray(size: Int): Array<Product?> {
                return arrayOfNulls(size)
            }
        }
    }

    // Restante do código Parcelable
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(prod_id)
        parcel.writeString(prod_nome)
        parcel.writeString(prod_imagem)
        parcel.writeDouble(prod_preco)
        parcel.writeString(prod_descricao)
    }

    override fun describeContents(): Int {
        return 0
    }
}
