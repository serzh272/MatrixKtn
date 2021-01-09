package ru.serzh272.matrix

import kotlin.math.sqrt


class Matrix(r: Int = 3, c: Int = 3) : IMatrix {
    override var numColumns:Int = c
        set(value) {
            if ((value > 0) and (value <= 20))
                field = value
        }
    override var numRows:Int = r
        set(value) {
            if ((value > 0) and (value <= 20))
                field = value
        }

    private var A: MutableList<MutableList<Fraction>> = MutableList<MutableList<Fraction>>(numRows){ MutableList<Fraction>(numColumns){ Fraction() }}

    init {
        A = MutableList<MutableList<Fraction>>(r){ MutableList<Fraction>(c){ Fraction() }}
    }

    operator fun plus(m: Matrix):Matrix {
        if ((this.numColumns != m.numColumns) or (this.numRows != m.numRows)){
            throw Exception("Matrix sizes not equals")
        }
        else{
            var res:Matrix = Matrix(this.numRows, this.numColumns)
            for (i:Int in 0 until this.numRows){
                for (j:Int in 0 until this.numColumns){
                    res[i, j] = this[i, j] + m[i, j]
                }
            }
            return res
        }
    }

    operator fun minus(m: Matrix):Matrix{
        if ((this.numColumns != m.numColumns) or (this.numRows != m.numRows)){
            throw Exception("Matrix sizes not equals")
        }
        else{
            var res:Matrix = Matrix(this.numRows, this.numColumns)
            for (i:Int in 0 until this.numRows){
                for (j:Int in 0 until this.numColumns){
                    res[i, j] = this[i, j] - m[i, j]
                }
            }
            return res
        }
    }

    override operator fun get(r: Int, c: Int): Fraction {
        return this.A[r][c]
    }

    override operator fun set(r: Int, c: Int, fr: Fraction){
        this.A[r][c] = fr.copy()
    }

    operator fun times(m: Matrix):Matrix{
        when {
            this.numColumns == m.numRows -> {
                var res:Matrix = Matrix(this.numRows, m.numColumns)
                for (i in 0 until this.numRows){
                    for (j in 0 until m.numColumns){
                        res[i, j] = Fraction()
                        for (g in 0 until m.numRows){

                            res[i, j] = res[i, j] + this[i,g] * m[g, j]
                        }
                    }
                }
                return res
            }
            (this.numRows == 1) and (this.numColumns == 1) -> {
                return this[0, 0] * m
            }
            (m.numRows == 1) and (m.numColumns == 1) -> {
                return this*m[0, 0]
            }
            else -> {
                throw Exception("Columns number of M1 must be equal Columns number of M2")
            }
        }
    }
    operator fun times(fr: Fraction):Matrix{
        var res:Matrix = Matrix(this.numRows, this.numColumns)
        for (i in 0 until this.numRows){
            for (j in 0 until this.numColumns){
                res[i, j] = res[i, j] * fr
            }
        }
        return res
    }

    operator fun Fraction.times(m: Matrix):Matrix{
        return m * this
    }

    override fun addRow(pos: Int){
        this.A.add(pos, MutableList(this.numColumns){ Fraction() })
        this.numRows++
    }

    override fun addRow(){
        this.A.add(MutableList(this.numColumns){ Fraction() })
        this.numRows++
    }

    override fun addColumn(pos: Int){
        for (i in 0 until this.numRows){
            this.A[i].add(pos, Fraction())
        }
        this.numColumns++
    }

    override fun addColumn(){
        for (i in 0 until this.numRows){
            this.A[i].add(Fraction())
        }
        this.numColumns++
    }

    override fun removeRowAt(pos: Int){
        if ((pos < this.numRows) and (pos >= 0)){
            this.A.removeAt(pos)
            this.numRows--
        }
        else{
            throw IndexOutOfBoundsException()
        }
    }

    override fun removeRow() {
        removeRowAt(numRows - 1)
    }

    override fun removeColumnAt(pos: Int){
        if ((pos < this.numColumns) and (pos >= 0)){
            for (i in 0 until this.numRows){
                A[i].removeAt(pos)
            }
            this.numColumns--
        }
        else{
            throw IndexOutOfBoundsException()
        }
    }

