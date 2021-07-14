package rojo.ader.digimind.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import rojo.ader.digimind.R
import rojo.ader.digimind.Task
import rojo.ader.digimind.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    var tasks = ArrayList<Task>()
    private lateinit var homeViewModel: HomeViewModel
    private var adaptador:AdaptadorTareas?=null

    companion object{
        var tasks=ArrayList<Task>()
        var first=true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home,container,false)

        if(first){
            fillTasks()
            first=false
        }

        adaptador= AdaptadorTareas(root.context,tasks)

        val gridView = root.findViewById(R.id.reminders) as GridView
        
        gridView.adapter=adaptador
        return root
    }

    fun fillTasks(){
        tasks.add(Task("Practice 1", arrayListOf("Tuesday"),"17:30"))
        tasks.add(Task("Practice 2", arrayListOf("Monday","Sunday"),"17:00"))
        tasks.add(Task("Practice 3", arrayListOf("Wednesday"),"14:00"))
        tasks.add(Task("Practice 4", arrayListOf("Saturday"),"11:00"))
        tasks.add(Task("Practice 5", arrayListOf("Friday"),"13:00"))
        tasks.add(Task("Practice 6", arrayListOf("Thursday"),"10:40"))
        tasks.add(Task("Practice 7", arrayListOf("Monday"),"12:00"))
    }

    private class AdaptadorTareas:BaseAdapter{
        var tasks = ArrayList<Task>()
        var contexto: Context?=null

        constructor(context: Context,tasks:ArrayList<Task>){
            this.contexto=contexto
            this.tasks=tasks
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var task = tasks[position]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.task_view,null)

            val tv_title  = vista.findViewById(R.id.tv_title) as TextView
            val tv_days = vista.findViewById(R.id.tv_days) as TextView
            val tv_time = vista.findViewById(R.id.tv_time) as TextView

            tv_title.setText(task.title)
            tv_days.setText(task.days.toString())
            tv_time.setText(task.time)

            return vista

        }

        override fun getItem(position: Int): Any {
            return tasks[position]
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
    }

}