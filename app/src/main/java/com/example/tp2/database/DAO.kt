package com.example.tp2.database

import android.content.Context
import androidx.room.*
import com.example.tp2.model.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * from user WHERE id = :key")
    fun get(key: Long): User?

    @Query("SELECT * FROM user ORDER BY id DESC LIMIT 1")
    fun getLastUser(): User?

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>?

    @Query("SELECT * FROM user where id = :key")
    fun getUserUpThen(key: Long): List<User>?

    @Query("SELECT * from user WHERE email = :email")
    fun getByEmail(email: String): List<User>?

    @Query("SELECT * from user WHERE email = :email and password = :password")
    fun getByEmailPassword(email: String, password: String): User?

    @Query("DELETE from user WHERE id = :id")
    fun deleteById(id: Long)
}

@Database(entities = [User::class], version = 4, exportSchema = false) abstract class MyDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "my_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}