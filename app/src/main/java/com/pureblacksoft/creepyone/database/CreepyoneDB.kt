package com.pureblacksoft.creepyone.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pureblacksoft.creepyone.data.Story
import com.pureblacksoft.creepyone.dialog.ManageDialog

class CreepyoneDB(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object {
        private const val DATABASE_NAME = "creepyone_db"
        private const val DATABASE_VERSION = 1

        //region Story Table
        private const val STORY_TABLE = "story_table"
        private const val STORY_ID = "id"
        private const val STORY_TITLE = "title"
        private const val STORY_CONTENT = "content"
        private const val STORY_AUTHOR = "author"
        private const val STORY_IMAGE = "image"
        private const val STORY_POPULARITY = "popularity"
        private const val STORY_LENGTH = "length"
        private const val STORY_FAVORITE = "favorite"
        private const val STORY_READED = "readed"

        private const val STORY_TABLE_SQL = "CREATE TABLE $STORY_TABLE (" +
                "$STORY_ID INTEGER PRIMARY KEY UNIQUE, " +
                "$STORY_TITLE VARCHAR(256), " +
                "$STORY_CONTENT TEXT, " +
                "$STORY_AUTHOR VARCHAR(128), " +
                "$STORY_IMAGE BLOB, " +
                "$STORY_POPULARITY INTEGER, " +
                "$STORY_LENGTH FLOAT, " +
                "$STORY_FAVORITE INTEGER, " +
                "$STORY_READED INTEGER)"
        //endregion
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(STORY_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $STORY_TABLE")
        onCreate(db)
    }

    //region Story Funcs
    //region Story Operations
    fun insertStory(story: Story) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(STORY_ID, story.id)
        values.put(STORY_TITLE, story.title)
        values.put(STORY_CONTENT, story.content)
        values.put(STORY_AUTHOR, story.author)
        values.put(STORY_IMAGE, story.image)
        values.put(STORY_POPULARITY, story.popularity)
        values.put(STORY_LENGTH, story.length)
        values.put(STORY_FAVORITE, story.favorite)
        values.put(STORY_READED, story.readed)
        db.insert(STORY_TABLE, null, values)
    }

    fun updateStory(story: Story, oldStory: Story) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(STORY_ID, story.id)
        values.put(STORY_TITLE, story.title)
        values.put(STORY_CONTENT, story.content)
        values.put(STORY_AUTHOR, story.author)
        values.put(STORY_IMAGE, story.image)
        values.put(STORY_POPULARITY, story.popularity)
        values.put(STORY_LENGTH, story.length)
        val whereClause = "$STORY_ID = ?"
        val whereArgs = Array(1) {oldStory.id.toString()}
        db.update(STORY_TABLE, values, whereClause, whereArgs)
    }

    fun deleteStory(oldStory: Story) {
        val db = this.writableDatabase
        val whereClause = "$STORY_ID = ?"
        val whereArgs = Array(1) {oldStory.id.toString()}
        db.delete(STORY_TABLE, whereClause, whereArgs)
    }

    fun deleteStoryTable() {
        val db = this.writableDatabase
        db.delete(STORY_TABLE, null, null)
        db.close()
    }
    //endregion

    //region Story Choices
    fun setStoryFavorite(story: Story, favorite: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(STORY_FAVORITE, favorite)
        val whereClause = "$STORY_ID = ?"
        val whereArgs = Array(1) {story.id.toString()}
        db.update(STORY_TABLE, values, whereClause, whereArgs)
    }

    fun setStoryReaded(story: Story, readed: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(STORY_READED, readed)
        val whereClause = "$STORY_ID = ?"
        val whereArgs = Array(1) {story.id.toString()}
        db.update(STORY_TABLE, values, whereClause, whereArgs)
    }
    //endregion

    //region Story Lists
    fun getStoryList(): MutableList<Story> {
        val query = "SELECT * FROM $STORY_TABLE"

        return getStoryListByQuery(query)
    }

    fun getManagedStoryList(filterId: Int, sortId: Int): MutableList<Story> {
        val filterQuery = when (filterId) {
            ManageDialog.ID_FILTER_UNREAD -> {
                "WHERE $STORY_READED = 0"
            }
            else -> {
                ""
            }
        }

        val sortQuery = when (sortId) {
            ManageDialog.ID_SORT_DATE_OLD -> {
                "ORDER BY $STORY_ID ASC"
            }
            ManageDialog.ID_SORT_LENGTH_ASC -> {
                "ORDER BY $STORY_LENGTH ASC"
            }
            ManageDialog.ID_SORT_LENGTH_DESC -> {
                "ORDER BY $STORY_LENGTH DESC"
            }
            else -> {
                "ORDER BY $STORY_ID DESC"
            }
        }

        val managedQuery = "SELECT * FROM $STORY_TABLE $filterQuery $sortQuery"

        return getStoryListByQuery(managedQuery)
    }

    fun getPopularStoryList(): MutableList<Story> {
        val popularQuery = "SELECT * FROM $STORY_TABLE WHERE $STORY_POPULARITY > 0 ORDER BY $STORY_POPULARITY DESC"

        return getStoryListByQuery(popularQuery)
    }

    fun getFavoriteStoryList(): MutableList<Story> {
        val favoriteQuery = "SELECT * FROM $STORY_TABLE WHERE $STORY_FAVORITE = 1 ORDER BY $STORY_TITLE"

        return getStoryListByQuery(favoriteQuery)
    }

    fun getPictureStoryList(): MutableList<Story> {
        val pictureQuery = "SELECT * FROM $STORY_TABLE WHERE $STORY_IMAGE IS NOT NULL ORDER BY $STORY_ID DESC"

        return getStoryListByQuery(pictureQuery)
    }

    private fun getStoryListByQuery(query: String): MutableList<Story> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        val storyList = mutableListOf<Story>()
        storyCursorFirst(cursor, storyList)
        db.close()

        return storyList
    }

    private fun storyCursorFirst(cursor: Cursor, storyList: MutableList<Story>) {
        if(cursor.moveToFirst()) {
            do {
                val story = Story(
                        id = cursor.getInt(0),
                        title = cursor.getString(1),
                        content = cursor.getString(2),
                        author = cursor.getString(3),
                        image = cursor.getBlob(4),
                        popularity = cursor.getInt(5),
                        length = cursor.getFloat(6),
                        favorite = cursor.getInt(7),
                        readed = cursor.getInt(8))
                storyList.add(story)
            }
            while (cursor.moveToNext())
        }
        cursor.close()
    }
    //endregion

    //region Story Id Lists
    fun getFavoriteStoryIdList(): MutableList<Int> {
        val favoriteQuery = "SELECT * FROM $STORY_TABLE WHERE $STORY_FAVORITE = 1"

        return getStoryIdListByQuery(favoriteQuery)
    }

    fun getReadedStoryIdList(): MutableList<Int> {
        val readedQuery = "SELECT * FROM $STORY_TABLE WHERE $STORY_READED = 1"

        return getStoryIdListByQuery(readedQuery)
    }

    private fun getStoryIdListByQuery(query: String): MutableList<Int> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        val storyIdList = mutableListOf<Int>()
        storyIdCursorFirst(cursor, storyIdList)
        db.close()

        return storyIdList
    }

    private fun storyIdCursorFirst(cursor: Cursor, storyIdList: MutableList<Int>) {
        if(cursor.moveToFirst()) {
            do {
                storyIdList.add(cursor.getInt(0))
            }
            while (cursor.moveToNext())
        }
        cursor.close()
    }
    //endregion
    //endregion
}

//PureBlack Software / Murat BIYIK