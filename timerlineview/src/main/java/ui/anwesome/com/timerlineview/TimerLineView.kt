package ui.anwesome.com.timerlineview

/**
 * Created by anweshmishra on 29/12/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
val colors = arrayOf("#FF5722","#4CAF50","#3F51B5","#f44336","#00796B","#448AFF")
class TimerLineView(ctx:Context):View(ctx) {
    var onTimerFinishListener:OnTimerFinishListener?=null
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = TimerLineRenderer(this)
    var times:LinkedList<Int> = LinkedList()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    fun addTime(time:Int) {
        times.add(time)
    }
    fun addOnTimerFinishListener(listener:(Int)->Unit) {
        onTimerFinishListener = OnTimerFinishListener(listener)
    }
    fun addToParent(activity:Activity) {
        activity.setContentView(this)
    }
    data class TimerLine(var time:Int,var i:Int,var y:Float) {
        val state = TimerLineState(time)
        fun draw(canvas: Canvas,paint:Paint,w:Float) {
            paint.strokeWidth = w/30
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.GRAY
            canvas.drawLine(w/10,y,9*w/10,y,paint)
            state.executeFn { scale ->
                paint.color = Color.parseColor("#B2FF59")
                canvas.drawLine(w/10,y,w/10+(8*w/10)*scale,y,paint)
            }
        }
        fun update(stopcb:(Int)->Unit) {
            state.update {
                stopcb(i)
            }
        }
    }
    data class TimerLineState(var time:Int,var t:Int = 0) {
        fun update(stopcb:()->Unit) {
            if(t == time) {
                stopcb()
                return
            }
            t++

        }
        fun executeFn(cb:(Float)->Unit) {
            cb(t.toFloat()/time.toFloat())
        }
    }
    data class CircleIndicator(var i:Int) {
        fun draw(canvas: Canvas,paint: Paint,deg:Float,w:Float,h:Float) {
            paint.color = Color.parseColor(colors[i% colors.size])
            canvas.drawArc(RectF(w/2-h/20,0f,w/2+h/20,h/10),i*deg,deg,false,paint)
        }
    }
    data class TimerLineContainer(var w:Float,var h:Float,var times:LinkedList<Int>,var j:Int = 0) {
        val timers:ConcurrentLinkedQueue<TimerLine> = ConcurrentLinkedQueue()
        val circles:ConcurrentLinkedQueue<CircleIndicator> = ConcurrentLinkedQueue()
        init {
            val gap = (0.9f*h)/(times.size+1)
            var y = gap
            var i = 0
            times.forEach {
                timers.add(TimerLine(it,i,y))
                y += gap
                i++
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            timers.forEach {
                it.draw(canvas,paint,w)
            }
            val pieScale = (timers?.at(j)?.state?.t?.toFloat()?:0f)/(timers?.at(j)?.state?.time?.toFloat()?:0f)
            if(j < timers.size && timers.size>0) {
                val gap = 360f/timers.size
                paint.style = Paint.Style.STROKE
                circles.forEach {
                    it.draw(canvas,paint,gap,w,h)
                }
                paint.color = Color.parseColor(colors[j% colors.size])
                canvas.drawArc(RectF(w/2-h/20,0f,w/2+h/20,h/10),j*gap,gap*pieScale,false,paint)
            }
        }
        fun update(stopcb: (Int) -> Unit,renderStop:()->Unit) {
            if(j < timers.size) {
                timers.at(j)?.update{
                    circles.add(CircleIndicator(j))
                    j++
                    stopcb(it)
                    if(j == timers.size) {
                        renderStop()
                    }
                }
            }
        }
    }
    data class TimerLineRenderer(var view:TimerLineView,var time:Int = 0) {
        var container:TimerLineContainer?=null
        var running = true
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                container = TimerLineContainer(w,h,view.times)
            }
            container?.draw(canvas,paint)
            container?.update({
                view.onTimerFinishListener?.onFinishListener?.invoke(it)
            },{
                running = false
            })
            time++
            if(running) {
                try {
                    Thread.sleep(1000)
                    view.invalidate()
                } catch(ex: Exception) {

                }
            }
        }
    }
    companion object {
        fun create(activity:Activity):TimerLineView {
            val view = TimerLineView(activity)
            return view
        }
    }
    data class OnTimerFinishListener(var onFinishListener:(Int)->Unit)
}
fun ConcurrentLinkedQueue<TimerLineView.TimerLine>.at(i:Int):TimerLineView.TimerLine? {
    var index = 0
    this.forEach {
        if(index == i) {
            return it
        }
        index++
    }
    return null
}