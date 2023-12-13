package br.edu.ifsp.scl.sdm.todolist.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.todolist.R
import br.edu.ifsp.scl.sdm.todolist.controller.MainController
import br.edu.ifsp.scl.sdm.todolist.databinding.FragmentMainBinding
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task.Companion.TASK_DONE_FALSE
import br.edu.ifsp.scl.sdm.todolist.model.entity.Task.Companion.TASK_DONE_TRUE
import br.edu.ifsp.scl.sdm.todolist.view.adapter.OnTaskClickListener
import br.edu.ifsp.scl.sdm.todolist.view.adapter.TaskAdapter

class MainFragment : Fragment(), OnTaskClickListener {
    private lateinit var fmb: FragmentMainBinding

    // Data source
    private val taskList: MutableList<Task> = mutableListOf()

    // Adapter
    private val tasksAdapter: TaskAdapter by lazy {
        TaskAdapter(taskList, this)
    }

    // Navigation controller
    private val navController: NavController by lazy {
        findNavController()
    }

    // Communication constants
    companion object {
        const val EXTRA_TASK = "EXTRA_TASK"
        const val TASK_FRAGMENT_REQUEST_KEY = "TASK_FRAGMENT_REQUEST_KEY"
    }

    //Controller
    private val mainController: MainController by lazy {
        MainController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(TASK_FRAGMENT_REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == TASK_FRAGMENT_REQUEST_KEY) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(EXTRA_TASK, Task::class.java)
                } else {
                    bundle.getParcelable(EXTRA_TASK)
                }
                task?.also { receivedTask ->
                    taskList.indexOfFirst { it.time == receivedTask.time }.also { position ->
                        if (position != -1) {
                            mainController.editTask(receivedTask)
                            taskList[position] = receivedTask
                            tasksAdapter.notifyItemChanged(position)
                        } else {
                            mainController.insertTask(receivedTask)
                            taskList.add(receivedTask)
                            tasksAdapter.notifyItemInserted(taskList.lastIndex)
                        }
                    }
                }

                // Hiding soft keyboard
                (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    fmb.root.windowToken,
                    HIDE_NOT_ALWAYS
                )
            }
        }

        mainController.getTasks()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.task_list)

        fmb = FragmentMainBinding.inflate(inflater, container, false).apply {
            tasksRv.layoutManager = LinearLayoutManager(context)
            tasksRv.adapter = tasksAdapter

            addTaskFab.setOnClickListener {
                navController.navigate(
                    MainFragmentDirections.actionMainFragmentToTaskFragment(null, editTask = false)
                )
            }
        }

        return fmb.root
    }

    override fun onTaskClick(position: Int) = navigateToTaskFragment(position, false)

    override fun onRemoveTaskMenuItemClick(position: Int) {
        mainController.removeTask(taskList[position])
        taskList.removeAt(position)
        tasksAdapter.notifyItemRemoved(position)
    }

    override fun onEditTaskMenuItemClick(position: Int) = navigateToTaskFragment(position, true)

    override fun onDoneCheckBoxClick(position: Int, checked: Boolean) {
        taskList[position].apply {
            done = if (checked) TASK_DONE_TRUE else TASK_DONE_FALSE
            mainController.editTask(this)
        }
    }

    private fun navigateToTaskFragment(position: Int, editTask: Boolean) {
        taskList[position].also {
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToTaskFragment(it, editTask)
            )
        }
    }

    fun updateTaskList(tasks: List<Task>) {
        taskList.clear()
        tasks.forEachIndexed { index, task ->
            taskList.add(task)
            tasksAdapter.notifyItemChanged(index)
        }
    }
}