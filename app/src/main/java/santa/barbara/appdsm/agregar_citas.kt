package santa.barbara.appdsm

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import santa.barbara.appdsm.citasHelper.tbCitas
import santa.barbara.appdsm.doctoresHelper.tbDoctores
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [agregar_citas.newInstance] factory method to
 * create an instance of this fragment.
 */
class agregar_citas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    fun showTimePickerDialog(textView: EditText) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val formattedTime = format.format(cal.time)
                textView.setText(formattedTime)
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

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
        val root = inflater.inflate(R.layout.fragment_agregar_citas, container, false)

        val imgAtras = root.findViewById<ImageView>(R.id.imgAtrasCitas)
        val txtfechaCita = root.findViewById<EditText>(R.id.txtFechaCita)
        val txtHoraCita = root.findViewById<EditText>(R.id.txtHoraCita)
        val txtPacienteCita = root.findViewById<EditText>(R.id.txtPacienteCita)
        val txtDoctorCita = root.findViewById<EditText>(R.id.txtDoctorCita)
        val txtMotivoCita = root.findViewById<EditText>(R.id.txtMotivoCita)
        val btnCrearCita = root.findViewById<Button>(R.id.btnCrearCita)


        //Mostrar el calendario al hacer click en el EditText txtFechaNacimientoPaciente
        txtfechaCita.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtfechaCita.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

            txtHoraCita.setOnClickListener {
                showTimePickerDialog(txtHoraCita)
            }



        btnCrearCita.setOnClickListener {

            if (txtfechaCita.text.toString().isEmpty() || txtHoraCita.text.toString()
                    .isEmpty() || txtPacienteCita.text.toString()
                    .isEmpty() || txtDoctorCita.text.toString()
                    .isEmpty() || txtMotivoCita.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val referencia = FirebaseDatabase.getInstance().getReference("citas")
                val nuevaCitaRef = referencia.push()
                val nuevaCitaId = nuevaCitaRef.key

                if (nuevaCitaId != null) {
                    val nuevaCita = tbCitas(
                        nuevaCitaId,
                        txtfechaCita.text.toString(),
                        txtHoraCita.text.toString(),
                        txtPacienteCita.text.toString(),
                        txtDoctorCita.text.toString(),
                        txtMotivoCita.text.toString()
                    )
                    nuevaCitaRef.setValue(nuevaCita).addOnSuccessListener {
                            txtfechaCita.text.clear()
                            txtHoraCita.text.clear()
                            txtPacienteCita.text.clear()
                            txtDoctorCita.text.clear()
                            txtMotivoCita.text.clear()
                            Toast.makeText(
                                requireContext(),
                                "Cita agendada exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            println("Error al agregar citas: $e")
                        }
                }
            }
        }
            imgAtras.setOnClickListener {
                findNavController().navigateUp()
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
             * @return A new instance of fragment agregar_citas.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                agregar_citas().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }
    }