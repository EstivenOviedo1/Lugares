package com.lugares.ui.lugar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel


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

        return binding.root
    }

    private fun addLugar(){
        var nombre = binding.etNombre.text.toString()
        if(nombre.isNotEmpty()){ //podemos insertar un lugar
            var correo = binding.etCorreo.text.toString()
            var telefono = binding.etTelefono.text.toString()
            var web = binding.etWeb.text.toString()
            var lugar = Lugar(0,nombre,correo,telefono,web,0.0,0.0,0.0,"","")
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