package com.example.objectdetection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    var REQ_CODE = 111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        submit.setOnClickListener {
            details.text =""
            var i: Intent = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(i, "Choose Image"), REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE){
            iv.setImageURI(data?.data)
            val image: FirebaseVisionImage
            try {
                image = FirebaseVisionImage.fromFilePath(applicationContext, data?.data!!)
                val labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()
                labeler.processImage(image)
                    .addOnSuccessListener { labels ->
                        // Task completed successfully
                        // ...

                        for (label in labels) {
                            val text = label.text
                            val entityId = label.entityId
                            val confidence = label.confidence

                            details.append("$text  $confidence \n")
                        }

                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                   
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }
}