    override fun removeColumn() {
        removeColumnAt(numColumns - 1)
    }

    override fun addRows(pos: Int, n: Int){
        for (i in 1..n){
            this.addRow(pos + i-1)
        }
    }

    override fun addColumns(pos: Int, n: Int){
        for (i in 1..n){
            this.addColumn(pos + i-1)
        }
    }

    fun swapRows(r1: Int, r2: Int){
        var f: Fraction
        for (j in 0 until this.numColumns){
            f = this[r1, j]
            this[r1, j] = this[r2, j]
            this[r2, j] = f
        }
    }

    fun swapColumns(c1: Int, c2: Int){
        var f: Fraction
        for (i in 0 until this.numRows){
            f = this[i, c1]
            this[i, c1] = this[i, c2]
            this[i, c2] = f
        }
    }

    fun multRows (r1: Int, r2: Int, fr: Fraction){
        for (j in 0 until this.numColumns){
            this[r2,j] -= this[r1, j] * fr
        }
    }

    fun multColumns (c1: Int, c2: Int, fr: Fraction){
        for (i in 0 until this.numRows){
            this[i,c2] -= this[i, c1] * fr
        }
    }

    fun norm3():Double{
        var fr: Fraction =
            Fraction()
        for (i in 0 until this.numRows){
            for (j in 0 until this.numColumns){
                var frItem: Fraction =
                    Fraction(
                        this[i, j].numerator,
                        this[i, j].denominator,
                        this[i, j].integ
                    )
                fr += frItem.power(2)
            }
        }
        return sqrt(fr.toDouble())
    }

    override fun transpose(){
        var m:Matrix = Matrix(this.numColumns, this.numRows)
        for (i in 0 until this.numRows){
            for (j in 0 until this.numColumns){
                m[j, i] = this[i, j]
            }
        }
        this.A = m.A
        this.numRows = m.numRows
        this.numColumns = m.numColumns
    }

    fun copy():Matrix{
        var res:Matrix = Matrix(this.numRows, this.numColumns)
        for (i in 0 until this.numRows){
            for (j in 0 until this.numColumns){
                res[i, j] = this[i, j].copy()
            }
        }
        return res
    }

