package com.seguras.laboratorio3.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.seguras.laboratorio3.R
import com.seguras.laboratorio3.adapter.OnRestClickListener
import com.seguras.laboratorio3.adapter.RestauranteAdapter
import com.seguras.laboratorio3.databinding.FragmentRestaurantesBinding
import com.seguras.laboratorio3.model.Restaurante
import com.seguras.laboratorio3.services.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantesFragment : Fragment(), OnRestClickListener {

    private lateinit var binding: FragmentRestaurantesBinding
    lateinit var autenticacion: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private lateinit var adapter: RestauranteAdapter
    private var restaurantes = ArrayList<Restaurante>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        autenticacion = FirebaseAuth.getInstance()
        db = Firebase.firestore

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onStart() {
        super.onStart()

        val user: FirebaseUser? = autenticacion.currentUser
        if(user == null){
            Navigation.findNavController(requireView()).navigate(R.id.action_restaurantesFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user: FirebaseUser? = autenticacion.currentUser
        val userData = db.collection("usuarios")
            .whereEqualTo("id", user?.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    binding.tvUser.text = "Bienvenido "+document.data.get("Nombre").toString()
                }
            }
            .addOnFailureListener { exeption ->
                Log.w(TAG, "Error al obtener el usuario", exeption)
            }

        initRecyclerView()
        loadRestaurantes()



        //binding.tvUser.text = user?.email ?: "User"
        //binding.tvUser.text = userData.


        binding.btnLogout.setOnClickListener {
            autenticacion.signOut()
            if(autenticacion.currentUser==null){
                Navigation.findNavController(view).navigate(R.id.action_restaurantesFragment_to_loginFragment)
            }else{
                Toast.makeText(activity,"Fallo Logout", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView(){
        adapter = RestauranteAdapter(restaurantes, this)
        binding.rvRestaurantes.layoutManager = LinearLayoutManager(activity)
        binding.rvRestaurantes.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://demo4802870.mockable.io/restaurantes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadRestaurantes(){
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<ArrayList<Restaurante>> = getRetrofit().create(APIService::class.java).getRestaurantes("getlist")
            val items: ArrayList<Restaurante>? = call.body()
            activity?.runOnUiThread {
                if (call.isSuccessful) {
                    restaurantes.clear()
                    restaurantes.addAll(items!!)
                    adapter.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
    }

    private fun showError(){
        Toast.makeText(activity, "Error en la carga de datos", Toast.LENGTH_LONG).show()
    }

    override fun onRestItemClicked(view: View, position: Int) {
        val param = Bundle()
        param.putSerializable("selecRest",restaurantes[position])
        Navigation.findNavController(view).navigate(R.id.action_restaurantesFragment_to_detalleFragment,param)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RestaurantesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RestaurantesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}