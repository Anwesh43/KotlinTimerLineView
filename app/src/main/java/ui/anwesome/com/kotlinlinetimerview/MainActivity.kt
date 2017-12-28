package ui.anwesome.com.kotlinlinetimerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.timerlineview.TimerLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = TimerLineView.create(this)
        view.addTime(3)
        view.addTime(5)
        view.addTime(10)
        view.addTime(15)
        view.addTime(20)
        view.addToParent(this)
    }
}
