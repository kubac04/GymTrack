package com.example.gymtrack.util

import android.content.Context

fun exerciseDrawableRes(context: Context, exerciseName: String): Int {
    val resourceName = "ex_" + exerciseName
        .lowercase()
        .replace(" ", "_")
        .replace("-", "_")
        .replace("(", "")
        .replace(")", "")
        .replace("/", "_")
    return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
}
