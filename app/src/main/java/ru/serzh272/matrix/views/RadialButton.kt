package ru.serzh272.matrix.views

import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.widget.AppCompatButton

class RadialButton(context: Context?) : AppCompatButton(context) {
    private var thickness: Float = 0.0F
    private var rad: Double = Math.PI/2
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}