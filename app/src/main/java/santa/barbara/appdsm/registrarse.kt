package santa.barbara.appdsm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            val correo = txtCorreo.text.toString()
            val password = txtPassword.text.toString()
            if (txtCorreo.text.isEmpty() || txtPassword.text.isEmpty()) {
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Usuario registrado", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Error al registrar", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}