package ru.serzh272.matrix.views

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ru.serzh272.matrix.*
import ru.serzh272.matrix.Fraction
import ru.serzh272.matrix.contracts.IMatrixContract
import ru.serzh272.matrix.presenters.MatrixPresenter
import kotlin.math.min

class MatrixViewGroup : ViewGroup, IMatrixContract.IMatrixView, View.OnClickListener{
    var presenter: MatrixPresenter = MatrixPresenter(this)
    override var numRows : Int = presenter.mMatrix?.numRows ?: 3
    override var numColumns : Int =presenter.mMatrix?.numColumns ?: 3
    var mode = 1
    var spacing: Int = 0
    private var buttonThickness: Int = 50
    private var cellSize: Float = 50.0f

    var cellsColor:Int = Color.WHITE
    private var btnLeft: Button = Button(context)
    private var btnUp: Button = Button(context)
    private var btnRight: Button = Button(context)
    private var btnDown: Button = Button(context)
    private var mEditText: EditText = EditText(context)
    private var currentPos:Point = Point(0, 0)
    private var mFractionViews: MutableList<MutableList<FractionView>> = MutableList<MutableList<FractionView>>(numRows){ MutableList<FractionView>(numColumns){
        FractionView(context)
    }}
    constructor(context: Context?, matrix: Matrix):super(context){
        addView(btnDown)
        btnDown.setOnClickListener(this)
        addView(btnUp)
        btnUp.setOnClickListener(this)
        addView(btnLeft)
        btnLeft.setOnClickListener(this)
        addView(btnRight)
        btnRight.setOnClickListener(this)
        mEditText.inputType = InputType.TYPE_NULL
        mEditText.inputType = InputType.TYPE_CLASS_DATETIME
        mEditText.setBackgroundColor(Color.WHITE)
        for (i in 0 until numRows){
            for (j in 0 until numColumns){
                mFractionViews[i][j].presenter.mFraction = presenter.mMatrix!![i,j]
                mFractionViews[i][j].setOnClickListener (this)
                addView(mFractionViews[i][j])
            }
        }
    }
    private constructor(context: Context?):super(context)
    private constructor(context: Context?, attrs: AttributeSet):super(context, attrs){
        initAttrs(context!!, attrs)
    }
    private constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initAttrs(context!!, attrs)
    }

    override fun showEditText(fv:FractionView) {
        mEditText.setText("${fv.toString()}")
        mEditText.layout( fv.left, fv.top, fv.right, fv.bottom)
        if (indexOfChild(mEditText) != -1) {
            removeView(mEditText)
            addView(mEditText)
        }
        else{
            addView(mEditText)
        }
    }

    override fun hideEditText() {
        if (indexOfChild(mEditText) != -1) {
            mFractionViews[currentPos.x][currentPos.y].presenter.mFraction.setFromString(mEditText.text.toString())
            removeView(mEditText)
            currentPos.x = 0
            currentPos.y = 0
        }
    }

    private fun initAttrs(context: Context,attrs: AttributeSet) {
        val a =context.theme.obtainStyledAttributes(attrs,
            R.styleable.MatrixLayout, 0, 0)
        try {
            numRows = a.getInt(R.styleable.MatrixLayout_rows, 2)
            numColumns = a.getInt(R.styleable.MatrixLayout_columns, 3)
            spacing = a.getDimensionPixelSize(R.styleable.MatrixLayout_spacing, 0)
            cellsColor = a.getColor(R.styleable.MatrixLayout_cells_color, Color.WHITE)
        } finally {
            a.recycle()
        }
    }

    /*override fun get(r: Int, c: Int): Fraction {
        TODO("Not yet implemented")
    }

    override fun set(r: Int, c: Int, fr: Fraction) {
        TODO("Not yet implemented")
    }*/



    override fun addRow(pos: Int) {
        this.mFractionViews.add(pos, MutableList(this.numColumns){
            FractionView(context)
        })
        for (j in 0 until numColumns){
            mFractionViews[pos][j].presenter.mFraction = presenter.mMatrix!![pos,j]
            addView(mFractionViews[pos][j])
        }
        this.numRows++
    }

    override fun addRow() {
        this.mFractionViews.add(MutableList(this.numColumns){
            FractionView(context)
        })
        for (j in 0 until numColumns){
            mFractionViews[numRows][j].presenter.mFraction = presenter.mMatrix!![numRows,j]
            addView(mFractionViews[numRows][j])
        }
        this.numRows++
    }

    override fun addColumn(pos: Int) {
        for (i in 0 until this.numRows){
            this.mFractionViews[i].add(pos,
                FractionView(context)
            )
            addView(mFractionViews[i][pos])
        }
        this.numColumns++
    }

    override fun addColumn() {
        for (i in 0 until this.numRows){
            this.mFractionViews[i].add(
                FractionView(context)
            )
            addView(mFractionViews[i][numColumns])
        }
        this.numColumns++
    }

    override fun removeRowAt(pos: Int) {
        if ((pos < this.numRows) and (pos >= 0)){
            for (j in 0 until numColumns){
                removeView(mFractionViews[pos][j])
            }
            this.mFractionViews.removeAt(pos)
            this.numRows--
        }
        else{
            throw IndexOutOfBoundsException()
        }
    }

    override fun removeColumnAt(pos: Int) {
        if ((pos < this.numColumns) and (pos >= 0)){
            for (i in 0 until this.numRows){
                removeView(mFractionViews[i][pos])
                mFractionViews[i].removeAt(pos)
            }
            this.numColumns--
        }
        else{
            throw IndexOutOfBoundsException()
        }
    }

    override fun addRows(pos: Int, n: Int) {
        for (i in 1..n){
            this.addRow(pos + i-1)
        }
    }

    override fun addColumns(pos: Int, n: Int) {
        for (i in 1..n){
            this.addColumn(pos + i-1)
        }
    }

    override fun removeRow() {
        removeRowAt(numRows - 1)
    }

    override fun removeColumn() {
        removeColumnAt(numColumns - 1)
    }

    override fun updateFractionView(r: Int, c: Int) {
        mFractionViews[r][c].invalidate()
    }
    /*
    override fun transpose() {
        TODO("Not yet implemented")
    }*/



    override fun onClick(v: View?) {
        when(v){
            is FractionView -> {
                mFractionViews[currentPos.x][currentPos.y].presenter.mFraction.setFromString(mEditText.text.toString())
                mFractionViews[currentPos.x][currentPos.y].invalidate()
                currentPos.x = v.pos.x
                currentPos.y = v.pos.y
                presenter.showEditText(v)

                }
            btnLeft -> {
                presenter.removeColumn()
                hideEditText()
                Toast.makeText(context, "$numRows x $numColumns", Toast.LENGTH_SHORT).show()
            }
            btnRight -> {
                presenter.addColumn()
                hideEditText()
                Toast.makeText(context, "$numRows x $numColumns", Toast.LENGTH_SHORT).show()
            }
            btnDown -> {
                presenter.removeRow()
                hideEditText()
                Toast.makeText(context, "$numRows x $numColumns", Toast.LENGTH_SHORT).show()
            }
            btnUp -> {
                //addRow()
                presenter.addRow()
                hideEditText()
                Toast.makeText(context, "$numRows x $numColumns", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val leftPadding: Int = (measuredWidth - paddingLeft - paddingRight -
                numColumns * cellSize.toInt() - (numColumns - 1) * spacing -
                buttonThickness * 2) / 2
        val topPadding: Int = (measuredHeight - paddingTop - paddingBottom -
                numRows * cellSize.toInt() - (numRows - 1) * spacing - buttonThickness * 2) / 2
        for (i in 0 until numRows){
            for (j in 0 until numColumns){
                val left = paddingLeft + j * (cellSize.toInt() + spacing) + leftPadding +
                        buttonThickness
                val right = paddingLeft + (j + 1) * cellSize.toInt() + j * spacing + leftPadding +
                        buttonThickness
                val top: Int = paddingTop + i * (cellSize.toInt() + spacing) + topPadding +
                        buttonThickness
                val bottom: Int = paddingTop + (i + 1) * cellSize.toInt() + i * spacing +
                        topPadding + buttonThickness
                mFractionViews[i][j].setPadding(0, 0, 0, 0)
                mFractionViews[i][j].layout(left, top, right, bottom)
                mFractionViews[i][j].backColor = cellsColor
                mFractionViews[i][j].setOnClickListener(this)
                mFractionViews[i][j].mode = mode
                mFractionViews[i][j].pos.x = i
                mFractionViews[i][j].pos.y = j

            }
        }
        mEditText.gravity = Gravity.CENTER
        mEditText.setPadding(0,0,0,0)
        //showEditText(mFractionViews[currentPos.x][currentPos.y])

        mEditText.layout( mFractionViews[currentPos.x][currentPos.y].left,mFractionViews[currentPos.x][currentPos.y].top,mFractionViews[currentPos.x][currentPos.y].right,mFractionViews[currentPos.x][currentPos.y].bottom)
        mEditText.clearFocus()
        btnLeft.layout(
            0,
            buttonThickness,
            buttonThickness,
            measuredHeight - buttonThickness
        )
        btnRight.layout(
            measuredWidth  - buttonThickness,
            buttonThickness,
            measuredWidth,
            measuredHeight - buttonThickness
        )
        btnUp.layout(
            buttonThickness,
            0,
            measuredWidth - buttonThickness,
            buttonThickness
        )
        btnDown.layout(
            buttonThickness,
            measuredHeight - buttonThickness,
            measuredWidth - buttonThickness,
            measuredHeight
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        val heightM = heightMeasureSpec
        val widthM = widthMeasureSpec
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            widthMeasureSpec = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
            heightMeasureSpec = min(widthMeasureSpec, View.getDefaultSize(suggestedMinimumHeight,
                heightMeasureSpec))
        } else {
            heightMeasureSpec = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
            widthMeasureSpec = heightMeasureSpec
        }
        buttonThickness = heightMeasureSpec / 12
        val cellHeight: Int = (heightMeasureSpec - spacing * (numRows - 1) - paddingTop -
                paddingBottom - buttonThickness * 2) / numRows
        val cellWidth: Int = (widthMeasureSpec - spacing * (numColumns - 1) - paddingLeft -
                paddingRight - buttonThickness * 2) / numColumns
        cellSize = min(cellHeight, cellWidth).toFloat()
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }
}