package com.nurullahsevinckan.instagramclone.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nurullahsevinckan.instagramclone.R
import com.nurullahsevinckan.instagramclone.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth //Need for authentication
    private lateinit var userMail : String
    private lateinit var userPassword :String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userMail = ""
        userPassword = ""
        // Initialize Firebase Auth
        auth = Firebase.auth

        //If current user is exist, directly go to main menu of app
        val currentUser =auth.currentUser
        if (currentUser != null){
            val intent = Intent(this@FeedActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
    fun signInButtonClick(view: View){

        userMail = binding.mailText.text.toString()
        userPassword = binding.passwordText.text.toString()
        if(userMail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this,"Enter Information Correctly",Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(userMail, userPassword).addOnSuccessListener{task ->
                //success
                val intent = Intent(this@FeedActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{task ->
                //fail
                Toast.makeText(this,task.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }
    }
    fun signUpButtonClick(view: View){

        userMail = binding.mailText.text.toString()
        userPassword = binding.passwordText.text.toString()
        if(userMail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this,"Enter Information Correctly",Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(userMail, userPassword).addOnSuccessListener{task ->
                //success
                val intent = Intent(this@FeedActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{task ->
                //fail
                Toast.makeText(this,task.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }

}