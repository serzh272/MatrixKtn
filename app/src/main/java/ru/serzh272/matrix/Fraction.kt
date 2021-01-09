package ru.serzh272.matrix

import java.lang.Exception
import kotlin.math.pow

class Fraction{

    var numerator:Long = 0
        set(value) {
            field = value

        }
    var denominator:ULong = 1u
        set(value) {
            field = value

        }
    var integ:Long = 0
        set(value) {
            field = value

        }

    constructor(){
        numerator = 0
        denominator = 1u
    }
    constructor(n:Long, d:ULong, i:Long = 0){
        if (d.toLong() == 0L) throw Exception("Denominator must be not 0")
        numerator = d.toLong()*i + n
        denominator = d
        this.normalize()
    }

    constructor(n:Long){
        numerator = n
        denominator = 1u
    }

    fun toMixed(): Fraction {
        this.normalize()
        if (this.numerator > this.denominator.toLong()){
            var res = Fraction()
            res.numerator = this.numerator % this.denominator.toLong()
            res.denominator = this.denominator
            res.integ = this.integ + this.numerator / this.denominator.toLong()
            return res
        }
        return Fraction(
            this.numerator,
            this.denominator,
            this.integ
        )
    }

    fun normalize(){
        if (this.numerator == 0L){
            this.denominator = 1u
        }
        else{
            val nd: Long = nod(this.numerator, this.denominator.toLong())
            this.numerator /= nd
            this.denominator /= nd.toUInt()
        }
    }

    private fun nod(a: Long, b: Long): Long {
        var an: Long = kotlin.math.abs(a)
        var bn: Long = kotlin.math.abs(b)
        while ((an !=0L) and (bn != 0L)){
            if (an > bn){
                an %= bn
            }
            else{
                bn %= an
            }
        }
        return an + bn
    }

    private fun nok(a: Long, b: Long): Long {
        var an: Long = kotlin.math.abs(a)
        var bn: Long = kotlin.math.abs(b)
        an /= nod(a,b)
        return an*bn
    }

    operator fun unaryMinus(): Fraction {
        return Fraction(
            -this.numerator,
            this.denominator
        )
    }
    operator fun plus(fr: Fraction): Fraction {
        val nk: Long = nok(this.denominator.toLong(), fr.denominator.toLong())
        val n1: Long = this.numerator * (nk/this.denominator.toLong())
        val n2: Long = fr.numerator * (nk/fr.denominator.toLong())
        return Fraction(n1 + n2, nk.toULong())
    }

    operator fun plus(n: Long): Fraction {
        return Fraction(
            this.numerator + n * this.denominator.toLong(),
            this.denominator
        )
    }

    operator fun Long.plus(fr: Fraction): Fraction {
        return fr + this
    }

    operator fun minus(fr: Fraction): Fraction {
        return this + (-fr)
    }

    operator fun minus(n: Long): Fraction {
        return this - n
    }

    operator fun Long.minus(fr: Fraction): Fraction {
        return (-fr) + this
    }

    operator fun times(fr: Fraction): Fraction {
        var i: Fraction =
            Fraction(
                this.numerator,
                this.denominator
            )
        var j: Fraction =
            Fraction(fr.numerator, fr.denominator)
        var l: Long = j.numerator
        j.numerator = i.numerator
        i.numerator = l
        j.normalize()
        i.normalize()

        return Fraction(
            i.numerator * j.numerator,
            i.denominator * j.denominator
        )
    }

    operator fun times(n: Long): Fraction {
        return this * Fraction(n, 1u)
    }

    operator fun Long.times(fr: Fraction): Fraction {
        return fr * this
    }

    fun invert(): Fraction {
        var rez = Fraction(
            this.numerator,
            this.denominator
        )
        if (rez.numerator != 0L){
            var n: Long = rez.denominator.toLong()
            if (rez.numerator > 0){
                rez.denominator = rez.numerator.toULong()
                rez.numerator = n
            }
            else{
                rez.denominator = (- rez.numerator).toULong()
                rez.numerator = -n
            }
        }
        return rez
    }

    operator fun div(fr: Fraction): Fraction {
        return this * (fr.invert())
    }

    operator fun div(n: Long): Fraction {
        return this/ Fraction(n, 1u)
    }

    operator fun Long.div(fr: Fraction): Fraction {
        return this * (fr.invert())
    }

    operator fun compareTo(fr: Fraction): Int {
        val f: Fraction = this - fr
        if (f.numerator > 0){
            return 1
        }else if (f.numerator == 0L){
            return 0
        }
        return -1
    }

    fun abs(): Fraction {
        var res: Fraction = this.copy()
        if (this.numerator < 0){
            return -res
        }
        return res
    }

    fun power(p: Int): Fraction {
        return Fraction(
            this.numerator.toDouble().pow(p).toLong(), this.denominator.toDouble().pow(p).toULong()
        )
    }

    fun copy(): Fraction {
        return Fraction(
            this.numerator,
            this.denominator,
            this.integ
        )
    }

    fun toDouble(): Double {
        return this.numerator.toDouble()/this.denominator.toLong()
    }

    /*operator fun minusAssign(fr: Fraction) {
        val res: Fraction = this - fr
        this.numerator = res.numerator
        this.denominator = res.denominator
    }*/

    override fun equals(fr: Any?): Boolean {
        this.normalize()
        return when (fr){
            is Fraction -> {
                fr.normalize()
                (this.numerator == fr.numerator) and (this.denominator == fr.denominator)
            }
            is Long ->{
                (this.numerator == fr) and (this.denominator.toLong() == 1L)
            }
            is Int ->{
                (this.numerator.toInt() == fr) and (this.denominator.toLong() == 1L)
            }
            else -> false
        }
    }

    fun setFromString(str:String){
        if (str.contains('/')){
            var frList: List<String> = str.split('/')
            if (frList.size == 2){
                var sNumerator = frList[0]
                var sDenominator = frList[1]
                if (sDenominator != ""){
                    numerator = sNumerator.toLong()
                    denominator = sDenominator.toULong()
                }
            }
        }
        else if (str.contains(',') or str.contains('.')){

        }else if (str == ""){
            numerator = 0
            denominator = 1u
        }
        else{
            numerator = str.toLong()
            denominator = 1u
        }
    }

    override fun toString(): String {
        if (this.denominator.toLong() != 1L)
            return "$numerator/$denominator"
        else
            return "$numerator"
    }


}