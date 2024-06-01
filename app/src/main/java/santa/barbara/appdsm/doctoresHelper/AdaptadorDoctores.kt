package santa.barbara.appdsm.doctoresHelper

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import santa.barbara.appdsm.R

class AdaptadorDoctores(private var Datos: MutableList<tbDoctores>) :
    RecyclerView.Adapter<ViewHolderDoctores>() {

    fun eliminar(position: Int) {
        val referencia = FirebaseDatabase.getInstance().getReference("doctores")
        referencia.orderByChild("nombre")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        if (dataSnapshot.child("nombre").value == Datos[position].nombre) {
                            dataSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    println("yes")
                                }
                                .addOnFailureListener { e ->
                                    println("no")
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                  println("no pero on caceled")
                }
            })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDoctores {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_card_doctores, parent, false)
        return ViewHolderDoctores(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderDoctores, position: Int) {
        //Asigno el nombre del doctor al texto de la card
        val item = Datos[position]
        holder.lblDoctorCard.text = item.nombre
        holder.lblDoctorEspecialidad.text = item.especialidad
        holder.lblDoctorTelefono.text = item.telefono

        //Clic al icono de borrar
        holder.imgBorrarDoctor.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar")
            builder.setMessage("¿Quieres editar este elemento?")

            builder.setPositiveButton("Sí") { dialog, which ->
                eliminar(position)
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        //Clic al icono de editar
    }
}