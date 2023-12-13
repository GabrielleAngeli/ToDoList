package br.edu.ifsp.scl.sdm.todolist.controller

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import br.edu.ifsp.scl.sdm.todolist.model.database.ToDoListDatabase
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): ViewModel() {
    private val taskDaoImpl = Room.databaseBuilder(
        application.applicationContext,
        ToDoListDatabase::class.java,
        ToDoListDatabase.TO_DO_LIST_DATABASE
    ).build().getTaskDao()

    val tasksMld = MutableLiveData<List<Task>>()
    fun insertTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDaoImpl.createTask(task)
        }
    }

    fun getTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = taskDaoImpl.retrieveTasks()
            tasksMld.postValue(tasks)
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

    companion object {
        val TaskViewModelFactory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T  =
                TaskViewModel(checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])) as T
        }
    }
}