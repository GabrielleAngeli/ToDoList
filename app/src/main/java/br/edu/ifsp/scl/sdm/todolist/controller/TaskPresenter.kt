package br.edu.ifsp.scl.sdm.todolist.controller

import androidx.fragment.app.Fragment
import androidx.room.Room
import br.edu.ifsp.scl.sdm.todolist.model.database.ToDoListDatabase
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskPresenter(private val taskView: TaskView) {
    private val taskDaoImpl = Room.databaseBuilder(
        (taskView as Fragment).requireContext(),
        ToDoListDatabase::class.java,
        ToDoListDatabase.TO_DO_LIST_DATABASE
    ).build().getTaskDao()

    fun insertTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDaoImpl.createTask(task)
        }
    }

    fun getTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = taskDaoImpl.retrieveTasks()
            taskView.updateTaskList(tasks)
        }
    }

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

    interface TaskView {
        fun updateTaskList(tasks: List<Task>)
    }
}