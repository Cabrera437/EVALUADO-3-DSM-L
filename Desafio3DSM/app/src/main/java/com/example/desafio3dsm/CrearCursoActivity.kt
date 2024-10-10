package com.example.desafio3dsm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent


class CrearCursoActivity : AppCompatActivity() {

    private lateinit var tituloEditText: EditText
    private lateinit var categoriaEditText: EditText
    private lateinit var resumenEditText: EditText
    private lateinit var referenciaEditText: EditText
    private lateinit var crearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_curso)

        tituloEditText = findViewById(R.id.editTextTitulo)
        categoriaEditText = findViewById(R.id.editTextCategoria)
        resumenEditText = findViewById(R.id.editTextResumen)
        referenciaEditText = findViewById(R.id.editTextReferencia)
        crearButton = findViewById(R.id.btnGuardar)

        crearButton.setOnClickListener {
            val titulo = tituloEditText.text.toString()
            val categoria = categoriaEditText.text.toString()
            val resumen = resumenEditText.text.toString()
            val referencia = referenciaEditText.text.toString()

            val nuevoCurso = Curso(titulo, categoria, resumen, referencia, "")

            // instancia del servicio API
            val api = ApiService.create()

            api.crearCurso(nuevoCurso).enqueue(object : Callback<Curso> {
                override fun onResponse(call: Call<Curso>, response: Response<Curso>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CrearCursoActivity,
                            "Curso creado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Inicia la actividad principal después de crear el curso
                        val intent = Intent(this@CrearCursoActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error al crear curso: $error")
                        Toast.makeText(
                            this@CrearCursoActivity,
                            "Error al crear el curso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Curso>, t: Throwable) {
                    Toast.makeText(
                        this@CrearCursoActivity,
                        "Error al crear el curso",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
