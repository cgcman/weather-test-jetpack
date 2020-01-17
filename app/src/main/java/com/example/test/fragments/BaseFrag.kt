package com.example.test.fragments

import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.test.MainActivity

abstract class BaseFrag: Fragment() {
    open fun getActionBar(): ActionBar? {
        return (activity as MainActivity).getSupportActionBar()
    }
}