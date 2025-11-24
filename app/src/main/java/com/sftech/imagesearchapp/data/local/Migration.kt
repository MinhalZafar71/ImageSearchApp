package com.sftech.imagesearchapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add new columns to the existing table
        db.execSQL("ALTER TABLE favorite ADD COLUMN imageUrl TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE favorite ADD COLUMN previewImageUrl TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE favorite ADD COLUMN tags TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE favorite ADD COLUMN imageWidth INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE favorite ADD COLUMN imageHeight INTEGER NOT NULL DEFAULT 0")
    }
}