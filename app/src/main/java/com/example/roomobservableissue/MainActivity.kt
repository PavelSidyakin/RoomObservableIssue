package com.example.roomobservableissue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.room.Room
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
        ).build()

        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.test_button).setOnClickListener { executeTransactions() }

        db.myDao().observeStatus(1)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.i("DB", "next: $it")
                })

        db.myDao().addNewRecord(MyTable(1, 0))
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private fun executeTransactions() {
        val delay = 0L
        Completable.defer { db.myDao().updateStatus(1,1) }
                .andThen(Completable.defer { Completable.timer(delay, TimeUnit.MILLISECONDS) })
                .andThen(Completable.defer { db.myDao().updateStatus(1, 2) })
                .andThen(Completable.defer { Completable.timer(delay, TimeUnit.MILLISECONDS) })
                .andThen(Completable.defer { db.myDao().updateStatus(1, 3) })
                .andThen(Completable.defer { Completable.timer(delay, TimeUnit.MILLISECONDS) })
                .andThen(Completable.defer { db.myDao().updateStatus(1, 4) })
                .andThen(Completable.defer { Completable.timer(delay, TimeUnit.MILLISECONDS) })
                .andThen(Completable.defer { db.myDao().updateStatus(1, 5) })
                .andThen(Completable.defer { Completable.timer(delay, TimeUnit.MILLISECONDS) })
                .andThen(Completable.defer { db.myDao().updateStatus(1, 6) })
                .subscribeOn(Schedulers.io())
                .subscribe()

    }

}