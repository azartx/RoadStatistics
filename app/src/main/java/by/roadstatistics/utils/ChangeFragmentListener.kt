package by.roadstatistics.utils

import android.os.Bundle

interface ChangeFragmentListener {
    fun onFragmentChange(fragmentId: Int, bundle: Bundle?)
}