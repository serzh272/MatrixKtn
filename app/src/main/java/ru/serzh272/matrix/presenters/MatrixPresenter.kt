package ru.serzh272.matrix.presenters

import ru.serzh272.matrix.Fraction
import ru.serzh272.matrix.Matrix
import ru.serzh272.matrix.contracts.IMatrixContract
import ru.serzh272.matrix.models.MatrixModel
import ru.serzh272.matrix.views.FractionView

class MatrixPresenter: IMatrixContract.IMatrixPresenter {
    var mMatrixView: IMatrixContract.IMatrixView?
    var mMatrix:Matrix? = null

    constructor(view: IMatrixContract.IMatrixView){
        mMatrixView = view
        mMatrix = Matrix(2,3)
    }

    override fun handleFractionViewClick() {

    }

    override fun addRow() {
        mMatrix?.addRow()
        mMatrixView?.addRow()
    }

    override fun addColumn() {
        mMatrix?.addColumn()
        mMatrixView?.addColumn()
    }

    override fun removeRow() {
        mMatrixView?.removeRow()
        mMatrix?.removeRow()
    }

    override fun removeColumn() {
        mMatrixView?.removeColumn()
        mMatrix?.removeColumn()
    }

    override fun detachView() {
        mMatrixView = null
    }

    override fun showEditText(fv: FractionView) {
        mMatrixView?.showEditText(fv)
    }

    override fun setValue(r: Int, c: Int, fr: Fraction) {
        if ((r<mMatrix!!.numRows) and (c<mMatrix!!.numColumns)){
            mMatrix!![r,c].numerator = fr.numerator
            mMatrix!![r,c].denominator = fr.denominator
            mMatrix!![r,c].integ = fr.integ
        }
        mMatrixView!!.updateFractionView(r, c)
    }
}