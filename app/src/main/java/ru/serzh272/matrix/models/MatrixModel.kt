package ru.serzh272.matrix.models

import ru.serzh272.matrix.Fraction
import ru.serzh272.matrix.Matrix
import java.lang.Exception

class MatrixModel {
    private val numItems: Int = 3
    private var mMatrices:ArrayList<Matrix> = ArrayList()
    private var model: MatrixModel? = null
    private constructor(){
        for (i in 0 until numItems){
            mMatrices.add(Matrix())
        }
    }

    fun getInstance(): MatrixModel {
        return if (model == null){
            MatrixModel()
        }
        else{
            this
        }
    }

    operator fun get(pos:Int): Matrix {
        if (pos < numItems){
            return mMatrices[pos]
        }
        else{
            throw Exception("Out of range")
        }
    }

    fun updateItem(nMatrix:Int, r:Int, c:Int, fr: Fraction){
        mMatrices[nMatrix][r, c] = fr
    }
}