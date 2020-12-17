package com.lukeedgar.contacttrace

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper


class ExposureIdDatabase internal constructor(context: Context?) :
        SQLiteOpenHelper(context, "exposure_id", null, 2) {

    private val DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + "exposure_id" + " (" +
            "id" + " TEXT, " +
            "dateOfGeneration" + " TEXT);"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DICTIONARY_TABLE_CREATE)
    }

    fun put(db: SQLiteDatabase, exposureId: ExposureId) {
        val updateCmd =
                """INSERT INTO exposure_id (id, dateOfGeneration) VALUES ("${exposureId.id}", "${exposureId.dateOfGeneration}");"""
        db.execSQL(updateCmd)
    }

    fun getAll() : List<ExposureId> {
        val db = readableDatabase

        val projection = arrayOf("id", "dateOfGeneration")
        val cursor = db.query(
            "exposure_id",   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val itemIds = mutableListOf<ExposureId>()
        with(cursor) {
            while (moveToNext()) {
                itemIds.add(ExposureId(getString(0), getString(1)))
            }
        }
        return itemIds
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}