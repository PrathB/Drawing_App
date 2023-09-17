package com.test.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)

        drawingView?.setBrushSize(10F)

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

        brushDialog.show()
    }
}