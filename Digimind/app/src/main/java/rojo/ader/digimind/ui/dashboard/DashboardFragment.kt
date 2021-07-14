package rojo.ader.digimind.ui.dashboard

import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import rojo.ader.digimind.R
import rojo.ader.digimind.Task
import rojo.ader.digimind.databinding.FragmentDashboardBinding
import rojo.ader.digimind.ui.home.HomeFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    val db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val btn_time = root.findViewById(R.id.time) as Button
        val btn_save = root.findViewById(R.id.done) as Button
        val et_task = root.findViewById(R.id.name) as EditText
        val checkMonday = root.findViewById(R.id.monday) as CheckBox
        val checkTuesday = root.findViewById(R.id.tuesday) as CheckBox
        val checkWednesday = root.findViewById(R.id.wednesday) as CheckBox
        val checkThursday = root.findViewById(R.id.thursday) as CheckBox
        val checkFriday = root.findViewById(R.id.friday) as CheckBox
        val checkSaturday = root.findViewById(R.id.saturday) as CheckBox
        val checkSunday = root.findViewById(R.id.sunday) as CheckBox

        btn_time.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                btn_time.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(
                root.context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        btn_save.setOnClickListener {
            var title = et_task.text.toString()
            var time = btn_time.text.toString()

            var days = ArrayList<String>()
            var lu = false
            var ma = false
            var mie = false
            var ju = false
            var vi = false
            var sa = false
            var dom = false

            if (checkMonday.isChecked) {
                days.add("Monday")
                lu=true
            }
            if (checkTuesday.isChecked) {
                days.add("Tuesday")
                ma=true
            }
            if (checkWednesday.isChecked) {
                days.add("Wednesday")
                mie=true
            }
            if (checkThursday.isChecked) {
                days.add("Thursday")
                ju=true
            }
            if (checkFriday.isChecked) {
                days.add("Friday")
                vi=true
            }
            if (checkSaturday.isChecked) {
                days.add("Saturday")
                sa=true
            }
            if (checkSunday.isChecked) {
                days.add("Sunday")
                dom=true
            }

            var task = Task(title, days, time)

            val homeFragment = HomeFragment()

            val tarea = hashMapOf(
                "actividad" to title,
                "tiempo" to time,
                "lu" to lu,
                "ma" to ma,
                "mie" to mie,
                "ju" to ju,
                "vi" to vi,
                "sa" to sa,
                "dom" to dom
            )

            db.collection("actividades").add(tarea).addOnSuccessListener {
                documentReference -> Log.d(TAG,"DocumentSnapshot added with ID: ${documentReference.id}")
            }
                .addOnFailureListener { e -> Log.w(TAG,"Error adding document",e) }
            homeFragment.tasks.add(task)

            Toast.makeText(root.context, "New task added", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}