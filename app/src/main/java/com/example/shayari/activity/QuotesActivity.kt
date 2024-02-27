package com.example.shayari.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.shayari.R
import com.example.shayari.databinding.ActivityQuotesBinding

class QuotesActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)

        binding = ActivityQuotesBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        initview()
    }

    private fun initview() {
        with(binding) {
//            DisplayActivity pass the data
            var shayari = intent.getStringExtra("shyari")
            txtShayari.setText(shayari)

//            camera & gallery click ivent
            imgCamera.setOnClickListener {
                val dialog = Dialog(this@QuotesActivity)
                dialog.setContentView(R.layout.camera_gallery_dialogbox)

                val linCamera = dialog.findViewById<LinearLayout>(R.id.linCamera)
                val linGallery = dialog.findViewById<LinearLayout>(R.id.linGallery)

                linCamera.setOnClickListener {
                    // Handle camera option here
                    // Launch camera intent
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                    dialog.dismiss()
                }

                linGallery.setOnClickListener {
                    // Handle gallery option here
                    // Launch gallery intent
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                    dialog.dismiss()
                }
                dialog.show()
            }

//            share Click Ivent
            imgShare.setOnClickListener {
                if (shayari != null) {
                    shareQuote(it.context, shayari)
                }
            }

//            Download Click Ivent
            imgDownload.setOnClickListener {
                // Capture the drawing cache of the specified views
                val backgroundBitmap = getBitmapFromView(binding.imgBackground)
                val textBitmap = getBitmapFromView(binding.txtShayari)

                // Combine the backgroundBitmap and textBitmap to create the final image
                val finalBitmap = combineBitmaps(backgroundBitmap, textBitmap)

                // Save the final image to the gallery
                MediaStore.Images.Media.insertImage(
                    contentResolver,
                    finalBitmap,
                    "image_title",
                    "image_description"
                )

                Toast.makeText(this@QuotesActivity, "Download Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //    camera & Gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.imgBackground.setImageBitmap(imageBitmap)
                }

                GALLERY_REQUEST_CODE -> {
                    val selectedImageUri = data?.data
                    selectedImageUri?.let {
                        binding.imgBackground.setImageURI(it)
                    }
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val GALLERY_REQUEST_CODE = 200
    }

    //    Quotes share
    fun shareQuote(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(shareIntent, "Share using"))
    }

//    Download Quotes
    private fun combineBitmaps(backgroundBitmap: Bitmap, textBitmap: Bitmap): Bitmap {
        val combinedBitmap = Bitmap.createBitmap(
            backgroundBitmap.width,
            backgroundBitmap.height,
            backgroundBitmap.config
        )

        val canvas = Canvas(combinedBitmap)
        canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)

        // Calculate the center position for the text
        val centerX = (backgroundBitmap.width - textBitmap.width) / 2.toFloat()
        val centerY = (backgroundBitmap.height - textBitmap.height) / 2.toFloat()

        // Draw the text on top of the background
        canvas.drawBitmap(textBitmap, centerX, centerY, null)

        return combinedBitmap
    }

//    Download Quotes
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}