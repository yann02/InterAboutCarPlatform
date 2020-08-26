package com.hnsh.dialogue.jet.modules.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.NinePatchDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat

import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cn.onestravel.one.navigation.BuildConfig

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

import cn.onestravel.one.navigation.R
import cn.onestravel.one.navigation.utils.DensityUtils
import kotlin.math.sqrt

/**
 * @author onestravel
 * @createTime 2019/1/20 9:48 AM
 * @description 可以凸起的底部导航菜单VIEW
 */

typealias OnItemSelectedListener = (item: OneBottomNavigationBar.Item, position: Int) -> Unit

class OneBottomNavigationBar : View {
    private val TAG = "BottomNavigationBar"

    // 导航菜单键列表
    private var itemList: MutableList<Item> = ArrayList()

    //总宽度 width
    private var mWidth = 0

    //总高度 height
    private var mHeight = 0

    //每个菜单的宽度 item width
    private var mItemWidth = 0

    //每个菜单的告诉 item height
    private var mItemHeight = 0

    //整体的上边距
    private var topPadding = DensityUtils.dpToPx(resources, 3f)

    //整体下边距
    private var bottomPadding = DensityUtils.dpToPx(resources, 3f)

    //文字相对于图标的边距
    private var textTop = DensityUtils.dpToPx(resources, 3f)

    //画笔
    private val mTextPaint: Paint by lazy { Paint() }

    //图标的状态颜色列表
    private var itemIconTintRes: ColorStateList? = null

    //文字的状态颜色列表
    private var itemColorStateList: ColorStateList? = null

    //Item菜单的选中事件
    private var onItemSelectedListener: OnItemSelectedListener? = null

    // 当前选中的坐标位置
    private var checkedPosition = 0

    //是否开启上浮
    private var floatingEnable: Boolean = false

    //上浮距离
    private var floatingUp: Int = 0

    //背景资源
    private var bgDrawable: Drawable? = null

