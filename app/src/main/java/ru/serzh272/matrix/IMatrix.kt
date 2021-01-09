package ru.serzh272.matrix

interface IMatrix {
    var numColumns: Int
    var numRows: Int

    operator fun get(r: Int, c: Int): Fraction
    operator fun set(r: Int, c: Int, fr: Fraction)
    fun addRow(pos: Int)
    fun addRow()
    fun addColumn(pos: Int)
    fun addColumn()
    fun removeRowAt(pos: Int)
    fun removeRow()
    fun removeColumnAt(pos: Int)
    fun removeColumn()
    fun addRows(pos: Int = this.numRows, n: Int = 1)
    fun addColumns(pos: Int = this.numColumns, n: Int = 1)
    fun transpose()
}