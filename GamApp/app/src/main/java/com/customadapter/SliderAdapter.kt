package com.customadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.barservicegam.app.R
import com.makeramen.roundedimageview.RoundedImageView
import data.SlideItem
import okhttp3.internal.notify

class SliderAdapter(var slideItems: ArrayList<SlideItem>, var viewPager: ViewPager2) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageSlide: RoundedImageView = itemView!!.findViewById(R.id.imageSlide)

        fun setImage(slideItem: SlideItem) {
            imageSlide.setImageResource(slideItem.getImage())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.slide_item_container, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SliderViewHolder) {
            holder.setImage(slideItems[position])

            if(position == slideItems.size - 2) {
                viewPager.post(sliderRunable)
            }
        }

//        if(position == slideItems.size - 2) {
//            slideItems.addAll(slideItems)
//            notifyDataSetChanged()
//        }
    }

    override fun getItemCount(): Int {
        return slideItems.size
    }

    val sliderRunable = object : Runnable {
        override fun run() {
            Log.d("vietnb", "tong so phan tu truoc: ${slideItems.size}")
            slideItems.addAll(slideItems)
            notifyDataSetChanged()
            Log.d("vietnb", "tong so phan tu sau: ${slideItems.size}")
        }
    }













}