package com.lugares.ui.lugar

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import java.util.jar.Manifest


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

        binding.tvLongitud.text =args.lugar.longitud.toString()
        binding.tvLatitud.text =args.lugar.latitud.toString()
        binding.tvAltura.text =args.lugar.altura.toString()

       // binding.etweb.setText(args.lugar.web)

binding.btActualizar.setOnClickListener{
    updateLugar();
}
        binding.btEmail.setOnClickListener{
           escribirCorreo ();
        }


        binding.btPhone.setOnClickListener{
            llamarLugar ();
        }

        binding.btWhatsapp.setOnClickListener{
            enviarWhatsapp ();
        }

        binding.btWeb.setOnClickListener{
            verWeb ();
        }

        binding.btLocation.setOnClickListener{
            verMapa ();
        }


        setHasOptionsMenu(true)
        return binding.root
    }

    private fun escribirCorreo() {
        val para = binding.etCorreo.text.toString()
        if(para.isNotEmpty()){
            val intento = Intent(Intent.ACTION_SEND)
            intento.type = "message/rfc822"
            intento.putExtra(Intent.EXTRA_EMAIL, arrayOf(para))
            intento.putExtra(Intent.EXTRA_SUBJECT,
            getString(R.string.msg_saludos)+ " "+ binding.etNombre.text)
            intento.putExtra(Intent.EXTRA_TEXT,getString(R.string.msg_mensaje_correo))
            startActivity(intento)
        }else {
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }


    private fun llamarLugar() {
        val telefono = binding.etCorreo.text.toString()
        if(telefono.isNotEmpty()){
            val intento = Intent(Intent.ACTION_CALL)
intento.data = Uri.parse("tel:$telefono")
            if (requireActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED){
requireActivity().requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE), 105)
            }else{
                requireActivity().startActivity(intento)
            }
        }else {
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun enviarWhatsapp() {
        val telefono = binding.etTelefono.text.toString()

        if(telefono.isNotEmpty()){
            val intento = Intent(Intent.ACTION_VIEW)
   val uri = "whatsapp://send?phone=506$telefono&text=" +
           getString(R.string.msg_saludos)
            intento.setPackage("com.whatsapp")
            intento.data=Uri.parse(uri)
            startActivity(intento)
        }else {
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }



    private fun verWeb() {
        val sitio = binding.etWeb.text.toString()

        if(sitio.isNotEmpty()){

            val uri = Uri.parse("http://$sitio")
            val intento = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intento)
        }else {
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }



    private fun verMapa() {
        val latittud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()

        if(latittud.isFinite() && longitud.isFinite()){

            val location = Uri.parse("geo:$latittud,$longitud?z=18")

            val intento = Intent(Intent.ACTION_VIEW,location)
            startActivity(intento)
        }else {
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
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