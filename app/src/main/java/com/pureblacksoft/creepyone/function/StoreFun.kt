package com.pureblacksoft.creepyone.function

import android.text.TextUtils
import androidx.preference.PreferenceManager
import com.pureblacksoft.creepyone.application.CreepyoneApp

class StoreFun
{
    companion object {
        //region Key
        const val KEY_APP_VER = "app_ver"
        const val KEY_APP_LANG = "app_lang"
        const val KEY_STORY_OP_TIME = "story_op_time"

        const val KEY_TERMS_ACCEPTED = "terms_accepted"
        const val KEY_TOTAL_LAUNCH = "total_launch"

        const val KEY_CURRENT_FILTER = "current_filter"
        const val KEY_CURRENT_SORT = "current_sort"

        const val KEY_CURRENT_FONT = "current_font"
        const val KEY_CURRENT_FONT_SIZE = "current_font_size"

        const val KEY_CURRENT_POSITION = "current_position"

        const val KEY_FAVORITE_STORY_ID_LIST = "favorite_story_id_list"
        const val KEY_READED_STORY_ID_LIST = "readed_story_id_list"
        //endregion

        private val preferences = PreferenceManager.getDefaultSharedPreferences(CreepyoneApp.getAppContext())

        //region Write
        fun writeString(key: String, value: String) {
            preferences.edit().putString(key, value).apply()
        }

        fun writeInt(key: String, value: Int) {
            preferences.edit().putInt(key, value).apply()
        }

        fun writeFloat(key: String, value: Float) {
            preferences.edit().putFloat(key, value).apply()
        }

        fun writeLong(key: String, value: Long) {
            preferences.edit().putLong(key, value).apply()
        }

        fun writeBoolean(key: String, value: Boolean) {
            preferences.edit().putBoolean(key, value).apply()
        }

        fun writeListString(key: String, stringList: MutableList<String>) {
            val myStringList = stringList.toTypedArray()
            preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
        }

        fun writeListInt(key: String, intList: MutableList<Int>) {
            val myIntList = intList.toTypedArray()
            preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply()
        }

        fun writeListFloat(key: String, floatList: MutableList<Float>) {
            val myFloatList = floatList.toTypedArray()
            preferences.edit().putString(key, TextUtils.join("‚‗‚", myFloatList)).apply()
        }

        fun writeListLong(key: String, longList: MutableList<Long>) {
            val myLongList = longList.toTypedArray()
            preferences.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply()
        }

        fun writeListBoolean(key: String, boolList: MutableList<Boolean>) {
            val newList = mutableListOf<String>()

            for (item in boolList) {
                if (item) {
                    newList.add("true")
                } else {
                    newList.add("false")
                }
            }

            writeListString(key, newList)
        }
        //endregion

        //region Read
        fun readString(key: String): String? = preferences.getString(key, "")

        fun readInt(key: String): Int = preferences.getInt(key, 0)

        fun readFloat(key: String): Float = preferences.getFloat(key, 0f)

        fun readLong(key: String): Long = preferences.getLong(key, 0)

        fun readBoolean(key: String): Boolean = preferences.getBoolean(key, false)

        fun readListString(key: String): MutableList<String> {
            return mutableListOf(*TextUtils.split(preferences.getString(key, ""), "‚‗‚"))
        }

        fun readListInt(key: String): MutableList<Int> {
            val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
            val mutableList = mutableListOf(*myList)
            val newList = mutableListOf<Int>()

            for (item in mutableList) newList.add(item.toInt())

            return newList
        }

        fun readListFloat(key: String): MutableList<Float> {
            val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
            val mutableList = mutableListOf(*myList)
            val newList = mutableListOf<Float>()

            for (item in mutableList) newList.add(item.toFloat())

            return newList
        }

        fun readListLong(key: String): MutableList<Long> {
            val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
            val mutableList = mutableListOf(*myList)
            val newList = mutableListOf<Long>()

            for (item in mutableList) newList.add(item.toLong())

            return newList
        }

        fun readListBoolean(key: String): MutableList<Boolean> {
            val myList = readListString(key)
            val newList = mutableListOf<Boolean>()

            for (item in myList) {
                if (item == "true") {
                    newList.add(true)
                } else {
                    newList.add(false)
                }
            }

            return newList
        }
        //endregion

        fun remove(key: String) {
            preferences.edit().remove(key).apply()
        }

        fun clear() {
            preferences.edit().clear().apply()
        }
    }
}

//PureBlack Software / Murat BIYIK