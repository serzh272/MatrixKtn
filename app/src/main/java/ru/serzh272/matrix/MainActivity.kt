package ru.serzh272.matrix

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ru.serzh272.matrix.views.MatrixViewGroup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var m: Matrix = Matrix()
        m[0,0] = Fraction(100000, 157u)
        m[0,1] = Fraction(5, 2u)
        m[0,2] = Fraction(1)
        m[1,0] = Fraction(1)
        m[1,1] = Fraction(1)
        m[1,2] = Fraction(1)
        m[2,0] = Fraction(1)
        m[2,1] = Fraction(1)
        m[2,2] = Fraction(1)
        var ll:LinearLayout = linearLo
        var mvg: MatrixViewGroup =
            MatrixViewGroup(this, m)
        mvg.spacing = 4
        mvg.cellsColor = Color.LTGRAY
        mvg.mode = 2
        ll.addView(mvg)
        var btn:Button = Button(this)
        var btnF:Button = Button(this)
        btn.text = "determinant"
        btnF.text = "frobenius"

        btn.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, mvg.presenter.mMatrix?.determinant().toString(),Toast.LENGTH_SHORT).show()
            }
        })
        btnF.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, mvg.presenter.mMatrix?.getFrobeniusMatrix().toString(),Toast.LENGTH_SHORT).show()
            }
        })
        ll.addView(btn)
        ll.addView(btnF)
    }
}
