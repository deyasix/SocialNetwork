package com.example.myprofilemarkup.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.myprofilemarkup.R


class CustomGoogleRegistrationButton @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributes, defStyleAttr, defStyleRes) {


    var color: Int = Color.WHITE
    var cornerRadius: Float = 0f
    var text: String? = null
    var textColor: Int = Color.BLACK
    var icon : Drawable? = null
        set(value) {
            field = value
            if (value != null) {
                iconWidth = value.minimumWidth.toFloat()
                iconHeight = value.minimumHeight.toFloat()
            }
        }
    var textSize: Float = 14f
    var paddingBeforeText: Float = 0f
    var iconWidth: Float = 0f
    private var iconHeight : Float = 0f
    private var typeface: Typeface? = null
    private var letterSpacing : Float = 0.1f

    override fun onDraw(canvas: Canvas?) {

        canvas?.let {
            val button = drawButton(it)
            drawIcon(it)
            val textString = text.toString()
            val (textWidth, textHeight) = getTextWidthHeight(textString)
            val paint = Paint()
            paint.color = textColor
            paint.style = Paint.Style.FILL
            paint.textSize = textSize
            paint.typeface = typeface
            paint.letterSpacing = letterSpacing
            it.drawText(textString, button.centerX() - iconWidth / 2,
                button.centerY() - textHeight / 2, paint)
        }

    }

    private fun drawButton(canvas: Canvas) : RectF {
        val button = RectF()
        button[0f, 0f, width.toFloat()] = height.toFloat()
        val paint = Paint()
        paint.color = this.color
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(button, cornerRadius, cornerRadius, paint)
        return button
    }

    private fun getTextWidthHeight(text: String) : Pair<Float, Float> {
        val paint = Paint()
        paint.color = textColor
        paint.style = Paint.Style.FILL
        paint.textSize = textSize
        paint.typeface = typeface
        return Pair(paint.measureText(text), paint.descent() + paint.ascent())
    }

    private fun drawIcon(canvas: Canvas) {

        val (textWidth, textHeight) = getTextWidthHeight(text?:"")
        val left = ((width - iconWidth - textWidth) / 2).toInt()
        val top = if (iconHeight > textHeight) ((height - iconHeight) / 2).toInt()
            else ((height - textHeight) / 2).toInt()
        icon?.setBounds(left, top, iconWidth.toInt() + left, iconHeight.toInt() + top)
        icon?.draw(canvas)
    }

    init {
        if (attributes != null) {
            initAttributes(attributes, defStyleAttr, defStyleRes)
        }
    }

    private fun initAttributes(attributes: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attributes, R.styleable.CustomGoogleRegistrationButton, defStyleAttr, defStyleRes)
        with (typedArray) {
            color = getColor(R.styleable.CustomGoogleRegistrationButton_googleButtonColor, Color.WHITE)
            cornerRadius = getDimension(R.styleable.CustomGoogleRegistrationButton_googleButtonCornerRadius, 0f)
            text = getString(R.styleable.CustomGoogleRegistrationButton_googleButtonText)
            textColor = getColor(R.styleable.CustomGoogleRegistrationButton_googleButtonTextColor, Color.BLACK)
            icon = getDrawable(R.styleable.CustomGoogleRegistrationButton_googleButtonIcon)
            textSize = getDimension(R.styleable.CustomGoogleRegistrationButton_googleButtonTextSize, 14f)
            paddingBeforeText = getDimension(R.styleable.CustomGoogleRegistrationButton_googleButtonPaddingBeforeText, 0f)
            iconWidth = getDimension(R.styleable.CustomGoogleRegistrationButton_googleButtonIconWidth, iconWidth)
            iconHeight = getDimension(R.styleable.CustomGoogleRegistrationButton_googleButtonIconHeight, iconHeight)
            val fontId = getResourceId(R.styleable.CustomGoogleRegistrationButton_googleButtonTextFontFamily, 0)
            if (fontId > 0) {
                typeface = ResourcesCompat.getFont(context, fontId)
            }
            letterSpacing = getFloat(R.styleable.CustomGoogleRegistrationButton_googleButtonLetterSpacing, 0.1f)
            recycle()
        }
    }


}