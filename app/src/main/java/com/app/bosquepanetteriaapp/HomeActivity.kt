package com.app.bosquepanetteriaapp

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray

class HomeActivity : Fragment() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        activity?.window?.statusBarColor = Color.TRANSPARENT

        val bottomNavigationView: BottomNavigationView = rootView.findViewById(R.id.btnv)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.findItem(R.id.menu_home).isChecked = true

        val btnMenuHome = rootView.findViewById<ImageView>(R.id.btn_menu_home)
        drawerLayout = rootView.findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = rootView.findViewById(R.id.nav_view)
        (activity as AppCompatActivity).setSupportActionBar(rootView.findViewById(R.id.toolbar_home))

        val toggle = ActionBarDrawerToggle(
            activity, drawerLayout, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_home, PlaceholderFragment())
                .commit()
            navigationView.setCheckedItem(R.id.nav_conta)
        }

        btnMenuHome.setOnClickListener {
            drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_conta -> {
                    // Adicione a lógica para a ação "Minha Conta" aqui
                    return@setNavigationItemSelectedListener true
                }
                // Adicione mais casos conforme necessário
                else -> return@setNavigationItemSelectedListener false
            }
        }
        return rootView
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // Remova esta parte para evitar a reinicialização da atividade atual
                    return@OnNavigationItemSelectedListener false
                }

                R.id.menu_cardapio -> {
                    startActivity(Intent(activity, CardapioActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_pedidos -> {
                    startActivity(Intent(activity, PedidosActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_carrinho -> {
                    startActivity(Intent(activity, CartActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun openCartActivity(view: View) {
        // Obtenha o valor do item de alguma forma (substitua isso pela sua lógica)
        val itemValue: String = "Valor do item"

        val intent = Intent(activity, CartActivity::class.java)
        intent.putExtra("item_value", itemValue)
        startActivity(intent)
    }

    private inner class FetchProductDataTask(private val recyclerView: RecyclerView) :
        AsyncTask<Void, Void, List<Product>>() {

        override fun doInBackground(vararg params: Void?): List<Product> {
            val apiUrl =
                "https://windy-energy-396600.rj.r.appspot.com/produtos" // Substitua pela URL da sua API
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(apiUrl)
                .build()

            val products = mutableListOf<Product>()

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

                        val product =
                            Product(prod_id, prod_nome, prod_imagem, prod_preco, prod_descricao)
                        products.add(product)
                    }
                } else {
                    // Tratar erro de resposta da API, se necessário
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Tratar erro de rede ou parsing JSON, se necessário
            }

            return products
        }

        override fun onPostExecute(result: List<Product>?) {
            super.onPostExecute(result)

            // Atualize a interface do usuário com os resultados após a conclusão da tarefa em segundo plano
            if (result != null) {
                val adapter = ProductAdapter(result) { productId ->
                    val intent = Intent(activity, ProductDetailActivity::class.java)
                    intent.putExtra(
                        "productId",
                        productId
                    ) // Substitua 'productId' pelo ID do produto que você deseja passar
                    intent.putParcelableArrayListExtra("productList", ArrayList(result))
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            }
        }
    }

    class PlaceholderFragment : Fragment() {
        // Adicione qualquer lógica específica do fragmento aqui
    }
}