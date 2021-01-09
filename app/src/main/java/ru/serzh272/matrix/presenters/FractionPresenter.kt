package ru.serzh272.matrix.presenters

import ru.serzh272.matrix.Fraction
import ru.serzh272.matrix.contracts.IFractionContract

class FractionPresenter:
    IFractionContract.IFractionPresenter {
    private lateinit var mFractionView: IFractionContract.IFractionView
    var mFraction: Fraction
    constructor(view: IFractionContract.IFractionView){
        mFractionView = view
        mFraction = Fraction()
    }

    override fun showDialog() {
        mFraction.numerator++
        mFractionView.showFraction()
    }

    override fun setFraction(n: Long, d: ULong, i: Long) {
        mFraction.numerator = n
        mFraction.denominator = d
        mFraction.integ = i
    }

    override fun setFraction(fr: Fraction) {
        mFraction.numerator = fr.numerator
        mFraction.denominator = fr.denominator
        mFraction.integ = fr.integ
    }


}