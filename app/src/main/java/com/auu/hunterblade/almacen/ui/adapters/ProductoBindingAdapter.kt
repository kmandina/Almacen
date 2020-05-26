package com.auu.hunterblade.almacen.ui.adapters

import android.net.Uri
import android.provider.SyncStateContract
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.preference.PreferenceManager
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Sell
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {

//        val sp = PreferenceManager.getDefaultSharedPreferences(view.context)
//        val downloadType = sp.getString("downloadType", "2")
//        val urlSite = URL_Site(view.context)
//        urlSite.abrir()
//        val dir = urlSite.first
//        urlSite.cerrar()
//
//        val pathIn = if (downloadType == "1") {
//            dir
//        } else {
//
//            getExternalStorageDirectoryCustom(view.context).path + "/" + SyncStateContract.Constants.Strings.App_Name + "/." + "app_android_asset/"
//
//        }
//
//        val urlOut = "$imageUrl"
        val directory = File(imageUrl)
        if (!directory.exists()) {

            Glide.with(view.context)
                    .load(R.drawable.ic_crop_original)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(view)

        }else {

            Glide.with(view.context)
                    .load(Uri.parse("file:///$imageUrl"))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(view)
        }


    }else {
        Glide.with(view.context)
        .load(R.drawable.ic_crop_original)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view);
    }
}

@BindingAdapter("isGoneCheck")
fun bindIsGoneCheck(view: ImageView, isGone: Short?) {
    if (isGone == 0.toShort()) {
        view.visibility = View.INVISIBLE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("isGonePlay")
fun bindIsGonePlay(view: ImageView, trailerUrl: String?) {
    if (trailerUrl == "") {
        view.visibility = View.INVISIBLE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("isGoneLink")
fun bindIsGoneLink(view: TextView, links: Array<String?>) {
    if ( links.size == 1 && links[0] == "") {
        view.visibility = View.INVISIBLE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("isGoneCritca")
fun bindIsGoneCritica(view: TextView, critica: String) {
    if (critica  == "") {
        view.visibility = View.INVISIBLE
    } else {
        view.visibility = View.VISIBLE
    }
}


@BindingAdapter("renderHtml")
fun bindRenderHtml(view: TextView, description: String?) {
    if (description != null) {
        view.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
        view.movementMethod = LinkMovementMethod.getInstance()
    } else {
        view.text = ""
    }
}

@BindingAdapter("setFloatBuy")
fun bindFloatTextBuy(textView: TextView, v: Float) {

    textView.text = textView.context.getString(R.string.product_buy) + " $$v"
}

@BindingAdapter("setFloatSell")
fun bindFloatTextSell(textView: TextView, v: Float) {

    textView.text = textView.context.getString(R.string.product_sell) + " $$v"
}

@BindingAdapter("setFloatEarn")
fun bindFloatTextEarn(textView: TextView, v: Float) {

    textView.text = textView.context.getString(R.string.product_earn) + " $$v"
}

@BindingAdapter("setFloat")
fun bindFloatText(textView: TextView, v: Float) {

    textView.text = v.toString()
}

@BindingAdapter("setLongAmount")
fun bindLongTextAmount(textView: TextView, v: Long) {

    textView.text = textView.context.getString(R.string.product_amount) + " $v"
}

@BindingAdapter("setLong")
fun bindLongText(textView: TextView, v: Long) {

    textView.text = v.toString()
}

@BindingAdapter("setDateSell")
fun bindDateSellText(textView: TextView, sell: Sell) {

    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)


    textView.text = dateFormat.format(sell.date.time)
}

