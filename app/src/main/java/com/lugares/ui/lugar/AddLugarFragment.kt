package com.lugares.ui.lugar

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel
import java.util.jar.Manifest


class AddLugarFragment : Fragment() {

private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!
private  lateinit var lugarViewModel: LugarViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        binding.btAgregar.setOnClickListener{
            addLugar();
        }

 ubicaGPS()

        return binding.root
    }

    private var conPermiso:Boolean = true;
    private fun ubicaGPS() {
  val fusedLocationProviderClient : FusedLocationProviderClient =
      LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION),105)
        }
        if(conPermiso){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                location: Location ->
                if (location != null){
                    binding.tvLatitud.text="${location.latitude}"
                    binding.tvLongitud.text="${location.longitude}"
                    binding.tvAltura.text="${location.altitude}"
                }else{

                    binding.tvLatitud.text=getString(R.string.error)
                    binding.tvLongitud.text=getString(R.string.error)
                    binding.tvAltura.text=getString(R.string.error)

                }
            }

        }
    }

    private fun addLugar(){
        var nombre = binding.etNombre.text.toString()
        if(nombre.isNotEmpty()){ //podemos insertar un lugar
            var correo = binding.etCorreo.text.toString()
            var telefono = binding.etTelefono.text.toString()
            var web = binding.etWeb.text.toString()
var latitud = binding.tvLatitud.text.toString().toDouble()
var longitud = binding.tvLongitud.text.toString().toDouble()
var altura = binding.tvAltura.text.toString().toDouble()



            var lugar = Lugar("",nombre,correo,telefono,web,latitud,longitud,altura,"","")
          lugarViewModel.addLugar(lugar)
            Toast.makeText(requireContext(),"Lugar Agregado",Toast.LENGTH_SHORT).show()


        }else{

            Toast.makeText(requireContext(),"Faltan Datos",Toast.LENGTH_LONG).show()

        }

        findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)

    }

    override fun onDestroy(){
        super.onDestroy()
        _binding = null

    }

}