package santa.barbara.appdsm.doctoresHelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import santa.barbara.appdsm.R

class AdaptadorDoctores(private var Datos: List<tbDoctores>) : RecyclerView.Adapter<ViewHolderDoctores>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDoctores {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_doctores, parent, false)
        return ViewHolderDoctores(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderDoctores, position: Int) {
        val item = Datos[position]
        holder.lblDoctorCard.text = item.nombre
    }
}