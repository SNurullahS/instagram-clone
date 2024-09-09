package com.nurullahsevinckan.instagramclone.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nurullahsevinckan.instagramclone.R
import com.nurullahsevinckan.instagramclone.adapter.MainRecyclerAdapter
import com.nurullahsevinckan.instagramclone.databinding.ActivityMainBinding
import com.nurullahsevinckan.instagramclone.model.Post

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAuth :FirebaseAuth
    private lateinit var db :FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var mainAdapter: MainRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userAuth = Firebase.auth
        db = Firebase.firestore
        postArrayList = ArrayList<Post>()
        getData()

        //Connect recycler_row with our xml ui
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainAdapter = MainRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = mainAdapter

    }

    private fun getData(){
        // orderBy("date",Query.Direction.DESCENDING) -> shorted post by posted date (new to old)
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value != null && !value.isEmpty){
                    val documents = value.documents

                    postArrayList.clear() // Avoid duplication of posts

                    for(document in documents){

                        //casting
                        val userMail = document.get("userEmail") as String
                        val postDescription = document.get("comment") as String
                        val imageUrl = document.get("downloadedUrl") as String

                        val post = Post(userMail,postDescription,imageUrl)
                        postArrayList.add(post)
                    }

                    mainAdapter.notifyDataSetChanged() // When something chane it ll notify dataset
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInfalater = menuInflater
        menuInfalater.inflate(R.menu.insta_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_post){
            //Add post
            var intent = Intent(this@MainActivity, PostActivity::class.java)
            startActivity(intent)

        }else if(item.itemId == R.id.logout){
            // logout
            userAuth.signOut()
            val intent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}