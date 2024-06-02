package santa.barbara.appdsm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import santa.barbara.appdsm.doctoresHelper.tbDoctores

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [agregar_doctores.newInstance] factory method to
 * create an instance of this fragment.
 */
class agregar_doctores : Fragment() {
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
        val root = inflater.inflate(R.layout.fragment_agregar_doctores, container, false)

        val database = FirebaseDatabase.getInstance()
        val referencia = database.getReference("doctores")
        val imgAtras = root.findViewById<ImageView>(R.id.imgAtras)
        val txtNombre = root.findViewById<EditText>(R.id.txtNombreDoctor)
        val txtEspecialidad = root.findViewById<EditText>(R.id.txtEspecialidadDoctor)
        val txtTelefono = root.findViewById<EditText>(R.id.txtTelefonoDoctor)
        val btnGuardar = root.findViewById<Button>(R.id.btnCrearCita)

        imgAtras.setOnClickListener {
            findNavController().navigateUp()
        }

        btnGuardar.setOnClickListener {

            if (txtNombre.text.toString().isEmpty() || txtEspecialidad.text.toString()
                    .isEmpty() || txtTelefono.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val referencia = FirebaseDatabase.getInstance().getReference("doctores")
                val nuevoDoctorRef = referencia.push()
                val nuevoDoctorId = nuevoDoctorRef.key

                if (nuevoDoctorId != null) {
                    val nuevoDoctor = tbDoctores(
                        nuevoDoctorId,
                        txtNombre.text.toString(),
                        txtEspecialidad.text.toString(),
                        txtTelefono.text.toString()
                    )

                    nuevoDoctorRef.setValue(nuevoDoctor)
                        .addOnSuccessListener {
                            txtNombre.text.clear()
                            txtEspecialidad.text.clear()
                            txtTelefono.text.clear()
                            println("Doctor agregado exitosamente")
                            Toast.makeText(
                                requireContext(),
                                "Doctor agregado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            println("Error al agregar doctores: $e")
                        }
                }
            }
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
         * @return A new instance of fragment agregar_doctores.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            agregar_doctores().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}