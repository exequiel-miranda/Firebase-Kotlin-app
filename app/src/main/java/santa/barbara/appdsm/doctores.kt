package santa.barbara.appdsm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import santa.barbara.appdsm.doctoresHelper.AdaptadorDoctores
import santa.barbara.appdsm.doctoresHelper.tbDoctores

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [doctores.newInstance] factory method to
 * create an instance of this fragment.
 */
class doctores : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val datos = mutableListOf<tbDoctores>()

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

        val root = inflater.inflate(R.layout.fragment_doctores, container, false)

        val btnAgregarDoctor = root.findViewById<FloatingActionButton>(R.id.btnAgregarDoctor)
        val rcvDoctores = root.findViewById<RecyclerView>(R.id.rcvDoctores)
        rcvDoctores.layoutManager = LinearLayoutManager(requireContext())

        //Preparación de la base de datos
        var key: String? = null
        val database = FirebaseDatabase.getInstance()
        val referencia = database.getReference("doctores")

        /////TODO: funcion de obtenerDatos
        fun obtenerDatos(): List<tbDoctores>{
            referencia.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    datos.clear()
                    for(dataSnapshot in snapshot.children){
                        key = dataSnapshot.key
                        val nombre = dataSnapshot.child("nombre").value
                        val especialidad = dataSnapshot.child("especialidad").value
                        val telefono = dataSnapshot.child("telefono").value
                        val doctorNuevo = tbDoctores(nombre.toString(), especialidad.toString(), telefono.toString())
                        datos.add(doctorNuevo)
                    }
                    val adapter = AdaptadorDoctores(datos)
                    rcvDoctores.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error: $error")
                }

            })
            return datos
        }

        obtenerDatos()
        btnAgregarDoctor.setOnClickListener {
            findNavController().navigate(R.id.action_doctores_to_add_doctor)
        }


        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment doctores.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            doctores().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}