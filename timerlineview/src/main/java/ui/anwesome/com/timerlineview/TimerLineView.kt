package ui.anwesome.com.timerlineview

/**
 * Created by anweshmishra on 29/12/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.*

class TimerLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var times:LinkedList<Int> = LinkedList()
    override fun onDraw(canvas:Canvas) {

    }
    fun addTime(time:Int) {
        times.add(time)
    }
    fun addToParent(activity:Activity) {
        activity.setContentView(this)
    }
}