package com.test.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
//    this variable stores the current selected color button
    private var selectedColorBtn : ImageButton? = null

//    this variable will launch the permission pop up
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach(){
                val permissionName= it.key
                val isGranted = it.value
                // TODO : ADD other permissions
//            if permission is granted, toast is displayed
                if(isGranted){
                    Toast.makeText(this,"Permission Read External Storage granted",
                        Toast.LENGTH_SHORT).show()

                    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                    gallery is opened with given intent
                    openGalleryLauncher.launch(pickIntent)

                }
//            if permission is not granted, toast is displayed
                else{
                    Toast.makeText(this,"Permission Read External Storage denied",
                        Toast.LENGTH_SHORT).show()
                }
            }

        }
//    this variable will open gallery and set selected image as background
    private val openGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == RESULT_OK && result.data != null){
            val imgBg: ImageView = findViewById(R.id.iv_background)
            imgBg.setImageURI(result.data?.data)
//            after setting image as background, canvas is cleared
            drawingView?.onClearAll()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)

        drawingView?.setBrushSize(10F)

//        setting the default selected color button
        val colorLL : LinearLayout = findViewById(R.id.ll_color_selector)
        selectedColorBtn = colorLL[1] as ImageButton
        selectedColorBtn!!.setImageResource(R.drawable.pallet_selected)

//        when brush selector is clicked selectBrushSizeDialog fxn is called
        val brushSelector : ImageButton = findViewById(R.id.ib_brush)
        brushSelector.setOnClickListener(){
            selectBrushSizeDialog()
        }
    }


//    this fxn will open the brush size selector dialog
//    and will change the brush size depending on the button clicked
    private fun selectBrushSizeDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush Size:")

        val xsmallbrush : ImageButton = brushDialog.findViewById(R.id.ib_xsmall_brush)
        xsmallbrush.setOnClickListener(){
            drawingView?.setBrushSize(2F)
            brushDialog.dismiss()
        }
        val smallbrush : ImageButton = brushDialog.findViewById(R.id.ib_small_brush)
        smallbrush.setOnClickListener(){
            drawingView?.setBrushSize(5F)
            brushDialog.dismiss()
        }
        val mediumbrush:ImageButton = brushDialog.findViewById(R.id.ib_medium_brush)
        mediumbrush.setOnClickListener(){
            drawingView?.setBrushSize(10F)
            brushDialog.dismiss()
        }
        val largebrush:ImageButton = brushDialog.findViewById(R.id.ib_large_brush)
        largebrush.setOnClickListener(){
            drawingView?.setBrushSize(20F)
            brushDialog.dismiss()
        }
        val xlargebrush:ImageButton = brushDialog.findViewById(R.id.ib_xlarge_brush)
        xlargebrush.setOnClickListener(){
            drawingView?.setBrushSize(30F)
            brushDialog.dismiss()
        }

        brushDialog.show()
    }

//    this fxn sets the selected color btn as brush color
    fun selectColor(view: View){
        if(view !== selectedColorBtn){
            val imgbtn = view as ImageButton
            val newcolor = imgbtn.tag.toString()
            drawingView?.setColor(newcolor)

//            setting old btn drawable to normal
            selectedColorBtn?.setImageResource(R.drawable.pallet_normal)

//            setting new btn drawable to selected
            selectedColorBtn = view
            selectedColorBtn!!.setImageResource(R.drawable.pallet_selected)
        }
    }

//    this fxn generates a random color and sets it as brush color
    fun randomColor(view: View){
        val rnd : Random = Random.Default
        val rndColor= Color.argb(255,rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256))
        val colorHex = String.format("#%06X", 0xFFFFFF and rndColor)
        if(view === selectedColorBtn){
            drawingView?.setColor(colorHex)
        }
        else{
          drawingView?.setColor(colorHex)

            //            setting old btn drawable to normal
            selectedColorBtn?.setImageResource(R.drawable.pallet_normal)

//        setting random color button to  selected
            selectedColorBtn = findViewById(R.id.random_color_btn)
            selectedColorBtn!!.setImageResource(R.drawable.pallet_selected)
        }


    }


    fun importFromGallery(view: View){
//        if a permission is already denied ,show alert dialog displaying why app needs permission
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            showRationaleDialog("Drawing App",
                "This app requires access to read your External Storage")
        }
//        if permission is not set up, open permission pop up
        else{
            requestPermission.launch(arrayOf
                (Manifest.permission.READ_EXTERNAL_STORAGE)
                // TODO : ADD other permissions
            )
        }

    }

//    this shows an alert dialog stating why app needs a certain permission
    private fun showRationaleDialog(title: String, message: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("Cancel"){dialog,_->dialog.dismiss()}
        builder.create().show()
    }


    fun undo(view: View) {
        drawingView?.onUndo()
    }

    fun redo(view: View) {
        drawingView?.onRedo()
    }

    fun clearAll(view: View){
        drawingView?.onClearAll()
    }

}