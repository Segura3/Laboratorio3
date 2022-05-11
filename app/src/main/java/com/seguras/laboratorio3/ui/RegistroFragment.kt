package com.seguras.laboratorio3.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.seguras.laboratorio3.R
import com.seguras.laboratorio3.databinding.FragmentRegistroBinding
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistroFragment : Fragment() {

    private lateinit var binding: FragmentRegistroBinding
    lateinit var autenticacion: FirebaseAuth
    lateinit var db: FirebaseFirestore

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistroBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuardar.setOnClickListener {
            if(validaCampos()){
                registro(view)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.tietRName.setText("")
        binding.tietRName.error = null
        binding.tietRApellido.setText("")
        binding.tietRApellido.error = null
        binding.tietRMail.setText("")
        binding.tietRMail.error = null
        binding.tietRPass.setText("")
        binding.tietRPass.error = null
    }

    fun registro(view: View){
        val name: String = binding.tietRName.text.toString()
        val apellido: String = binding.tietRApellido.text.toString()
        val correo: String = binding.tietRMail.text.toString()
        val psw: String = binding.tietRPass.text.toString()

        autenticacion.createUserWithEmailAndPassword(correo,psw).addOnCompleteListener {
            if(it.isSuccessful){
                Log.i("USER", autenticacion.currentUser?.uid.toString())
                val id: String = autenticacion.currentUser?.uid.toString()

                val userData = hashMapOf(
                    "Nombre" to name,
                    "Apellido" to apellido,
                    "id" to id
                )

                db.collection("usuarios")
                    .add(userData)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error al añadir los datos", e)
                    }

                autenticacion.signOut()
                if(autenticacion.currentUser==null){
                    Toast.makeText(activity,"Registro exitoso", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_registroFragment_to_loginFragment)
                }
            }else{
                Toast.makeText(activity,"Error en el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun validaCampos(): Boolean{
        val pswRegex = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=[^A-Za-z]*[A-Za-z])" + //at least 1 letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 6 characters
                "$")
        var res = true

        with(binding){
            if(tietRName.text.toString().isEmpty()){
                tietRName.error = getString(R.string.valreq)
                res = false
            }else tietRName.error = null
            if(tietRApellido.text.toString().isEmpty()){
                tietRApellido.error = getString(R.string.valreq)
                res = false
            }else tietRApellido.error = null
            if(tietRMail.text.toString().isEmpty()){
                tietRMail.error = getString(R.string.valreq)
                res = false
            }else if(!Patterns.EMAIL_ADDRESS.matcher(tietRMail.text.toString()).matches()){
                tietRMail.error = getString(R.string.correo_valido)
                res = false
            }else tietRMail.error = null
            if(tietRPass.text.toString().isEmpty()){
                tietRPass.error = getString(R.string.valreq)
                res = false
            }else if(!pswRegex.matcher(tietRPass.text).matches()){
                tietRPass.error = "La contraseña debe tener al menos 8 caracteres que incluyan por lo menos una letra y un numero"
                res = false
            }else tietRPass.error = null
        }
        return res
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}