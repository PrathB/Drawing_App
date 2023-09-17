package com.test.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context :Context , attrs :AttributeSet) : View(context,attrs) {
//    mdrawPath stores a traced path and is used to display it on screen
    private var mdrawPath : CustomPath? = null
    private var mcanvasBitmap : Bitmap? = null
    private var mdrawPaint : Paint? = null
    private var mcanvasPaint : Paint? = null
    private var color :Int = Color.BLACK
    private var mbrushSize : Float = 0.toFloat()
    private var mcanvas : Canvas? = null

//    mPaths stores all traced paths so that previous paths don't disappear on lifting finger from screen
    private val mPaths = ArrayList<CustomPath>()

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
    }

//    OnSizeChanged fxn is called to setup canvas when screen is displayed
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        setting up canvas by creating a bitmap
        mcanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        mcanvas = Canvas(mcanvasBitmap!!)
    }

//    fxn to implement drawing using drawPath
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(mcanvasBitmap!!,0f,0f,mcanvasPaint)

//    drawing all previous drawPaths stored in mPaths
        for(path in mPaths){
            mdrawPaint!!.strokeWidth = path.brushThickness
            mdrawPaint!!.color = path.color
            canvas?.drawPath(path,mdrawPaint!!)
        }
//        drawing current path on screen
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
//                when finger is lifted, mdrawPath is pushed to paths arraylist (so it can store all traced paths)
//                when finger is lifted , mdrawPath is updated (as color or brush size is updated by user)
                mPaths.add(mdrawPath!!)
                mdrawPath = CustomPath(color,mbrushSize)
            }
            else -> {
                return false

            }
        }
        invalidate()
        return true
    }

//    this public fxn allows to set brush size for drawing
    fun setBrushSize(size: Float){
        mbrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,size, resources.displayMetrics)
        mdrawPaint!!.strokeWidth = mbrushSize
    }
//    this class allows to choose a custom color and brush thickness for brush path
    internal inner class CustomPath (var color : Int , var brushThickness : Float) : Path() {

    }
}