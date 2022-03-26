package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var mDescriptionEditText: EditText
    private lateinit var mTakePhotoButton: Button
    private lateinit var mImageView: ImageView
    private lateinit var mPostButton: Button
    private lateinit var mLogoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDescriptionEditText = findViewById(R.id.etDescription)
        mTakePhotoButton = findViewById(R.id.btTakePhoto)
        mImageView = findViewById(R.id.ivImage)
        mPostButton = findViewById(R.id.btSubmit)
        mLogoutButton = findViewById(R.id.btLogout)

        mLogoutButton.setOnClickListener {
            ParseUser.logOut()
            if (ParseUser.getCurrentUser() == null) {
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }

        mPostButton.setOnClickListener{
            val description = mDescriptionEditText.text.toString()
            val user = ParseUser.getCurrentUser()
            photoFile?.let { submitPost(description, user, it) }
        }

        mTakePhotoButton.setOnClickListener {
            onLaunchCamera()
        }
        queryPosts()
    }

    private fun submitPost(description:String, user:ParseUser, photoFile:File) {
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(photoFile))
        post.saveInBackground { e->
            if (e!=null) {
                Log.e(TAG, "submitPost: ",e )
                e.printStackTrace()
                Toast.makeText(this,"Submit post failed",Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "submitPost: submit successfully")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                mImageView.setImageBitmap(takenImage)
            }
        } else {
            Toast.makeText(this,"Picture wasn't taken!",Toast.LENGTH_SHORT).show()
        }
    }

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1023
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    private fun onLaunchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFileUri(photoFileName)
        if (photoFile!=null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this,"com.codepath.fileprovider",photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)

            if (intent.resolveActivity(packageManager)!=null) {
                startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }


    private fun getPhotoFileUri(filename:String):File? {
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),TAG)
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdir()) {
            Log.d(TAG, "getPhotoFileUri: failed to creatr directory")
        }

        return File(mediaStorageDir.path+File.separator+filename)
    }

    private fun queryPosts() {
        val query:ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.findInBackground { posts, e ->
            if (e != null) {
                e.printStackTrace()
            } else {
                if (posts != null) {
                    for (p in posts) {
                        Log.i(TAG, "done: " + p.getDerscription()+" usernamre="+p.getUser()?.username)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "mainActivity"
    }
}