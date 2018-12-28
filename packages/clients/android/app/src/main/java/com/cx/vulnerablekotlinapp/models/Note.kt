package com.cx.vulnerablekotlinapp.models

data class Note(var title: String, var content: String) {
    var id: Int = -1
    var owner: Int = -1
}