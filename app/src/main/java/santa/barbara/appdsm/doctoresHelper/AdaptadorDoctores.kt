package santa.barbara.appdsm.doctoresHelper

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
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
                                    println("Registro eliminado")
                                }
                                .addOnFailureListener { e ->
                                    println("Error al eliminar: $e")
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                  println("Error: $error")
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
        //Asigno los valores a los textView de la card
        val item = Datos[position]
        holder.lblDoctorCard.text = item.nombre
        holder.lblDoctorEspecialidad.text = item.especialidad
        holder.lblDoctorTelefono.text = item.telefono
        val currentDoctor = Datos[position]

        // Asignar el ID único del doctor al ítem en el RecyclerView
        holder.itemView.tag = currentDoctor.id

        //Clic al icono de borrar
        holder.imgBorrarDoctor.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Quieres eliminar este elemento?")

            builder.setPositiveButton("Sí") { dialog, which ->
                eliminar(position)
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        // Clic al icono de editar
        holder.imgEditarDoctor.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar Doctor")

            val txtNuevoNombre = EditText(context).apply {
                setText(currentDoctor.nombre)
            }
            val txtNuevaEspecialidad = EditText(context).apply {
                setText(currentDoctor.especialidad)
            }
            val txtNuevoTelefono = EditText(context).apply {
                setText(currentDoctor.telefono)
            }

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNombre)
                addView(txtNuevaEspecialidad)
                addView(txtNuevoTelefono)
            }
            builder.setView(layout)

            builder.setPositiveButton("Guardar") { dialog, which ->
                val nuevoNombre = txtNuevoNombre.text.toString()
                val nuevaEspecialidad = txtNuevaEspecialidad.text.toString()
                val nuevoTelefono = txtNuevoTelefono.text.toString()

                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("doctores").child(currentDoctor.id)

                // Actualizo los datos en Firebase
                reference.child("nombre").setValue(nuevoNombre)
                reference.child("especialidad").setValue(nuevaEspecialidad)
                reference.child("telefono").setValue(nuevoTelefono)
                    .addOnSuccessListener {
                        println("Datos actualizados")

                        currentDoctor.nombre = nuevoNombre
                        currentDoctor.especialidad = nuevaEspecialidad
                        currentDoctor.telefono = nuevoTelefono
                        notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        println("Error al actualizar doctores: $e")
                    }
            }
            builder.setNegativeButton("Cancelar", null)

            val dialog = builder.create()
            dialog.show()
        }
    }


    }

