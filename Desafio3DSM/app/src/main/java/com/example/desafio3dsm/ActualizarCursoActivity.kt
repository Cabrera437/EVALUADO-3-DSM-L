package com.example.desafio3dsm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

    class ActualizarCursoActivity : AppCompatActivity() {

        private lateinit var tituloeEditText: EditText
        private lateinit var categoriaEditText: EditText
        private lateinit var resumenEditText: EditText
        private lateinit var referenciaEditText: EditText
        private lateinit var actualizarButton: Button
        private lateinit var apiService: ApiService

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_actualizar_curso)

            // Inicializa los EditText y el botón
            tituloeEditText = findViewById(R.id.tituloeEditText)
            categoriaEditText = findViewById(R.id.categoriaEditText)
            resumenEditText = findViewById(R.id.resumenEditText)
            referenciaEditText = findViewById(R.id.referenciaEditText)
            actualizarButton = findViewById(R.id.actualizarButton)

            // Configuracion de ApiService
            apiService = ApiService.create()

            // Cargar los datos del curso a actualizar
            cargarCurso()

            // Configuracion botón de actualizar
            actualizarButton.setOnClickListener {
                actualizarCurso()
            }
        }

        private fun cargarCurso() {
            // Obtener el ID del curso desde el Intent
            val cursoId = intent.getStringExtra("curso_id") // Esto debería ser un String

            // Verificar que el cursoId no sea null
            if (cursoId != null) {
                // Llamar a la API para obtener los datos del curso
                apiService.obtenerCursoPorId(cursoId).enqueue(object : Callback<Curso> {
                    override fun onResponse(call: Call<Curso>, response: Response<Curso>) {
                        if (response.isSuccessful) {
                            val curso = response.body()
                            // Verificar que el curso no sea null antes de acceder a sus propiedades
                            if (curso != null) {
                                // Rellenar los EditText con los datos del curso
                                tituloeEditText.setText(curso.Titulo)
                                categoriaEditText.setText(curso.Categoria)
                                resumenEditText.setText(curso.Resumen)
                                referenciaEditText.setText(curso.Referencia)
                            } else {
                                Log.e("API", "Respuesta vacía, curso no encontrado")
                                Toast.makeText(this@ActualizarCursoActivity, "Curso no encontrado", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("API", "Error al cargar curso: ${response.errorBody()?.string()}")
                            Toast.makeText(this@ActualizarCursoActivity, "Error al cargar curso", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Curso>, t: Throwable) {
                        Log.e("API", "Error al cargar curso: ${t.message}")
                        Toast.makeText(this@ActualizarCursoActivity, "Error al cargar curso", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Log.e("API", "ID de curso inválido")
                Toast.makeText(this, "ID de curso inválido", Toast.LENGTH_SHORT).show()
            }
        }


        private fun actualizarCurso() {
            val titulo = tituloeEditText.text.toString()
            val categoria = categoriaEditText.text.toString()
            val resumen = resumenEditText.text.toString()
            val referencia = referenciaEditText.text.toString()

            // Obtener el ID del curso desde el Intent como String
            val cursoId = intent.getStringExtra("curso_id")

            if (cursoId != null) {
                val cursoActualizado = Curso(titulo, categoria, resumen, referencia, cursoId)

                apiService.actualizarCurso(cursoId, cursoActualizado)
                    .enqueue(object : Callback<Curso> {
                        override fun onResponse(call: Call<Curso>, response: Response<Curso>) {
                            if (response.isSuccessful) {

                                Toast.makeText(
                                    this@ActualizarCursoActivity,
                                    "Curso actualizado exitosamente", // Mensaje de confirmación
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent()
                                intent.putExtra("Titulo", titulo)
                                intent.putExtra("Categoria", categoria)
                                intent.putExtra("Resumen", resumen)
                                intent.putExtra("Referencia", referencia)
                                setResult(RESULT_OK, intent)
                                finish()

                            } else {
                                Log.e(
                                    "API",
                                    "Error al actualizar curso: ${response.errorBody()?.string()}"
                                )
                                Toast.makeText(
                                    this@ActualizarCursoActivity,
                                    "Error al actualizar curso",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Curso>, t: Throwable) {
                            Log.e("API", "Error al actualizar curso: ${t.message}")
                            Toast.makeText(
                                this@ActualizarCursoActivity,
                                "Error al actualizar curso",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            } else {
                Log.e("API", "ID de curso inválido")
                Toast.makeText(this, "ID de curso inválido", Toast.LENGTH_SHORT).show()
            }
        }

    }


