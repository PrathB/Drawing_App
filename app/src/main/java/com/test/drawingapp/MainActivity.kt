package com.test.drawingapp

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
//    this variable stores the current selected color button
    private var selectedColorBtn : ImageButton? = null
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



}