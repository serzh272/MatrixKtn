package ru.serzh272.matrix.views

import android.app.AlertDialog
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.serzh272.matrix.R
import ru.serzh272.matrix.Fraction
import ru.serzh272.matrix.presenters.FractionPresenter
import ru.serzh272.matrix.contracts.IFractionContract
import kotlin.math.max


class FractionView: View,
    IFractionContract.IFractionView {
    var backColor: Int = Color.WHITE
    var mode: Int = 2
    private var sp:Float = 0.0f
    private var anchor: Point = Point(0, 0)
    var presenter: FractionPresenter = FractionPresenter(this)
    var pos:Point = Point()
    constructor(context: Context?): this(context, null){
    }
    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        initAttrs(context,attrs)
    }
    private fun initAttrs(context: Context?, attrs: AttributeSet?){
        var a:TypedArray = context!!.theme.obtainStyledAttributes(attrs,
            R.styleable.FractionViewLayout, 0,0)
        try {
            presenter.mFraction.numerator = a.getInteger(R.styleable.FractionViewLayout_numerator, 0).toLong()
            presenter.mFraction.denominator = a.getInteger(R.styleable.FractionViewLayout_denominator, 1).toULong()
            presenter.mFraction.integ = a.getInteger(R.styleable.FractionViewLayout_integ, 0).toLong()
            mode = a.getInteger(R.styleable.FractionViewLayout_mode, 1)
            backColor = a.getColor(R.styleable.FractionViewLayout_color, Color.WHITE)
            sp = a.getDimension(R.styleable.FractionViewLayout_fraction_space, 2.0f)
        }
        finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        showFraction(canvas!!, presenter.mFraction)
    }

    override fun showFraction() {
        invalidate()
    }

    fun showFraction(canvas: Canvas, fraction: Fraction) {
        var p:Paint = Paint()
        p.style = Paint.Style.STROKE
        p.strokeWidth = 0.0f
        p.textSize = this.height.toFloat()/3
        p.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC)
        p.textAlign = Paint.Align.CENTER
        anchor.x = this.width/2
        anchor.y = this.height/2
        var txt:String
        var integTxt:String = ""
        var bndNum: Rect = Rect()
        var bndDen: Rect = Rect()
        var textHeight:Float
        var bndInteg: Rect = Rect(0,0,0,0)
        var frSpan: Float = 0.0f
        var integSpan: Float = 0.0f
        p.style = Paint.Style.FILL
        p.color = backColor
        canvas.drawRect(0.0f, 0.0f, this.width.toFloat(), this.height.toFloat(),p)
        p.setColor(Color.BLUE)
        if ((fraction.denominator.toUInt() == 1u) or (mode == 3)){
            txt = if (mode == 3){
                if (fraction.numerator%fraction.denominator.toLong() == 0L){
                    this.toString()
                }
                else{
                    "%.3f".format(fraction.toDouble())
                }
            }
            else{
                fraction.numerator.toString()
            }
            p.getTextBounds(txt,0, txt.length, bndNum)
            if (bndNum.width()> (this.width - sp)){
                p.textSize *= ((this.width.toFloat()-sp)/bndNum.width().toFloat())
                p.getTextBounds(txt,0, txt.length, bndNum)
            }
            textHeight = bndNum.height().toFloat()
            canvas.drawText(txt,anchor.x.toFloat(),anchor.y.toFloat()+textHeight/2, p)
        }
        else{
            var fr: Fraction = fraction.copy()
            if (mode == 2){
                fr = fraction.toMixed()
            }
            txt = fr.numerator.toString()
            p.getTextBounds(txt,0, txt.length, bndNum)
            textHeight = bndNum.height().toFloat()
            txt = fr.denominator.toString()
            p.getTextBounds(txt,0, txt.length, bndDen)
            sp = bndDen.height().toFloat()/5
            var wdt:Float = max(bndNum.width().toFloat(), bndDen.width().toFloat())
            if (fr.integ != 0L){
                integTxt = fr.integ.toString()
                p.getTextBounds(integTxt, 0, integTxt.length, bndInteg)
                frSpan = bndInteg.width().toFloat()/2 +sp
                integSpan = wdt/2 +sp
            }
            if ((bndInteg.width() + wdt + sp*8) > this.width){
                val k:Float = (this.width.toFloat())/(bndInteg.width().toFloat() + wdt + sp*8)
                p.textSize *= k
                wdt *= k
                frSpan *= k
                integSpan *= k
                textHeight *= k
                sp *= k
                p.getTextBounds(integTxt, 0, integTxt.length, bndInteg)
            }
            if (fr.integ != 0L){
                canvas.drawText(integTxt,anchor.x.toFloat() -integSpan,anchor.y.toFloat()+textHeight/2, p)
            }
            canvas.drawText(fr.numerator.toString(),anchor.x.toFloat() + frSpan,anchor.y.toFloat()-sp, p)
            canvas.drawText(fr.denominator.toString(),anchor.x.toFloat() + frSpan,anchor.y.toFloat()+textHeight+sp, p)
            p.strokeWidth = 3.0f
            canvas.drawLine(anchor.x.toFloat() - wdt/2+frSpan, anchor.y.toFloat(), anchor.x.toFloat() + wdt/2 + frSpan, anchor.y.toFloat(),p)
        }
    }


    /*fun setValue(numerator:Long, denominator:ULong, integ:Long){
        this.fraction.numerator = numerator
        this.fraction.denominator = denominator
        this.fraction.integ = integ
    }

    fun setValue(fr: Fraction){
        this.fraction.numerator = fr.numerator
        this.fraction.denominator = fr.denominator
        this.fraction.integ = fr.integ
    }

    fun getValue(): Fraction {
        return fraction
    }*/

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun toString(): String {
        return presenter.mFraction.toString()
    }

}