package com.sunny.lib.common.font

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.LayoutInflaterCompat
import com.sunny.lib.base.utils.FontUtils
import timber.log.Timber

object PaxFontHelper {

    /**
     * 方式二，setFactory2
     */
    fun byFactory(actContext: AppCompatActivity) {
//        byFactory(actContext, FontUtils.getFont())
    }

    fun byFactory(actContext: AppCompatActivity, toFont: Int) {
        var index = 0

        LayoutInflaterCompat.setFactory2(
            actContext.layoutInflater,
            object : LayoutInflater.Factory2 {
                override fun onCreateView(
                    parent: View?, name: String, context: Context, attrs: AttributeSet
                ): View? {
                    var view: View? = null
                    Timber.i("onCreateView(${index++}) >>> $name")
                    if (1 == name.indexOf(".")) {
                        //表示自定义 View，通过反射创建
                        view = actContext.layoutInflater.createView(name, null, attrs)
                        Timber.i("layoutInflater.createView >>> $view")
                    }

                    if (view == null) {
                        //解决Xml中直接使用AppCompatXXX控件导致createView失败的问题
                        val newName = when (name) {
                            "androidx.appcompat.widget.AppCompatTextView" -> "TextView"
                            "androidx.appcompat.widget.AppCompatButton" -> "Button"
                            "androidx.appcompat.widget.AppCompatEditText" -> "EditText"
                            else -> name
                        }
                        //通过系统创建一系列 appcompat 的 View
                        view = actContext.delegate.createView(parent, newName, context, attrs)
                        Timber.i("delegate.createView >>> $view")
                    }

                    FontUtils.changeDefaultFont(view, toFont)
                    return view
                }

                override fun onCreateView(
                    name: String,
                    context: Context,
                    attrs: AttributeSet
                ): View? {
                    return null
                }
            })
    }


    /**
     * 在application中执行
     * 方式4：第2步：编写反射方法全局替换默认字体
     *
     * @param mContext 上下文
     */
    @JvmStatic
    fun changeDefaultFont(mContext: Context) {
        try {
            val typeface = ResourcesCompat.getFont(mContext, FontUtils.getFont())
            Timber.i("typeface :$typeface")
            val defaultField = Typeface::class.java.getDeclaredField("SERIF")
            Timber.i("defaultField :$defaultField")
            defaultField.isAccessible = true
            defaultField[null] = typeface
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}