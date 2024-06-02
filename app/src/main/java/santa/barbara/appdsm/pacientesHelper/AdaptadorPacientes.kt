package santa.barbara.appdsm.pacientesHelper

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import santa.barbara.appdsm.R
import santa.barbara.appdsm.detalle_paciente

class AdaptadorPacientes(private var Datos: MutableList<tbPacientes>) :
    RecyclerView.Adapter<ViewHolderPacientes>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPacientes {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_card_pacientes, parent, false)
        return ViewHolderPacientes(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderPacientes, position: Int) {
        //Asigno los valores a los textView de la card
        val item = Datos[position]
        holder.lblPacienteNombre.text = item.nombre
        holder.lblPacienteEspecie.text = item.especie
        holder.lblRazaPaciente.text = item.raza
        holder.itemView.tag = item.id

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, detalle_paciente::class.java)
            intent.putExtra(
                "nombrePaciente",
                item.nombre
            )
            context.startActivity(intent)
        }
    }

}