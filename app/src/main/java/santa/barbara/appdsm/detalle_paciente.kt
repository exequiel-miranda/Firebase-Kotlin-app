package santa.barbara.appdsm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class detalle_paciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_paciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgAtrasDetallePaciente = findViewById<ImageView>(R.id.imgAtrasDetallePaciente)
        val lblNombreMascota = findViewById<TextView>(R.id.lblNombreMascota)
        val lblEspecie = findViewById<TextView>(R.id.lblEspecie)
        val lblRaza = findViewById<TextView>(R.id.lblRaza)
        val lblPeso = findViewById<TextView>(R.id.lblPeso)
        val lblTamanio = findViewById<TextView>(R.id.lblAltura)
        val lblEdad = findViewById<TextView>(R.id.lblEdad)
        val lblSexo = findViewById<TextView>(R.id.lblGenero)
        val lblHistorialMedico = findViewById<TextView>(R.id.lblHistorialMedico)
        val btnBorrarPaciente = findViewById<Button>(R.id.btnBorrarPaciente)
        val btnEditarPaciente = findViewById<Button>(R.id.btnEditarPaciente)


        val idRecibido = intent.getStringExtra("id")!!
        val nombreMascotaRecibido = intent.getStringExtra("nombreMascota")
        val nombreDuenioRecibido = intent.getStringExtra("nombreDuenio")
        val especieRecibida = intent.getStringExtra("especie")
        val razaRecibida = intent.getStringExtra("raza")
        val pesoRecibido = intent.getStringExtra("peso")
        val tamanioRecibido = intent.getStringExtra("tamanio")
        val sexoRecibido = intent.getStringExtra("sexo")
        val fechaNacimientoRecibido = intent.getStringExtra("fechaNacimiento")
        val historialMedicoRecibido = intent.getStringExtra("historialMedico")


        lblNombreMascota.text = nombreMascotaRecibido
        lblEspecie.text = especieRecibida
        lblRaza.text = razaRecibida
        lblPeso.text = pesoRecibido
        lblTamanio.text = tamanioRecibido
        lblSexo.text = sexoRecibido
        lblEdad.text = fechaNacimientoRecibido
        lblHistorialMedico.text = historialMedicoRecibido

        btnBorrarPaciente.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Quieres eliminar este paciente?")

            builder.setPositiveButton("Sí") { dialog, which ->
            Toast.makeText(this, "Paciente eliminado", Toast.LENGTH_SHORT).show()
                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("pacientes").child(idRecibido)
                reference.removeValue()
                finish()
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        imgAtrasDetallePaciente.setOnClickListener {
            finish()
        }

    }
}