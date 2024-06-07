package tn.esprit.pdm.viewModel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import tn.esprit.pdm.R
import tn.esprit.pdm.network.RetrofitClient
import tn.esprit.pdm.network.UserApiService
import tn.esprit.pdm.network.model.LoginRequest
import tn.esprit.pdm.network.model.UserResponse

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: UserApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiService =
            RetrofitClient.getClient("http://10.0.2.2:7002/").create(UserApiService::class.java)

        var loginButton = findViewById<Button>(R.id.btnLogin)
        loginButton.setOnClickListener { performLogin() }
    }

    fun performLogin() {
        var emailView = findViewById<EditText>(R.id.tfEmail)
        var email = emailView.text.toString()
        var passwordView = findViewById<EditText>(R.id.tfPwd)
        var password = passwordView.text.toString()

        val loginRequest = LoginRequest(email, password)
        apiService.loginUser(loginRequest).enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: retrofit2.Call<UserResponse>, response: retrofit2.Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    emailView.setText("")
                    passwordView.setText("")
                    // Gérer la réponse réussie
                    Toast.makeText(applicationContext, "Connexion réussie!", Toast.LENGTH_SHORT).show()
                    val intentToHomeActivity = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intentToHomeActivity)
                } else {
                    // Gérer l'échec de la requête
                    Toast.makeText(applicationContext, "Erreur de connexion: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<UserResponse>, t: Throwable) {
                // Gérer l'échec de la communication
                Toast.makeText(applicationContext, "Erreur réseau: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}