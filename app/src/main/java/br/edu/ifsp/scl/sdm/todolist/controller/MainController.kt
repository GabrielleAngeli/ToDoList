package br.edu.ifsp.scl.sdm.todolist.controller

import androidx.room.Room
import br.edu.ifsp.scl.sdm.todolist.model.database.ToDoListDatabase
import br.edu.ifsp.scl.sdm.todolist.model.database.ToDoListDatabase.Companion.TO_DO_LIST_DATABASE
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task
import br.edu.ifsp.scl.sdm.todolist.view.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainController(private val mainFragment: MainFragment) {
    private val taskDaoImpl = Room.databaseBuilder(
        mainFragment.requireContext(),
        ToDoListDatabase::class.java,
        TO_DO_LIST_DATABASE
    ).build().getTaskDao()

    fun insertTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDaoImpl.createTask(task)
        }
    }

//    fun getTasks() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val tasks = taskDaoImpl.retrieveTasks()
//            mainFragment.updateTaskList(tasks)
//        }
//    }

    fun editTask(task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            taskDaoImpl.updateTask(task)
        }
    }

    fun removeTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDaoImpl.deleteTask(task)
        }
    }
}