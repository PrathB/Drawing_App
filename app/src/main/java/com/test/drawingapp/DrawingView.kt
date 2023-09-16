package com.test.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context :Context , attrs :AttributeSet) : View(context,attrs) {
    private var mdrawPath : CustomPath? = null
    private var mcanvasBitmap : Bitmap? = null
    private var mdrawPaint : Paint? = null
    private var mcanvasPaint : Paint? = null
    private var color :Int = Color.BLACK
    private var mbrushSize : Float = 0.toFloat()
    private var mcanvas : Canvas? = null

    init{
        setUpDrawing()
    }

    private fun setUpDrawing(){
        mdrawPath = CustomPath(color, mbrushSize)
//        setting up mdrawPaint color,style,strokeCap,strokeJoin
        mdrawPaint = Paint()
        mdrawPaint!!.color = color
        mdrawPaint!!.style = Paint.Style.STROKE
        mdrawPaint!!.strokeJoin = Paint.Join.ROUND
        mdrawPaint!!.strokeCap = Paint.Cap.ROUND
        mcanvasPaint = Paint(Paint.DITHER_FLAG)
//        initializing brush size with 20.0
        mbrushSize = 20.toFloat()
    }

//    OnSizeChanged fxn is called to setup canvas when screen is displayed
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        setting up canvas by creating a bitmap
        mcanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        mcanvas = Canvas(mcanvasBitmap!!)
    }

//    fxn to implement drawing using drawpath
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(mcanvasBitmap!!,0f,0f,mcanvasPaint)

        if(!mdrawPath!!.isEmpty) {
//            selecting custom color and stroke width
            mdrawPaint!!.strokeWidth = mdrawPath!!.brushThickness
            mdrawPaint!!.color = mdrawPath!!.color
            canvas?.drawPath(mdrawPath!!, mdrawPaint!!)
        }
    }

//    this fxn is used to register screen touch
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchx = event?.x
        val touchy = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                mdrawPath!!.color = color
                mdrawPath!!.brushThickness = mbrushSize

                mdrawPath!!.reset()
                if (touchx != null) {
                    if (touchy != null) {
                        mdrawPath!!.moveTo(touchx,touchy)
                    }
                }
            }

            MotionEvent.ACTION_MOVE ->{
                if (touchx != null) {
                    if (touchy != null) {
                        mdrawPath!!.lineTo(touchx,touchy)
                    }
                }
            }
            MotionEvent.ACTION_UP ->{
//                when finger is lifted , mdrawPath is updated (in case color or brush size is updated by user)
                mdrawPath = CustomPath(color,mbrushSize)
            }
            else -> {
                return false

            }
        }
        invalidate()
        return true
    }
//    this class allows to choose a custom color and brush thickness for brush path
    internal inner class CustomPath (var color : Int , var brushThickness : Float) : Path() {

    }
}