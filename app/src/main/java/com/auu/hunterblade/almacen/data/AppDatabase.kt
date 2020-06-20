package com.auu.hunterblade.almacen.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.auu.hunterblade.almacen.utils.DATABASE_NAME

@Database(entities = [Product::class, Sell::class, ProductSell::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ProductDao(): ProductDao
    abstract fun SellDao(): SellDao
    abstract fun ProductSellDao(): ProductSellDao

    companion object {

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE product ADD COLUMN state TEXT NOT NULL DEFAULT 'Max';")
                database.execSQL("ALTER TABLE product ADD COLUMN currency TEXT NOT NULL DEFAULT 'CUP';")
                database.execSQL("ALTER TABLE product ADD COLUMN sold INTEGER NOT NULL DEFAULT 0;")
            }
        }

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
//                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
//                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}