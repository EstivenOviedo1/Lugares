package com.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lugares.databinding.LugarFilaBinding
import com.lugares.model.Lugar
import com.lugares.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>(){

    //Una lista para almacenar la informacion de los lugares
    private var listaLugar = emptyList<Lugar>()


   inner class LugarViewHolder(private val itemBinding: LugarFilaBinding):
       RecyclerView.ViewHolder(itemBinding.root){

  fun  bind(lugar: Lugar){
      itemBinding.tvTelefono.text = lugar.telefono
      itemBinding.tvCorreo.text = lugar.correo
      itemBinding.tvNombre.text = lugar.nombre

Glide.with(itemBinding.root.context)
    .load(lugar.rutaImagen)
    .circleCrop()
    .into(itemBinding.image)


itemBinding.vistaFila.setOnClickListener{
    val action = LugarFragmentDirections.actionNavLugarToUpdateLugarFragment(lugar)
    itemView.findNavController().navigate(action)

}
  }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  LugarViewHolder(itemBinding)


    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {

        val lugarActual = listaLugar[position]
        holder.bind(lugarActual)
    }

    override fun getItemCount(): Int {
        return listaLugar.size
    }

    fun setData(lugares: List<Lugar>){
        this.listaLugar = lugares
notifyDataSetChanged() //Provoca q se redibuje la lista
    }
}