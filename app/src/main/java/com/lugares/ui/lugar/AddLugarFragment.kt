package com.lugares.ui.lugar

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.model.Lugar
import com.lugares.utiles.AudioUtiles
import com.lugares.utiles.ImagenUtiles
import com.lugares.viewmodel.LugarViewModel
import java.util.jar.Manifest


class AddLugarFragment : Fragment() {

private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!
private  lateinit var lugarViewModel: LugarViewModel

private lateinit var audioUtiles: AudioUtiles
private lateinit var imagenUtiles: ImagenUtiles


    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>



            @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        binding.btAgregar.setOnClickListener{
            binding.progressBar.visibility = ProgressBar.VISIBLE
            binding.msgMensaje.text = getString(R.string.msg_subiendo_audio)
            binding.msgMensaje.visibility = TextView.VISIBLE
            subeAudiNube()
        }


        audioUtiles = AudioUtiles(requireActivity(),requireContext(),binding.btAccion,binding.btPlay,binding.btDelete,
        getString(R.string.msg_graba_audio),
        getString(R.string.msg_detener_audio))

tomarFotoActivity = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()) {result ->
    if (result.resultCode == Activity.RESULT_OK){
        imagenUtiles.actualizaFoto()
    }
}

imagenUtiles =  ImagenUtiles(requireContext(),
binding.btPhoto,binding.btRotaL,binding.btRotaR,binding.imagen,tomarFotoActivity)


 ubicaGPS()

        return binding.root
    }

    private fun subeAudiNube() {
val audioFile = audioUtiles.audioFile
        if(audioFile.exists() && audioFile.isFile && audioFile.canRead()){
            val ruta = Uri.fromFile(audioFile)
            val rutaNube = "lugaresApp/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaAudio = it.toString()
                            subeImagenNube(rutaAudio)
                        }
                }
                .addOnFailureListener{(subeImagenNube(""))}
        }else{
            subeImagenNube("")

        }

    }

    private fun subeImagenNube(rutaAudio: String) {
        val imagenFile = imagenUtiles.imagenFile
        if(imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()){
            val ruta = Uri.fromFile(imagenFile)
            val rutaNube = "lugaresApp/${Firebase.auth.currentUser?.email}/imagenes/${imagenFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaImagen = it.toString()
                            addLugar(rutaAudio,rutaImagen)
                        }
                }
                .addOnFailureListener{ addLugar(rutaAudio,"")}
        }else{
            addLugar(rutaAudio,"")
        }

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

    private fun addLugar(rutaAudio: String, rutaImagen:String){
        var nombre = binding.etNombre.text.toString()
        if(nombre.isNotEmpty()){ //podemos insertar un lugar
            var correo = binding.etCorreo.text.toString()
            var telefono = binding.etTelefono.text.toString()
            var web = binding.etWeb.text.toString()
var latitud = binding.tvLatitud.text.toString().toDouble()
var longitud = binding.tvLongitud.text.toString().toDouble()
var altura = binding.tvAltura.text.toString().toDouble()



            var lugar = Lugar("",nombre,correo,telefono,web,latitud,longitud,altura,rutaAudio,rutaImagen)
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