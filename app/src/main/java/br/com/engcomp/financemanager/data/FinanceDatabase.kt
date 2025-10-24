package br.com.engcomp.financemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.engcomp.financemanager.model.Transaction


@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class FinanceDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object{

        private var INSTANCE: FinanceDatabase? = null
        fun getInstance(context: Context): FinanceDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FinanceDatabase::class.java,
                        "finance_database"
                    ).build()
                    INSTANCE = instance
                }
                 return instance
            }
        }
    }
}