package br.edu.ifsp.scl.sdm.todolist.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task

@Dao
interface TaskDao {
    companion object {
        const val TASK_TABLE = "task" // nome da tabela
    }
    @Insert
    fun createTask(task: Task)
    @Query("SELECT * FROM $TASK_TABLE")
    fun retrieveTasks(): List<Task>
    @Update
    fun updateTask(task: Task)
    @Delete
    fun deleteTask(task: Task)
}