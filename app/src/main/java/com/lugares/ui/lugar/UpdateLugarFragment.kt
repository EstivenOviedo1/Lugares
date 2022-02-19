package com.lugares.ui.lugar

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares.R
import com.lugares.databinding.FragmentUpdateLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel


class UpdateLugarFragment : Fragment() {

    private val args by navArgs<UpdateLugarFragmentArgs>()

private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!
private  lateinit var lugarViewModel: LugarViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        //setiar el nombre en la pantallla dde actualizar
        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
       // binding.etNombre.setText(args.lugar.nombre)



binding.btActualizar.setOnClickListener{
    updateLugar();
}
setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){

            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateLugar(){
        var nombre = binding.etNombre.text.toString()
        if(nombre.isNotEmpty()){ //podemos insertar un lugar
            var correo = binding.etCorreo.text.toString()
            var telefono = binding.etTelefono.text.toString()
            var web = binding.etWeb.text.toString()
            var lugar = Lugar(args.lugar.id,nombre,correo,telefono,web,0.0,0.0,0.0,"","")
          lugarViewModel.updateLugar(lugar)
            Toast.makeText(requireContext(),"Lugar Actualizado",Toast.LENGTH_SHORT).show()


        }else{

            Toast.makeText(requireContext(),"Error al actualizar",Toast.LENGTH_LONG).show()

        }

        findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)

    }

    private  fun deleteLugar(){

        val builder =  AlertDialog.Builder(requireContext())

        builder.setPositiveButton(getString(R.string.si)){_,_ ->
            lugarViewModel.deleteLugar(args.lugar)
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }
        builder.setNegativeButton(getString(R.string.no)){_,_ ->}
        builder.setTitle(R.string.menu_delete)
        builder.setMessage(getString(R.string.msg_seguro_borrar)+ " ${args.lugar.nombre}?")
        builder.create().show()
    }

    override fun onDestroy(){
        super.onDestroy()
        _binding = null

    }

}