package lyh.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

class ImageTextUtil {
    companion object {
        fun getUrlDrawable(souce:String,mTextView:TextView):Drawable{
            val imageGetter = GlideImageGetter(mTextView.context, mTextView)
            return imageGetter.getDrawable(souce)
        }

        fun setImageText(tv: TextView, html: String) {

            val htmlStr = Html.fromHtml(html)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//                tv.setTextIsSelectable(true)
//            }
            tv.text = htmlStr
            tv.movementMethod = LinkMovementMethod.getInstance()
            val text = tv.text
            if (text is Spannable) {
                val end = text.length
                val sp = tv.text as Spannable
                val urls = sp.getSpans(0, end, URLSpan::class.java)
                val imgs = sp.getSpans(0, end, ImageSpan::class.java)
                val styleSpens = sp.getSpans(0, end, StyleSpan::class.java)
                val colorSpans = sp.getSpans(0, end, ForegroundColorSpan::class.java)
                val style = SpannableStringBuilder(text)
                style.clearSpans()
                for (url in urls) {
                    style.setSpan(url, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val colorSpan = ForegroundColorSpan(Color.parseColor("#FF12ADFA"))
                    style.setSpan(colorSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                for (url in imgs) {
                    val span = ImageSpan(getUrlDrawable(url.source, tv), url.source)
                    style.setSpan(span, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                for (styleSpan in styleSpens) {
                    style.setSpan(styleSpan, sp.getSpanStart(styleSpan), sp.getSpanEnd(styleSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                for (colorSpan in colorSpans) {
                    style.setSpan(colorSpan, sp.getSpanStart(colorSpan), sp.getSpanEnd(colorSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                tv.text = style
            }
        }
    }
}