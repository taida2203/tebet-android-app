package com.squline.student.common.view

import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * Created by Mochamad Noor Syamsu on 7/31/18.
 */
class ViewPagerTabChangePageListener(private val pagerTabAdapter: FragmentStatePagerAdapter) : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        val fragmentTabScreenTracker =
                pagerTabAdapter.getItem(position) as FragmentTabScreenTracker
        fragmentTabScreenTracker.trackLoadAction()
    }

    interface FragmentTabScreenTracker {
        fun trackLoadAction()
    }
}
