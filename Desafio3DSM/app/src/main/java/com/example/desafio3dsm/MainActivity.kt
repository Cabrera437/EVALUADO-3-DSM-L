package com.example.desafio3dsm

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService
    private lateinit var cursoAdapter: CursoAdapter
    private lateinit var cardDetalles: View
    private lateinit var textTitulo: TextView
    private lateinit var textCategoria: TextView
    private lateinit var textResumen: TextView
    private lateinit var textReferencia: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa el card de detalles y sus TextViews
        cardDetalles = findViewById(R.id.card_detalles)
        textTitulo = findViewById(R.id.text_titulo)
        textCategoria = findViewById(R.id.text_categoria)
        textResumen = findViewById(R.id.text_resumen)
        textReferencia = findViewById(R.id.text_referencia)

        cardDetalles.visibility = View.GONE


        val fabAgregar: FloatingActionButton = findViewById(R.id.fab_agregar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://67061ac1031fd46a8311f3da.mockapi.io/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        cargarDatos()

        // Acción para agregar un nuevo curso
        fabAgregar.setOnClickListener {
            val intent = Intent(this, CrearCursoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatos() // Cargar datos al reanudar la actividad
        cardDetalles.visibility = View.GONE // Ocultar el CardView de detalles al reiniciar la actividad

    }

    private fun cargarDatos() {
        apiService.obtenerCursos().enqueue(object : Callback<List<Curso>> {
            override fun onResponse(call: Call<List<Curso>>, response: Response<List<Curso>>) {
                if (response.isSuccessful) {
                    val cursos = response.body()
                    if (cursos != null) {
                        cursoAdapter = CursoAdapter(cursos)
                        recyclerView.adapter = cursoAdapter

                        // Establecemos el escuchador de clics en el adaptador
                        cursoAdapter.setOnItemClickListener(object : CursoAdapter.OnItemClickListener {

                            override fun onItemClick(curso: Curso) {
                                val opciones = arrayOf("Ver Detalles", "Actualizar Curso", "Eliminar Curso") // Opciones de menú

                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle(curso.Titulo)
                                    .setItems(opciones) { dialog, index ->
                                        when (index) {
                                            0 -> verDetalles(curso)
                                            1 -> {
                                                // Inicia la actividad de actualización
                                                val intent = Intent(this@MainActivity, ActualizarCursoActivity::class.java)
                                                intent.putExtra("curso_id", curso.id) // Enviar el ID del curso a actualizar
                                                startActivity(intent)
                                            }
                                            2 -> eliminarCurso(curso)
                                        }
                                    }
                                    .setNegativeButton("Cancelar", null)
                                    .show()

                            }
                        })
                    }
                } else {
                    Log.e("API", "Error al obtener los cursos: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Error al obtener los cursos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Curso>>, t: Throwable) {
                Log.e("API", "Error al obtener los cursos: ${t.message}")
                Toast.makeText(this@MainActivity, "Error al obtener los cursos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun verDetalles(curso: Curso) {
        // Mostrar los detalles del curso en el CardView
        textTitulo.text = curso.Titulo
        textCategoria.text = "Categoría: ${curso.Categoria}"
        textResumen.text = "Resumen: ${curso.Resumen}"
        textReferencia.text = "Referencia: ${curso.Referencia}"

        // Hacer visible el CardView
        cardDetalles.visibility = View.VISIBLE

        cardDetalles.setOnClickListener {
            // Iniciar ActualizarCursoActivity
            val intent = Intent(this, ActualizarCursoActivity::class.java).apply {
                putExtra("curso_id", curso.id) // Pasa el ID del curso
            }
            startActivity(intent)
        }
    }



    private fun eliminarCurso(curso: Curso) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Curso")
            .setMessage("¿Estás seguro de que deseas eliminar el curso '${curso.Titulo}'?")
            .setPositiveButton("Eliminar") { dialog, which ->
                apiService.eliminarCurso(curso.id).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Curso eliminado exitosamente", Toast.LENGTH_SHORT).show()
                            cargarDatos() // Recargar datos después de eliminar
                        } else {
                            Toast.makeText(this@MainActivity, "Error al eliminar el curso", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error al eliminar el curso", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}
