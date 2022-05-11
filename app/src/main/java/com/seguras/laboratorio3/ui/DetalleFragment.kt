package com.seguras.laboratorio3.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.seguras.laboratorio3.R
import com.seguras.laboratorio3.databinding.FragmentDetalleBinding
import com.seguras.laboratorio3.model.Restaurante
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetalleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetalleFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDetalleBinding
    private lateinit var map: GoogleMap
    private lateinit var restaurante: Restaurante

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurante = arguments?.getSerializable("selecRest") as Restaurante

        with(binding){
            tvDname.text = restaurante.nombre
            tvDcalificacion.text = "Calificación: " + restaurante.calificacion
            tvDcosto.text = "Costo Promedio: " + restaurante.costo
            tvDfundacion.text = "Fundando desde " + restaurante.ano
            tvResena.text = restaurante.resena
            tvDireccion.text = restaurante.direccion
            Picasso.get().load(restaurante.fotos[0]).into(ivUno)
            Picasso.get().load(restaurante.fotos[1]).into(ivDos)
            Picasso.get().load(restaurante.fotos[2]).into(ivTres)
            Picasso.get().load(restaurante.fotos[3]).into(ivCuatro)
        }

        createFragment()

        //createMarker(restaurante.nombre, restaurante.cordenadas[0], restaurante.cordenadas[1])
    }

    private fun createFragment(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker(restaurante.nombre, restaurante.cordenadas[0], restaurante.cordenadas[1])
        enalbeLocation()
    }

    private fun createMarker(title: String ,latitude: Double, longitude: Double) {
        map.clear()
        val coordinates = LatLng(latitude, longitude)
        val marker = MarkerOptions().position(coordinates).title(title)
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude),16f)
        )
    }

    @SuppressLint("MissingPermission")
    private fun enalbeLocation(){
        if(!::map.isInitialized)return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }else{
            requestLocationPermission()
        }
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            //Mostrar la ventana de permiso
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(activity, "Para activar permiso, ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetalleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetalleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val REQUEST_CODE_LOCATION = 0
    }


}