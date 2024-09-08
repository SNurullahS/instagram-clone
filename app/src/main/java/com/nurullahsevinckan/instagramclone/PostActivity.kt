package com.nurullahsevinckan.instagramclone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.nurullahsevinckan.instagramclone.databinding.ActivityMainBinding
import com.nurullahsevinckan.instagramclone.databinding.ActivityPostBinding
import java.util.UUID

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var activityResultLauncger : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture : Uri? = null

    //For firebase futures
    private lateinit var userAuth : FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        registerLauncher()

        //Implementation Of Firebase Components
        userAuth = Firebase.auth
        fireStore = Firebase.firestore
        storage = Firebase.storage
    }
    fun publishButtonClick(view: View){
        //Give every image unique name
        //We use uuid, if we don t use this, every time we save image with same name
        //Its cause override same name pictures
        val uuid = UUID.randomUUID()
        val randomImageName = "$uuid.jpg"

        val reference = storage.reference // Give use the storage main space
        val imageReferance = reference.child("images").child(randomImageName)

        selectedPicture?.let { selectedPicture ->
            imageReferance.putFile(selectedPicture).addOnSuccessListener { uploadTask ->
                //download url to firestore
            }.addOnFailureListener{exception ->
                //error message
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }


    }

    fun selectImageClick(view: View){
    if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
            Snackbar.make(view,"Permission Needed For Gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }.show()
        }else{
            // request permission
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }

    }else{
        //Intent gallery
        val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // Start activity for result
        activityResultLauncger.launch(intentToGallery)

    }
    }
    private fun registerLauncher(){
        activityResultLauncger = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                var activityResultUri = result.data
                if(activityResultUri != null) {
                    selectedPicture = activityResultUri.data
                    selectedPicture?.let { selectedPicture->
                        //We wont convert this image data to bitmap cause Firebase can store image Uri format
                        binding.imageButton.setImageURI(selectedPicture.normalizeScheme())
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->

            if(result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncger.launch(intentToGallery)
            }else{
                //permission denied
                Toast.makeText(this@PostActivity,"Permission needed",Toast.LENGTH_LONG).show()
            }

        }
        
    }
}
