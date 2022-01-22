package com.math3249.listler.util.utilinterface

interface Dragable {
    val dragDirs: Int
    fun movement(from: Int, to: Int)

}