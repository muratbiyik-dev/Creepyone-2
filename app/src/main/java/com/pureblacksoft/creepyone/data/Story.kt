package com.pureblacksoft.creepyone.data

data class Story(
        var id: Int,
        var title: String,
        var content: String,
        var author: String,
        var image: ByteArray?,
        var popularity: Int,
        var length: Float,
        var favorite: Int,
        var readed: Int)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Story

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}

//PureBlack Software / Murat BIYIK