package santa.barbara.appdsm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import santa.barbara.appdsm.citasHelper.AdaptadorCitas
import santa.barbara.appdsm.citasHelper.tbCitas

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Inicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Inicio : Fragment() {
    val datos = mutableListOf<tbCitas>()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_inicio, container, false)

        val btnIrAPacientes = root.findViewById<ImageView>(R.id.btnIrAPacientes)
        val btnIraAgregarPaciente = root.findViewById<ImageView>(R.id.btnIraAgregarPaciente)
        val rcvProximasCitas = root.findViewById<RecyclerView>(R.id.rcvProximasCitas)
        rcvProximasCitas.layoutManager = LinearLayoutManager(requireContext())

        btnIrAPacientes.setOnClickListener {
            findNavController().navigate(R.id.action_inicio_to_pacientes)
        }
        //Al dar clic voy a la pantalla de agregar paciente
        btnIraAgregarPaciente.setOnClickListener {
            findNavController().navigate(R.id.action_inicio_to_add_pacientes)
        }

        val referencia = FirebaseDatabase.getInstance().getReference("citas")

        /////Mostrar datos de Doctores/////
        fun obtenerDatos(): List<tbCitas> {
            referencia.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    datos.clear()
                    for (dataSnapshot in snapshot.children) {
                        val id = snapshot.key
                        val cita = snapshot.getValue(tbCitas::class.java)
                        val myid = dataSnapshot.child("id").value
                        val fecha = dataSnapshot.child("fecha").value
                        val hora = dataSnapshot.child("hora").value
                        val paciente = dataSnapshot.child("paciente").value
                        val doctor = dataSnapshot.child("doctor").value
                        val motivo = dataSnapshot.child("motivo").value
                        if (cita != null && id != null) {
                            val citanueva = tbCitas(
                                myid.toString(),
                                fecha.toString(),
                                hora.toString(),
                                paciente.toString(),
                                doctor.toString(),
                                motivo.toString()
                            )
                            datos.add(citanueva)
                        }
                    }
                    val adapter = AdaptadorCitas(datos)
                    rcvProximasCitas.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error al mostrar citas: $error")
                }
            })
            return datos
        }
        obtenerDatos()


        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Inicio.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Inicio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}