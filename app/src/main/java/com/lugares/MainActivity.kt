package com.lugares


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares.databinding.ActivityMainBinding


//import com.lugares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private  const val  RC_SIGN_IN =9001

    }


    //CLENTE DE GOOGLE
    private lateinit var googleSignInClient : GoogleSignInClient

   private  lateinit var auth: FirebaseAuth
   private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        //Metodo para el login

        binding.btLogin.setOnClickListener {
            haceLogin();
        }


        binding.btRegister.setOnClickListener {
            haceRegistro();
        }

      val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_r))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        binding.btGoogle.setOnClickListener { googleSignIn() }

        // setContentView(R.layout.activity_main)
    }

    private fun googleSignIn() {
      val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private  fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    actualiza(user)
                }else{
                    actualiza(null)
                }

            }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val cuenta = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(cuenta.idToken!!)
            }catch (e: ApiException){

            }
        }
    }

    private fun haceRegistro() {
        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //se hace el registro

        auth.createUserWithEmailAndPassword(email,clave).
        addOnCompleteListener(this)  { task ->
            if(task.isSuccessful){
                Log.d("creando usuario", "Registrado")
                val user = auth.currentUser
                actualiza(user)

            }else {
               Log.d("creando usuario","fallo")
               Toast.makeText(baseContext,"fallo",Toast.LENGTH_LONG).show()
                actualiza(null)

            }
        }  }

    private fun actualiza(user: FirebaseUser?) {
        if (user != null){
            var intent = Intent(this, Principal::class.java)
            startActivity(intent)

        }
    }

    public override fun onStart(){
        super.onStart()
        val usuario= auth.currentUser
        actualiza(usuario)

    }


    private fun haceLogin() {
        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //se hace el login

        auth.signInWithEmailAndPassword(email,clave).
        addOnCompleteListener(this)  { task ->
            if(task.isSuccessful){
                Log.d("Autenticando usuario", "Autenticado")
                val user = auth.currentUser
                actualiza(user)

            }else {
                Log.d("Autenticando usuario","fallo")
                Toast.makeText(baseContext,"fallo",Toast.LENGTH_LONG).show()
                actualiza(null)

            }
        }
    }
}