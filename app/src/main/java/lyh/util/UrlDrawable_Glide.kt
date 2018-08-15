package lyh.util

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.resource.drawable.GlideDrawable

class UrlDrawable_Glide: Drawable(),Drawable.Callback {
    var mDrawable:GlideDrawable?=null

    override fun draw(canvas: Canvas?) {
        if (mDrawable != null) {
            mDrawable!!.draw(canvas)
        }
    }

    override fun setAlpha(alpha: Int) {
        if (mDrawable != null) {
            mDrawable!!.alpha = alpha
        }
    }

    override fun getOpacity(): Int {
        var i=0
        return if (mDrawable != null) {
            mDrawable!!.opacity
        } else i
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (mDrawable != null) {
            mDrawable!!.colorFilter = colorFilter
        }
    }

    fun setDrawable(drawable: GlideDrawable) {
        if (this.mDrawable != null) {
            this.mDrawable!!.callback = null
        }
        drawable.callback = this
        this.mDrawable = drawable
    }

    override fun unscheduleDrawable(who: Drawable?, what: Runnable?) {
        if (callback != null) {
            callback!!.unscheduleDrawable(who, what)
        }
    }

    override fun invalidateDrawable(who: Drawable?) {
        if (callback != null) {
            callback!!.invalidateDrawable(who)
        }
    }

    override fun scheduleDrawable(who: Drawable?, what: Runnable?, `when`: Long) {
        if (callback != null) {
            callback!!.scheduleDrawable(who, what, `when`)
        }
    }
}