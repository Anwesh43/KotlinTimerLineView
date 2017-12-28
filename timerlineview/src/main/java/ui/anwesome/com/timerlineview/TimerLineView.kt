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
    data class TimerLine(var time:Int,var i:Int,var y:Float) {
        fun draw(canvas: Canvas,paint:Paint,w:Float) {
            paint.strokeWidth = w/30
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.GRAY
            canvas.drawLine(w/10,y,9*w/10,y,paint)
        }
        fun update(stopcb:(Int)->Unit) {

        }
    }
    data class TimerLineState(var time:Int,var t:Int = 0) {
        fun update(stopcb:()->Unit) {
            t++
            stopcb()
        }
        fun executeFn(cb:(Float)->Unit) {
            cb(time.toFloat()/t.toFloat())
        }
    }
}