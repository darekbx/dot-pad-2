package com.dotpad2.dots

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.AdapterView
import com.dotpad2.model.Dot
import android.graphics.DashPathEffect



class DotBoardView(context: Context, attrs: AttributeSet?)
    : AdapterView<DotAdapter>(context, attrs) {

    companion object {
        val DRAG_PADDING = 5f
    }

    private lateinit var dotAdapter: DotAdapter

    var openDotCallback: ((dot: Dot) -> Unit)? = null
    var deleteDotCallback: ((dot: Dot) -> Unit)? = null
    var newDotCallback: ((position: Point) -> Unit)? = null

    private var dragPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = DRAG_PADDING
        pathEffect = setPathEffect(DashPathEffect(floatArrayOf(40f, 40f), 10f))
    }

    private var isDragDropEnabled = false

    init {
		setWillNotDraw(false)
    }

    override fun getAdapter(): DotAdapter = dotAdapter

    override fun setAdapter(adapter: DotAdapter?) {
        adapter?.let {
            this.dotAdapter = it
            refresh()
        }
    }

    override fun getSelectedView() = null

    override fun setSelection(position: Int) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        markDragDropActive(canvas)
    }

    private fun markDragDropActive(canvas: Canvas?) {
        if (isDragDropEnabled) {
            canvas?.drawRect(
                DRAG_PADDING, DRAG_PADDING,
                width - DRAG_PADDING * 2, height - DRAG_PADDING * 2,
                dragPaint
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed || childCount == 0) {
            for (dotIndex in 0 until adapter.count) {
                addDotToLayout(dotIndex)
            }
            invalidate()
        }
    }

    fun refresh() {
		removeAllViewsInLayout()
		requestLayout()
    }

    private fun addDotToLayout(dotIndex: Int) {
        val dot = adapter.getItem(dotIndex)
        val dotView = (adapter.getView(dotIndex, null, this) as DotView).apply {
            dotSize = dot.size

            setOnClickListener { openDot(dot) }
            setOnLongClickListener {
                deleteDot(dot)
                true
            }
        }

        with(dotView) {
            val dotPosition = dot.position
            measure(-1, -1)
            layout(
                dotPosition.x,
                dotPosition.y,
                dotPosition.x + measuredWidth,
                dotPosition.y + measuredHeight
            )
            addViewInLayout(this, dotIndex, null, true)
        }
    }

    private fun openDot(dot: Dot) {
        if (isDragDropEnabled) {
            return
        }
        openDotCallback?.invoke(dot)
    }

    private fun deleteDot(dot: Dot) {
        if (isDragDropEnabled) {
            return
        }
        deleteDotCallback?.invoke(dot)
    }

    private fun notifyDragDropEnabled() {
        for (dotIndex in 0 until childCount) {
            val child = getChildAt(dotIndex)
            if (child is DotView) {
                child.isDragDropEnabled = isDragDropEnabled
            }
        }
    }

    val gestureDetector = GestureDetector(context, object : GestureDetector.OnGestureListener {

        override fun onShowPress(e: MotionEvent?) {
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            isDragDropEnabled = !isDragDropEnabled
            notifyDragDropEnabled()
            invalidate()
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            e?.run {
                val position = Point(x.toInt(), y.toInt())
                newDotCallback?.invoke(position)
            }
            return false
        }
    })
}