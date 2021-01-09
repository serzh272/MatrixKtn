package ru.serzh272.matrix.contracts

import ru.serzh272.matrix.Fraction

interface IFractionContract {
    interface IFractionView{
        fun showFraction()
    }

    interface IFractionPresenter{
        fun showDialog()
        fun setFraction(n:Long, d:ULong, i:Long)
        fun setFraction(fr:Fraction)
    }

}