    //菜单的布局文件
    @MenuRes
    private var menuRes: Int = 0
    private var titleSize: Int = 0
    private var itemIconWidth: Int = 0
    private var itemIconHeight: Int = 0
    private var itemPadding: Int = 0
    private var itemFloatingPadding: Int = 0
    private val fragmentMap: MutableMap<Int, Fragment> = HashMap<Int, Fragment>()
    private var manager: FragmentManager? = null
    private var containerView: View? = null
    private var currentFragment: Fragment? = null
    /**
     * 是否替换Fragment,替换后Fragment 数据会清空
     *
     * @return
     */
    /**
     * 设置是否替换Fragment,替换后Fragment 数据会清空
     *
     * @param replace
     */
    var isReplace: Boolean = false
    private var linePaint: Paint? = null
    private val lineWidth by lazy { DensityUtils.dpToPx(context, 1f).toFloat() }
    private var topLineColor: Int = 0
    private final val DEFAULT_MSG_COUNT_TEXT_PADDING: Int = DensityUtils.dpToPx(resources, 2f)

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }


    /**
     * 设置选中的监听事件
     *
     * @param onItemSelectedListener
     */
    fun setOnItemSelectedListener(onItemSelectedListener: OnItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener
    }

    /**
     * 设置选中
     *
     * @param position 选中位置
     */
    fun setSelected(position: Int) {
        val item = itemList[position]
        if (item.isCheckable) {
            if (checkedPosition >= 0) {
                itemList[checkedPosition].isChecked = false
            }
            item.isChecked = true
            checkedPosition = position
        }
        postInvalidate()
        if (onItemSelectedListener != null) {
            onItemSelectedListener!!.invoke(item, position)
        }
        try {
            if (manager == null) {
                throw RuntimeException("FragmentManager is null,please use setFragmentManager(getFragmentManager(),fragmentContainerView) in Activity")
            }
            if (containerView == null) {
                throw RuntimeException("fragmentContainerView is null,please use setFragmentManager(getFragmentManager(),fragmentContainerView) set Fragment's ContainerView")
            }
            if (containerView !is ViewGroup) {
                throw RuntimeException("fragmentContainerView is not viewGroup ")
            }
            if (containerView!!.id == View.NO_ID) {
                throw RuntimeException("fragmentContainerView not id")
            }
            if (item.showFragment) {
                val fragment = fragmentMap[item.id]
                        ?: throw RuntimeException("[" + item.id + "] fragment is null ")
                if (item.isCheckable){
                    selectFragment(fragment)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 设置未读消息数
     *
     * @param position 未读消息数的位置
     * @param count    未读消息数量 <0 是显示为小红点，没有数字
     * == 0  时不显示未读消息红点
     * >0 && <100 时显示对应的消息数量
     * >=100 时显示 99+
     */
    fun setMsgCount(position: Int, count: Int) {
        if (position < itemList.size) {
            itemList[position].msgCount = count
            postInvalidate()
        }
    }


    /**
     * 设置Menu 菜单资源文件
     *
     * @param menuRes
     */
    fun setMenu(@MenuRes menuRes: Int) {
        this.menuRes = menuRes
        parseXml(menuRes)
        format()
        postInvalidate()
    }

    /**
     * 设置Item 菜单的图标颜色状态列表
     *
     * @param resId 图标颜色状态的资源文件
     */
    fun setItemIconTint(@DrawableRes @ColorRes resId: Int) {
        this.itemIconTintRes = ResourcesCompat.getColorStateList(resources, resId, null)
        parseXml(menuRes)
        format()
        postInvalidate()
    }

    /**
     * 设置Item 菜单的文字颜色状态列表
     *
     * @param resId 文字颜色状态的资源文件
     */
    fun setItemColorStateList(@DrawableRes @ColorRes resId: Int) {
        this.itemColorStateList = ResourcesCompat.getColorStateList(resources, resId, null)
        postInvalidate()
    }


    /**
     * 设置分割线颜色
     * @param color
     */
    fun setTopLineColor(@ColorInt color: Int) {
        this.topLineColor = color
    }


    /**
     * 设置分割线颜色
     * @param colorRes
     */
    fun setTopLineColorRes(@ColorRes colorRes: Int) {
        this.topLineColor = resources.getColor(colorRes)
    }


    /**
     * 设置是否开启浮动
     *
     * @param floatingEnable
     */
    fun setFloatingEnable(floatingEnable: Boolean) {
        this.floatingEnable = floatingEnable
        postInvalidate()
    }

    /**
     * 设置上浮距离，不能超过导航栏高度的1/2
     *
     * @param floatingUp
     */
    fun setFloatingUp(floatingUp: Int) {
        this.floatingUp = floatingUp
        postInvalidate()
    }

    /**
     * 设置FragmentManager，管理fragment
     *
     * @param fragmentManager       fragment管理
     * @param fragmentContainerView fragment 将要添加的view
     */
    fun setFragmentManager(fragmentManager: FragmentManager, fragmentContainerView: View) {
        this.manager = fragmentManager
        this.containerView = fragmentContainerView
    }

    /**
     * 添加Fragment
     *
     * @param tabId
     * @param fragment
     */
    fun addFragment(@IdRes tabId: Int, fragment: Fragment) {
        fragmentMap[tabId] = fragment
    }

    /**
     * 获取布局参数
     *
     * @return
     */
    override fun getLayoutParams(): ViewGroup.LayoutParams {
        return super.getLayoutParams()
    }

    /**
     * 设置图标大小
     *
     * @param itemIconWidth
     * @param itemIconHeight
     */
    fun setItemIconSize(itemIconWidth: Int, itemIconHeight: Int) {
        this.itemIconWidth = itemIconWidth
        this.itemIconHeight = itemIconHeight
        format()
        postInvalidate()
    }

    /**
     * 设置Title文字大小
     *
     * @param titleSize
     */
    fun setTitleSize(titleSize: Int) {
        this.titleSize = titleSize
        format()
        postInvalidate()
    }

    /**
     * 设置图标和title文字间距
     *
     * @param textTopMargin
     */
    fun setTextTopMargin(textTopMargin: Int) {
        this.textTop = textTopMargin
        postInvalidate()
    }

    /**
     * 设置布局参数
     *
     * @param params
     */
    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        if (floatingEnable) {
            val floatingUp = getFloatingUpHeight()
            when (params) {
                is LinearLayout.LayoutParams -> {
                    params.topMargin = params.topMargin - floatingUp
                }
                is RelativeLayout.LayoutParams -> {
                    params.topMargin = params.topMargin - floatingUp
                }
                is FrameLayout.LayoutParams -> {
                    params.topMargin = params.topMargin - floatingUp
                }
                is ViewGroup.MarginLayoutParams -> {
                    params.topMargin = params.topMargin - floatingUp
                }
            }
        }
        super.setLayoutParams(params)
    }


    /**
     * 初始化，获取该View的自定义属性，以及item 列表
     *
     * @param context      上下文
     * @param attrs        属性
     * @param defStyleAttr 默认样式
     */
    @SuppressLint("ResourceType")
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.One_StyleBottomLayout)
            itemIconTintRes = ta.getColorStateList(R.styleable.One_StyleBottomLayout_oneItemIconTint)
            itemColorStateList = ta.getColorStateList(R.styleable.One_StyleBottomLayout_oneItemTextColor)
            if (itemColorStateList == null) {
                itemColorStateList = ResourcesCompat.getColorStateList(resources, R.drawable.default_blue_tab_tint, null)
            }
            topLineColor = ta.getColor(R.styleable.One_StyleBottomLayout_oneItemTopLineColor, Color.TRANSPARENT)
            floatingEnable = ta.getBoolean(R.styleable.One_StyleBottomLayout_oneFloatingEnable, false)
            floatingUp = ta.getDimension(R.styleable.One_StyleBottomLayout_oneFloatingUp, 20f).toInt()
            titleSize = ta.getDimension(R.styleable.One_StyleBottomLayout_oneItemTextSize, DensityUtils.spToPx(resources, 12f).toFloat()).toInt()
            textTop = ta.getDimension(R.styleable.One_StyleBottomLayout_oneItemTextTopMargin, DensityUtils.dpToPx(resources, 3f).toFloat()).toInt()
            itemIconWidth = ta.getDimension(R.styleable.One_StyleBottomLayout_oneItemIconWidth, 0f).toInt()
            itemIconHeight = ta.getDimension(R.styleable.One_StyleBottomLayout_oneItemIconHeight, 0f).toInt()
            itemPadding = ta.getDimension(R.styleable.One_StyleBottomLayout_oneItemPadding, 0f).toInt()
            itemFloatingPadding = ta.getDimension(R.styleable.One_StyleBottomLayout_oneFloatingPadding, 0f).toInt()
            val xmlRes = ta.getResourceId(R.styleable.One_StyleBottomLayout_oneMenu, 0)
            parseXml(xmlRes)
        }
        format()
    }

    /**
     * 处理数据
     */
    private fun format() {
        if (itemList.size > 5) {
            itemList = itemList.subList(0, 5)
        }
        if (getBackground() != null && getBackground() is ColorDrawable) {
            bgDrawable = getBackground()
        } else if (getBackground() is StateListDrawable) {
            bgDrawable = getBackground()
        } else if (getBackground() is GradientDrawable) {
            bgDrawable = getBackground()
        } else {
            bgDrawable = ColorDrawable(Color.WHITE)
        }
        for (item in itemList) {
            item.titleSize = titleSize
            item.iconWidth = itemIconWidth
            item.iconHeight = itemIconHeight
            if (item.isFloating) {
                item.padding = itemFloatingPadding
            } else {
                item.padding = itemPadding;
            }
        }
        linePaint = createPaint(topLineColor)
        linePaint!!.strokeWidth = lineWidth
    }

    /**
     * 解析 menu 的 xml 的文件，得到相关的 导航栏菜单
     *
     * @param xmlRes
     */
    private fun parseXml(xmlRes: Int) {
        try {
            if (xmlRes == 0) {
                return
            }
            itemList.clear()
            val xmlParser = resources.getXml(xmlRes)
            var event = xmlParser.eventType   //先获取当前解析器光标在哪
            while (event != XmlPullParser.END_DOCUMENT) {    //如果还没到文档的结束标志，那么就继续往下处理
                when (event) {
                    XmlPullParser.START_DOCUMENT -> {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "xml解析开始")
                        }
                    }
                    XmlPullParser.START_TAG ->
                        //一般都是获取标签的属性值，所以在这里数据你需要的数据
                        //                        Log.e(TAG, "当前标签是：" + xmlParser.getName());
                        if (xmlParser.name == "item") {
                            val item = Item()
                            for (i in 0 until xmlParser.attributeCount) {
                                //两种方法获取属性值
                                //                                Log.e(TAG, "第" + (i + 1) + "个属性：" + xmlParser.getAttributeName(i)
                                //                                        + ": " + xmlParser.getAttributeValue(i));
                                if ("id".equals(xmlParser.getAttributeName(i), ignoreCase = true)) {
                                    item.id = xmlParser.getAttributeResourceValue(i, 0)
                                } else if ("icon".equals(xmlParser.getAttributeName(i), ignoreCase = true)) {
                                    val drawableId = xmlParser.getAttributeResourceValue(i, 0)
                                    val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
                                    item.drawable = drawable!!.constantState!!.newDrawable()
                                    var stateListDrawable = StateListDrawable()
                                    if (drawable is StateListDrawable) {
                                        stateListDrawable = drawable
                                        stateListDrawable.state = intArrayOf(android.R.attr.state_checked)
                                        stateListDrawable.mutate()
                                    } else {
                                        val selectedDrawable = tintListDrawable(drawable, itemIconTintRes)
                                        selectedDrawable.state = intArrayOf(android.R.attr.state_checked)
                                        stateListDrawable.addState(intArrayOf(android.R.attr.state_checked), selectedDrawable.current)
                                        stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selectedDrawable.current)
                                        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), selectedDrawable.current)
                                        stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), selectedDrawable.current)
                                        selectedDrawable.state = intArrayOf()
                                        stateListDrawable.addState(intArrayOf(), selectedDrawable.current)
                                    }
                                    item.icon = stateListDrawable
                                } else if ("title".equals(xmlParser.getAttributeName(i))) {
                                    val titleId = xmlParser.getAttributeResourceValue(i, 0)
                                    if (titleId > 0) {
                                        item.title = resources.getString(titleId)
                                    } else {
                                        item.title = xmlParser.getAttributeValue(i)
                                    }
                                } else if ("floating".equals(xmlParser.getAttributeName(i))) {
                                    item.isFloating = xmlParser.getAttributeBooleanValue(i, false)
                                } else if ("checked".equals(xmlParser.getAttributeName(i))) {
                                    item.isChecked = xmlParser.getAttributeBooleanValue(i, false)
                                } else if ("checkable".equals(xmlParser.getAttributeName(i))) {
                                    item.isCheckable = xmlParser.getAttributeBooleanValue(i, false)
                                } else if ("showFragment".equals(xmlParser.getAttributeName(i))) {
                                    item.showFragment = xmlParser.getAttributeBooleanValue(i, true)
                                }
                            }
                            if (item.isCheckable && item.isChecked) {
                                checkedPosition = itemList.size
                            }
                            itemList.add(item)
                        }
                    XmlPullParser.TEXT -> {
                    }
                    XmlPullParser.END_TAG -> {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "xml解析结束")
                        }
                    }
                    else -> {
                    }
                }//                        Log.e(TAG, "Text:" + xmlParser.getText());
                event = xmlParser.next()   //将当前解析器光标往下一步移
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    /**
     * 当初始化布局以后，进行默认选中
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        setSelected(checkedPosition)
    }

    /**
     * 尺寸测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specMode = MeasureSpec.getMode(heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mItemWidth = (mWidth - paddingLeft - paddingRight) / itemList.size
        topPadding = paddingTop
        bottomPadding = paddingBottom
        setTextPaint(titleSize, Color.BLACK)
        val textHeight = getTextHeight("首页", mTextPaint)
        mHeight = if (specMode == View.MeasureSpec.AT_MOST) {
            itemIconHeight = if (itemIconHeight < 50) itemIconHeight else 50
            topPadding + bottomPadding + itemIconHeight + textHeight + textTop + itemPadding * 2
        } else {
            val height = MeasureSpec.getSize(heightMeasureSpec)
            this.itemIconHeight = height - topPadding - bottomPadding - textHeight - textTop - itemPadding * 2
            height
        }
        this.itemIconWidth = this.itemIconHeight
        mHeight += getFloatingUpHeight()
        mItemHeight = mHeight
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, specMode))
        layoutParams = layoutParams
    }


    /**
     * 进行绘制
     *
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var floatingUpInt = getFloatingUpHeight()
        canvas.drawLine(0f, floatingUpInt.toFloat(), mWidth.toFloat(), floatingUpInt.toFloat(), linePaint!!)
        //画背景
        drawFloating(canvas)

        val rectInit = Rect()
        rectInit.set(0, (floatingUpInt + lineWidth/2).toInt(), mWidth, mHeight)
        bgDrawable!!.bounds = rectInit
        bgDrawable!!.draw(canvas)

        //画Floating
        //画出所有导航菜单
        if (itemList.size > 0) {
            for (i in itemList.indices) {
                val item = itemList[i]
                drawItem(canvas, item, i)
            }
        }

    }

    /**
     * 画出上浮图标的背景
     *
     * @param canvas
     */
    private fun drawFloating(canvas: Canvas) {
        if (!floatingEnable) {
            return
        }
        if (itemList.size > 0) {
            for (i in itemList.indices) {
                val item = itemList[i]
                if (item.isFloating) {
                    var rect = getIconRect(item, i)
                    val x = (rect.left + rect.right) / 2
                    val y = (rect.top + rect.bottom) / 2
                    val r = y
                    val paint = createPaint(Color.WHITE)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        paint.colorFilter = bgDrawable!!.colorFilter
                    }
                    linePaint!!.style = Paint.Style.STROKE
                    canvas.drawCircle(x.toFloat(), y.toFloat(), (r + DensityUtils.dpToPx(context, 1f) / 2).toFloat(), linePaint!!)
                    paint.style = Paint.Style.FILL
                    canvas.drawCircle(x.toFloat(), y.toFloat(), r.toFloat(), paint)
                }
            }
        }
    }

    /**
     * 获取内容区域位置
     *
     */
    private fun getItemRect(item: Item, position: Int): Rect {
        val rect = Rect()
        rect.left = paddingLeft + position * mItemWidth
        if (floatingEnable && item.isFloating) {
            rect.top = paddingTop + item.padding + getFloatingUpHeight() / 4
        } else {
            rect.top = paddingTop + getFloatingUpHeight() + item.padding
        }
        rect.right = rect.left + mItemWidth
        rect.bottom = mItemHeight - paddingBottom - item.padding
        return rect
    }

    /**
     * 画出每一个Item导航菜单
     *
     * @param canvas
     * @param item
     */
    private fun drawItem(canvas: Canvas, item: Item?, position: Int) {
        if (item == null) {
            return
        }
        var rect = getItemRect(item, position)
        if (!TextUtils.isEmpty(item.title)) {
            var color = if (item.isChecked) itemColorStateList!!.getColorForState(intArrayOf(android.R.attr.state_checked), itemColorStateList!!.defaultColor) else itemColorStateList!!.defaultColor
            if (!item.isCheckable) {
//                color = itemColorStateList!!.getColorForState(intArrayOf(android.R.attr.state_checked), itemColorStateList!!.defaultColor)
                color = itemColorStateList!!.defaultColor
            }
            setTextPaint(if (item.titleSize == 0) DensityUtils.dpToPx(resources, 14f) else item.titleSize, color)
            val textHeight = getTextHeight(item.title, mTextPaint)
            val textX = (rect.left + rect.right) / 2
            val textY = rect.bottom - textHeight / 4
            canvas.drawText(item.title!!, textX.toFloat(), textY.toFloat(), mTextPaint)
        }
        if (item.icon != null) {
            val drawable: Drawable? = getIconDrawable(item, position)
            drawable?.let {
                it.draw(canvas)
            }
        }
        if (item.msgCount != 0) {
            drawItemMsgCount(item, position, canvas)
        }
    }

    /**
     * 画出消息数
     */
    private fun drawItemMsgCount(item: Item, position: Int, canvas: Canvas) {
        val msgCountRect = getMsgCountRect(item, position)
        var x = (msgCountRect.left + msgCountRect.right) / 2
        var y = (msgCountRect.top + msgCountRect.bottom) / 2
        var r = (msgCountRect.bottom - msgCountRect.top) / 2

        if (item.msgCount > 0) {
            var countStr = when {
                item.msgCount > 99 -> {
                    setTextPaint(DensityUtils.dpToPx(resources, 7f), Color.WHITE)
                    "99+"
                }
                item.msgCount < 10 -> {
                    setTextPaint(DensityUtils.dpToPx(resources, 9f), Color.WHITE)
                    item.msgCount.toString()
                }
                else -> {
                    setTextPaint(DensityUtils.dpToPx(resources, 8f), Color.WHITE)
                    item.msgCount.toString()
                }
            }
            val paint = createPaint(Color.RED)
            canvas.drawCircle(x.toFloat(), y.toFloat(), r.toFloat(), paint)
            canvas.drawText(countStr, x.toFloat(), (y + (r - DEFAULT_MSG_COUNT_TEXT_PADDING) / 2).toFloat(), mTextPaint)
            paint.style = Paint.Style.STROKE
            paint.color = Color.WHITE
            paint.strokeWidth = DensityUtils.dpToPx(resources, 1f).toFloat()
            canvas.drawCircle(x.toFloat(), y.toFloat(), r.toFloat(), paint)
        } else {
            val paint = createPaint(Color.RED)
            canvas.drawCircle(x.toFloat(), y.toFloat(), r.toFloat(), paint)
            paint.style = Paint.Style.STROKE
            paint.color = Color.WHITE
            paint.strokeWidth = DensityUtils.dpToPx(resources, 1f).toFloat()
            canvas.drawCircle(x.toFloat(), y.toFloat(), r.toFloat(), paint)
        }
    }

    private fun getMsgCountRect(item: Item, position: Int): Rect {
        val r = if (item.msgCount > 0) {
            setTextPaint(DensityUtils.dpToPx(resources, 7f), Color.WHITE)
            getTextWidth("99+", mTextPaint!!) / 2 + DEFAULT_MSG_COUNT_TEXT_PADDING
        } else {
            7
        }
        val rect = getIconRect(item, position)
        var x = (rect.left + rect.right) / 2
        var y = (rect.top + rect.bottom) / 2
        var r2 = y - rect.top
        val offset = sqrt(r2 * r2 * 1.0 / 2)
        val msgRect = Rect()
        msgRect.left = (x + offset - r).toInt()
        msgRect.top = (y - offset - r).toInt()
        if (item.msgCount > 0) {
            msgRect.left += r / 3
            msgRect.top += r / 3
        }
        msgRect.right = msgRect.left + 2 * r
        msgRect.bottom = msgRect.top + 2 * r
        return msgRect
    }


    private fun getFloatingUpHeight(): Int {
        if (floatingEnable) {
            for (item in itemList) {
                if (item.isFloating) {
                    return floatingUp
                }
            }
        }
        return 0
    }

    private fun getIconDrawable(item: Item, position: Int): Drawable? {
        val drawable: Drawable? = if (item.isCheckable) {
            if (item.isChecked) {
                item.icon!!.state = intArrayOf(android.R.attr.state_checked)
                item.icon!!.current
            } else {
                item.icon!!.state = intArrayOf()
                item.icon!!.current
            }
        } else {
            item.drawable
        }
        drawable?.let {
            it.bounds = getIconRect(item, position)
        }
        return drawable
    }

    /**
     * 获取图标的位置
     */
    private fun getIconRect(item: Item, position: Int): Rect {
        val rect = getItemRect(item, position)
        val to = Rect()
        val floating = getFloatingUpHeight()
        var iconSize = itemIconWidth.coerceAtMost(itemIconHeight)
        setTextPaint(titleSize, Color.BLACK)
        val textHeight = getTextHeight("首页", mTextPaint!!)
        if (TextUtils.isEmpty(item.title)) {
            iconSize += (textTop + textHeight)
        }
        to.top = rect.top
        if (floatingEnable && item.isFloating) {
            iconSize += floating * 3 / 4
        }
        iconSize += (itemPadding - item.padding) * 2//高度修复
        to.left = (rect.left + rect.right - iconSize) / 2
        to.right = to.left + iconSize
        to.bottom = to.top + iconSize
        return to
    }


    /**
     * 创建文字类型的画笔
     *
     * @param textSize  文字大小
     * @param textColor 文字颜色
     * @return
     */
    private fun setTextPaint(textSize: Int, textColor: Int): Paint {
        return mTextPaint?.apply {
            this.color = textColor//设置画笔的颜色
            this.textSize = textSize.toFloat()//设置文字大小
            this.isAntiAlias = true//设置抗锯齿功能 true表示抗锯齿 false则表示不需要这功能
            this.textAlign = Paint.Align.CENTER
            this.style = Paint.Style.FILL_AND_STROKE
        }
    }

    /**
     * 创建图形的画笔
     *
     * @param color 画笔颜色
     * @return
     */
    private fun createPaint(color: Int): Paint {
        return Paint().apply {
            this.color = color
            this.isAntiAlias = true//设置抗锯齿功能 true表示抗锯齿 false则表示不需要这功能
            this.textAlign = Paint.Align.CENTER
            this.style = Paint.Style.FILL
        }
    }


    /**
     * 获取文字宽度
     *
     * @param text  文字
     * @param paint 画笔
     * @return
     */
    private fun getTextWidth(text: String, paint: Paint): Int {
        val rect = Rect() // 文字所在区域的矩形
        paint.getTextBounds(text, 0, text.length, rect)
        return rect.width()
    }

    /**
     * 获取文字高度
     *
     * @param text  文字
     * @param paint 画笔
     * @return
     */
    private fun getTextHeight(text: String?, paint: Paint): Int {
        val rect = Rect()
        paint.getTextBounds(text, 0, text!!.length, rect)
        return rect.height()
    }


    /**
     * 更改图片颜色
     *
     * @param drawable
     * @param colors
     * @return
     */
    private fun tintListDrawable(drawable: Drawable, colors: ColorStateList?): Drawable {
        return if (colors != null) {
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.MULTIPLY)
            DrawableCompat.setTintList(wrappedDrawable, colors)
            wrappedDrawable
        } else {
            drawable
        }
    }

    /**
     * 触摸事件监听
     *
     * @param event
     * @return
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX.toDouble()
        var y = event.rawY.toDouble()
        //获取控件在屏幕的位置
        val location = IntArray(2)
        getLocationOnScreen(location)
        val locationY = location[1]
        y -= locationY
        val action = event.action
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "action = $action")
        }
        when (action) {
            MotionEvent.ACTION_DOWN -> return true
            //                break;
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                for (i in itemList.indices) {
                    val item = itemList[i]
                    var startTop = 0
                    if (!item.isFloating) {
                        startTop = paddingTop + getFloatingUpHeight()
                    }
                    if (x > paddingLeft + mItemWidth * i && x < paddingLeft + mItemWidth * (i + 1)) {
                        //图片文字内容宽度
                        val width = mItemHeight - topPadding - bottomPadding
                        //图片文字内容高度
                        val height = mItemHeight - topPadding - bottomPadding
                        if (!TextUtils.isEmpty(item.title)) {
                            val color = if (item.isChecked) itemColorStateList!!.getColorForState(intArrayOf(android.R.attr.state_checked), itemColorStateList!!.defaultColor) else itemColorStateList!!.defaultColor
                            setTextPaint(if (item.titleSize == 0) DensityUtils.dpToPx(resources, 14f) else item.titleSize, color)
                            val textHeight = getTextHeight(item.title, mTextPaint!!)
                            val textY = startTop + height - textHeight / 4//上边距+图片文字内容高度
                            val w = textY - textHeight / 2 - topPadding
                            //                        width = height = height - textHeight - textTop;
                        }
                        val centerX = paddingLeft + i * mItemWidth + (mItemWidth - width) / 2 + width / 2
                        val centerY = mItemHeight / 2
                        val r = mItemHeight / 2
                        if (y >= getFloatingUpHeight() || item.isFloating && isInCircle(centerX, centerY, r, x.toInt(), y.toInt())) {
                            setSelected(i)
                        }
                    }
                }
                postInvalidate()
            }
        }
        return super.onTouchEvent(event)
    }


    /**
     * 判断触摸位置是否在圆形内部
     *
     * @param vCenterX 圆形的 X 坐标
     * @param vCenterY 圆形的 Y 坐标
     * @param r        圆形的半径
     * @param touchX   触摸位置的 X 坐标
     * @param touchY   触摸位置的 Y 坐标
     * @return
     */
    private fun isInCircle(vCenterX: Int, vCenterY: Int, r: Int, touchX: Int, touchY: Int): Boolean {
        //点击位置x坐标与圆心的x坐标的距离
        val distanceX = Math.abs(vCenterX - touchX)
        //点击位置y坐标与圆心的y坐标的距离
        val distanceY = Math.abs(vCenterY - touchY)
        //点击位置与圆心的直线距离
        val distanceZ = Math.sqrt(Math.pow(distanceX.toDouble(), 2.0) + Math.pow(distanceY.toDouble(), 2.0)).toInt()

        //如果点击位置与圆心的距离大于圆的半径，证明点击位置没有在圆内
        return distanceZ <= r
    }

    /**
     * 导航菜单Item 的实体
     */
    inner class Item {
        var showFragment: Boolean = true
        var id: Int = 0
        var icon: StateListDrawable? = null
        var drawable: Drawable? = null
        var title: String? = null
        var titleSize: Int = 0
        var iconWidth: Int = 0
        var iconHeight: Int = 0
        var padding: Int = 0
        var isFloating = false
        var isChecked = false
        var isCheckable = true
        var msgCount = 0
    }


    internal fun drawable2Bitmap(drawable: Drawable?): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable is NinePatchDrawable) {
            val bitmap = Bitmap
                    .createBitmap(
                            drawable.intrinsicWidth,
                            drawable.intrinsicHeight,
                            if (drawable.opacity != PixelFormat.OPAQUE)
                                Bitmap.Config.ARGB_8888
                            else
                                Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth,
                    drawable.intrinsicHeight)
            drawable.draw(canvas)
            return bitmap
        } else {
            val bitmap: Bitmap
            if (drawable!!.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }

    /**
     * 改变Fragment
     *
     * @param to
     */
    private fun selectFragment(to: Fragment) {
        if (!isReplace) {
            hiddenFragment(currentFragment, to)
        } else {
            replaceFragment(currentFragment, to)
        }

    }

    /**
     * 隐藏Fragment
     *
     * @param from
     * @param to
     */
    private fun hiddenFragment(from: Fragment?, to: Fragment) {
        val transaction = manager!!.beginTransaction()
        if (from != null && from !== to) {
            if (to.isAdded) {
                transaction.hide(from).show(to)
            } else {
                transaction.add(containerView!!.id, to).hide(from).show(to)
            }
        } else if (to != null) {
            transaction.replace(containerView!!.id, to).show(to)
        }
        transaction.commit()
        currentFragment = to
    }

    /**
     * 替换Fragment
     *
     * @param from
     * @param to
     */
    private fun replaceFragment(from: Fragment?, to: Fragment) {
        val transaction = manager!!.beginTransaction()
        if (from != null && from !== to) {
            transaction.remove(from)
            transaction.replace(containerView!!.id, to).show(to)
        } else if (to != null) {
            transaction.replace(containerView!!.id, to).show(to)
        }
        transaction.commit()
        currentFragment = to
    }
}