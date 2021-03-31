package com.example.roomobservableissue

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable

@Entity(tableName = "my_table")
data class MyTable(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "status")
    val status: Int,
)

@Dao
interface MyDao {
    @Query("SELECT status FROM my_table WHERE id=:id")
    fun observeStatus(id: Int): Observable<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewRecord(myTable: MyTable): Completable

    @Query("UPDATE my_table SET status=:newStatus WHERE id=:id")
    fun updateStatus(id: Int, newStatus: Int): Completable
}

@Database(entities = arrayOf(MyTable::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myDao(): MyDao
}
