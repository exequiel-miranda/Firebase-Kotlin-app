package santa.barbara.appdsm

import android.app.DatePickerDialog
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
import com.google.firebase.database.FirebaseDatabase
import santa.barbara.appdsm.doctoresHelper.tbDoctores
import santa.barbara.appdsm.pacientesHelper.tbPacientes
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [agregar_pacientes.newInstance] factory method to
 * create an instance of this fragment.
 */
class agregar_pacientes : Fragment() {
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
        val root = inflater.inflate(R.layout.fragment_agregar_pacientes, container, false)

        val txtNombrePaciente = root.findViewById<EditText>(R.id.txtNombrePaciente)
        val txtEspeciePaciente = root.findViewById<EditText>(R.id.txtEspeciePaciente)
        val txtRazaPaciente = root.findViewById<EditText>(R.id.txtRazaPaciente)
        val txtFechaNacimientoPaciente =
            root.findViewById<EditText>(R.id.txtFechaNacimientoPaciente)
        val txtHistorialMedicoPaciente = root.findViewById<EditText>(R.id.txtHistorialMedicoPaciente)
        val imgAtrasPacientes = root.findViewById<ImageView>(R.id.imgAtrasPacientes)
        val btnCrearPaciente = root.findViewById<Button>(R.id.btnCrearPaciente)

        imgAtrasPacientes.setOnClickListener {
            findNavController().navigateUp()
        }

        //Mostrar el calendario al hacer click en el EditText txtFechaNacimientoPaciente
        txtFechaNacimientoPaciente.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFechaNacimientoPaciente.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

        btnCrearPaciente.setOnClickListener {
            if (txtNombrePaciente.text.toString().isEmpty() ||
                txtEspeciePaciente.text.toString().isEmpty() ||
                txtRazaPaciente.text.toString().isEmpty() ||
                txtFechaNacimientoPaciente.text.toString().isEmpty() ||
                txtHistorialMedicoPaciente.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val referencia = FirebaseDatabase.getInstance().getReference("pacientes")
                val nuevoPacienteRef = referencia.push()
                val nuevoPacienteId = nuevoPacienteRef.key

                if (nuevoPacienteId != null) {
                    val nuevoDoctor = tbPacientes(
                        nuevoPacienteId,
                        txtNombrePaciente.text.toString(),
                        txtEspeciePaciente.text.toString(),
                        txtRazaPaciente.text.toString(),
                        txtFechaNacimientoPaciente.text.toString(),
                        txtHistorialMedicoPaciente.text.toString()
                    )

                    nuevoPacienteRef.setValue(nuevoDoctor)
                        .addOnSuccessListener {
                            txtNombrePaciente.text.clear()
                            txtEspeciePaciente.text.clear()
                            txtRazaPaciente.text.clear()
                            txtFechaNacimientoPaciente.text.clear()
                            txtHistorialMedicoPaciente.text.clear()
                            Toast.makeText(
                                requireContext(),
                                "Paciente agregado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            println("Error al agregar pacientes: $e")
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
         * @return A new instance of fragment agregar_pacientes.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            agregar_pacientes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}