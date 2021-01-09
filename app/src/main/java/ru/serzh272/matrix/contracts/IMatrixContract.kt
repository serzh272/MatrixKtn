package ru.serzh272.matrix.contracts

import android.graphics.Point
import ru.serzh272.matrix.Fraction
import ru.serzh272.matrix.IMatrix
import ru.serzh272.matrix.views.FractionView

interface IMatrixContract{
    interface IMatrixView{
        var numRows : Int
        var numColumns : Int
        fun showEditText(fv: FractionView)
        fun hideEditText()
        fun addRow(pos: Int)
        fun addRow()
        fun addColumn(pos: Int)
        fun addColumn()
        fun removeRowAt(pos: Int)
        fun removeColumnAt(pos: Int)
        fun addRows(pos: Int, n: Int)
        fun addColumns(pos: Int, n: Int)
        fun removeRow()
        fun removeColumn()
        fun updateFractionView(r:Int, c:Int)

    }

    interface IMatrixPresenter{
        fun handleFractionViewClick()
        fun addRow()
        fun addColumn()
        fun removeRow()
        fun removeColumn()
        fun detachView()
        fun showEditText(fv:FractionView)
        fun setValue(r:Int, c:Int, fr:Fraction)
    }
}

