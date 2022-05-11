package com.seguras.laboratorio3.ui

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.seguras.laboratorio3.R
import com.seguras.laboratorio3.databinding.FragmentLoginBinding
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    lateinit var autenticacion: FirebaseAuth

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        autenticacion = FirebaseAuth.getInstance()

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEntrar.setOnClickListener {
            if(validaCampos()){
                login(view)
            }
        }

        binding.btnReg.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registroFragment)
        }
    }

    override fun onStart() {
        super.onStart()

        val user: FirebaseUser? = autenticacion.currentUser
        if(user != null){
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_restaurantesFragment)
        }

        binding.tietMail.setText("")
        binding.tietMail.error = null
        binding.tietPass.setText("")
        binding.tietPass.error = null
    }

    fun login(view: View){
        val correo: String = binding.tietMail.text.toString()
        val password: String = binding.tietPass.text.toString()

        autenticacion.signInWithEmailAndPassword(correo,password).addOnCompleteListener {
            if(it.isSuccessful){
                //Avansar a la pantalla principal
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_restaurantesFragment)
            }else{
                //Mostrar un resutado de error
                Toast.makeText(activity, "Usuario no registrado", Toast.LENGTH_SHORT).show()
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
            if(tietMail.text.toString().isEmpty()){
                tietMail.error = getString(R.string.valreq)
                res = false
            }else if(!Patterns.EMAIL_ADDRESS.matcher(tietMail.text.toString()).matches()){
                tietMail.error = getString(R.string.correo_valido)
                res = false
            }else tietMail.error = null
            if(tietPass.text.toString().isEmpty()){
                tietPass.error = getString(R.string.valreq)
                res = false
            }else tietPass.error = null
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}