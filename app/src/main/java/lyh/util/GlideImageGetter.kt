package lyh.util

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ViewTarget
import lyh.readdemo.R
import java.util.HashSet


class GlideImageGetter(var mContext:Context,var mTextView:TextView):Html.ImageGetter,Drawable.Callback {
    private var mTargets: HashSet<ImageGetterViewTarget>? = null

    companion object {
        operator fun get(view: View): GlideImageGetter {
            return view.getTag(R.id.drawable_callback_tag) as GlideImageGetter
        }
    }

    fun clear() {
        val prev = get(mTextView) ?: return

        for (target in prev.mTargets!!) {
            Glide.clear(target)
        }
    }

    init {
        mTargets = HashSet<ImageGetterViewTarget>()
        mTextView.setTag(R.id.drawable_callback_tag, this)
    }



    override fun unscheduleDrawable(who: Drawable?, what: Runnable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun invalidateDrawable(who: Drawable?) {
        mTextView.invalidate()
    }

    override fun scheduleDrawable(who: Drawable?, what: Runnable?, `when`: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDrawable(source: String): Drawable {
        val urlDrawable = UrlDrawable_Glide()
        println("Downloading from: $source")
        Glide.with(mContext)
                .load(source)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ImageGetterViewTarget(mTextView, urlDrawable))
        return urlDrawable
    }

    private inner class ImageGetterViewTarget(view: TextView, private val mDrawable: UrlDrawable_Glide) : ViewTarget<TextView, GlideDrawable>(view) {

        private var request: Request? = null

        init {
            mTargets!!.minus(this)
        }

        override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
            val rect: Rect
            if (resource.intrinsicWidth > 100) {
                val width: Float
                val height: Float
                println("Image width is " + resource.intrinsicWidth)
                println("View width is " + view.width)
                if (resource.intrinsicWidth >= getView().width) {
                    val downScale = resource.intrinsicWidth.toFloat() / getView().width
                    width = resource.intrinsicWidth.toFloat() / downScale
                    height = resource.intrinsicHeight.toFloat() / downScale
                } else {
                    val multiplier = getView().width.toFloat() / resource.intrinsicWidth
                    width = resource.intrinsicWidth.toFloat() * multiplier
                    height = resource.intrinsicHeight.toFloat() * multiplier
                }
                println("New Image width is $width")


                rect = Rect(0, 0, Math.round(width), Math.round(height))
            } else {
                rect = Rect(0, 0, resource.intrinsicWidth * 2, resource.intrinsicHeight * 2)
            }
            resource.bounds = rect

            mDrawable.bounds = rect
            mDrawable.setDrawable(resource)


            if (resource.isAnimated) {
                mDrawable.callback = get(getView())
                resource.setLoopCount(GlideDrawable.LOOP_FOREVER)
                resource.start()
            }

            getView().text = getView().text
            getView().invalidate()
        }

        override fun getRequest(): Request? {
            return request
        }

        override fun setRequest(request: Request) {
            this.request = request
        }
    }

}