    fun determinant(): Fraction {
        if (this.numRows == this.numColumns){
            var m:Matrix = this.copy()
            var n:Int = this.numColumns
            var fr1: Fraction
            var fr2: Fraction
            var d: Fraction =
                Fraction(1, 1u)
            var sumRow: Fraction =
                Fraction(0)
            var sumCol: Fraction =
                Fraction(0)
            for (i in 0 until n){
                for (j in 0 until n){
                    if (m[i, j].equals(0)){
                        sumRow = Fraction(0)
                        sumCol = Fraction(0)
                        for (p in 0 until n){
                            sumCol += m[i,p].abs()
                            sumRow += m[p,j].abs()
                        }
                        if ((sumCol.equals(0)) or (sumRow.equals(0))){
                            return Fraction()
                        }
                    }
                }
            }
            for (step in 0 until (n - 1)){
                for (i in step until n){
                    if (!(m[i,step].equals(0))){
                        if (m[step, step].equals(0)){
                            m.swapRows(step, i)
                            d *= -1
                        }
                        else{
                            for (j in 0 until n){
                                if ((i != j) and !(m[j, step].equals(0)) and (j>= step)){
                                    if (j < i){
                                        fr1 = m[i, step].copy()
                                        fr2 = m[j,step].copy()
                                        m.multRows(j, i, fr1/fr2)
                                    }else{
                                        fr1 = m[j,step].copy()
                                        fr2 = m[i, step].copy()
                                        m.multRows(i, j, fr1/fr2)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (i in 0 until n){
                d *= m[i,i]
            }
            return  d
        }
        else{
            throw Exception("Matrix must be squared")
        }
    }

    fun setEMatr(){
        if (this.numRows == this.numColumns){
            for (i in 0 until this.numRows){
                for (j in 0 until this.numColumns){
                    if (i == j){
                        this[i,j] = Fraction(1)
                    }else{
                        this[i,j] = Fraction(0)
                    }
                }
            }
        }else{
            throw Exception("Matrix must be squared")
        }
    }

    fun transformMatrix(type:Int):Matrix{
        var dt: Fraction = this.determinant()
        if (dt.equals(0)){
            throw Exception("Determinant equals 0")
        }
        else{
            var m: Fraction =
                Fraction()
            var n:Int = this.numColumns
            var e:Matrix = Matrix(n, n)
            var x:Matrix = Matrix(n, n)
            var y:Matrix = Matrix(n, n)
            var b:Matrix = Matrix(n, n)
            var c:Matrix = Matrix(n, n)
            e.setEMatr()
            for (j in 0 until n){
                for (i in j until n){
                    for (g in 0 until j){
                        m += b[i,g] * c[g, j]
                    }
                    b[i, j] = this[i,j] - m
                    m = Fraction()
                    for (g in 0 until j){
                        m += b[j,g] * c[g, i]
                    }
                    c[j, i] = (this[j, i] - m)/b[j, j]
                    m = Fraction()
                }
            }
            for (i in 0 until n) {
                for (j in 0 until n) {
                    for (g in 0 until j) {
                        m += b[j, g]*y[g,i]
                    }
                    y[j, i] = (e[j, i] - m) / b[j,j]
                    m = Fraction()
                }
            }
            for (i in 0 until n) {
                for (j in n - 1 downTo 0) {
                    for (g in 0 until n - j - 1) {
                        m += c[j, n-g-1] * x[n-g-1, i]
                    }
                    x[j,i] = y[j, i] - m
                    m = Fraction()
                }
            }
            return when (type){
                1 -> x
                2 -> b
                else ->c
            }
        }
    }

    fun getFrobeniusMatrix():Matrix{
        if (this.numRows == this.numColumns){
            var n: Int = this.numColumns
            var mFr:Matrix = Matrix(n,n)
            var e = Matrix(n,n)
            mFr = this.copy()
            for (g in 0 until n - 1) {
                var d: Fraction =
                    Fraction()
                for (i in 0..n - g - 2) {
                    d += mFr[n - g - 1, i]
                }
                if (d.equals(0)) {
                    var b:Matrix = Matrix(n - g - 1, n - g - 1)
                    for (i in 0 until n - g - 1) {
                        for (j in 0 until n - g - 1) {
                            b[i, j] = mFr[i, j]
                        }
                    }
                    b = b.getFrobeniusMatrix()
                    for (i in 0 until n - g - 1) {
                        for (j in 0 until n - g - 1) {
                            mFr[i, j] = b[i, j]
                            //mFr.A[i][j].Color = Color.FromRgb(127, 251, 189);
                        }
                    }
                    for (i in n - g - 1 until n) {
                        for (j in n - g - 1 until n) {
                            //mFr.A[i, j].Color = Color.FromRgb(127, 251, 189);
                        }
                    }
                    return mFr
                } else if (mFr[n - g - 1, n - g - 2].equals(0)) {
                    mFr.swapColumns(n - g - 1, n - g - 2)
                    mFr.swapRows(n - g - 1, n - g - 2)
                }
                e.setEMatr()
                for (i in 0 until n) {
                    e[n - g - 2, i] = mFr[n - g - 1, i].copy()
                }
                mFr = if (!e.determinant().equals(0)) {
                    (e * mFr)* (e.transformMatrix(1))
                } else {
                    (e * mFr)* (e.transformMatrix(1))
                }
            }
            return mFr
        }
        else{
            throw Exception("Matrix must be squared")
        }
    }
    override fun toString(): String {
        var res:String = ""
        for (i in 0 until this.numRows){
            for (j in 0 until this.numColumns){
                if (this[i, j] == null){
                    res += "0"
                }
                else{
                    res += this[i, j]
                }

                if (j != (this.numColumns-1)){
                    res += "\t\t"
                }
            }
            if (i != (this.numRows-1)) {
                res += '\n'
            }
        }
        return res
    }
}