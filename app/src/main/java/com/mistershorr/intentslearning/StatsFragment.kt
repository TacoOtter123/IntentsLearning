package com.mistershorr.intentslearning

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_log_list.*
import kotlinx.android.synthetic.main.fragment_stats.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var userId : String
    private var logList : List<Logs?>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_stats, container, false)
        val bottomNavigationView =
            view.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.action_stats);

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {


                R.id.action_logs -> {
                    view.findNavController().navigate(R.id.action_statsFragment_to_fragment_log_list)
                }       // do something here
                R.id.action_search -> {
                }     // do something here
                R.id.action_stats -> {

                }
            }
            true
        }

        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var total = 0.0
        var allTime = 0.0
        var longestMileage = 0.0
        var fastestSplit = Double.MAX_VALUE

        userId = Backendless.UserService.CurrentUser().userId
        stats_textView_name.text = Backendless.UserService.CurrentUser().getProperty("name").toString()
        stats_textView_username.text = Backendless.UserService.CurrentUser().getProperty("username").toString()
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause


        Backendless.Data.of(Logs::class.java).find(queryBuilder,
            object : AsyncCallback<List<Logs?>?> {
                override fun handleResponse(foundLogs: List<Logs?>?) {
                    // the "foundGrades" collection now contains instances of the Contact class.
                    // each instance represents an object stored on the server.
                    logList = foundLogs

                    Log.d(fragment_log_list.TAG, "found: " + logList.toString())
                    if (foundLogs != null) {
                        for (log in foundLogs) {
                            if (log != null) {

                                allTime += log.distance
                                if (log.splitTime < fastestSplit) {
                                    fastestSplit = log.splitTime
                                }
                                if (log.distance > longestMileage) {
                                    longestMileage = log.distance
                                }

                                val c = Calendar.getInstance();
                                c.setTime(log.created);
                                c.add(Calendar.DATE,7);
                                if(c.getTime().compareTo(Date(System.currentTimeMillis()))>=0){
                                    total += log.distance
                                }



                            }
                        }

                    }
                    progressBar3.max =
                        Backendless.UserService.CurrentUser().getProperty("goal").toString().toInt()
                    Log.d("asdf", progressBar3.max.toString())
                    progressBar3.setProgress(total.toInt())
                    Log.d("asdf", total.toString())

                    stats_textView_progressNum.text =
                        "(" + total.toInt().toString() + "/" + Backendless.UserService.CurrentUser()
                            .getProperty(
                                "goal"
                            ).toString() + ") miles"
                    stats_textView_allTime.text = allTime.toString() + " miles"
                    stats_textView_longestRun.text = longestMileage.toString() + " miles"
                    stats_textView_splitShortest.text = fastestSplit.toString() + " minutes"
                    stats_textView_week.text = total.toString() + " miles"


                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d(fragment_log_list.TAG, fault.toString())
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                }
            })





        stats_button_update.setOnClickListener() {

            if(!stats_editText_goal.text.toString().equals("")) {
                progressBar3.max = stats_editText_goal.text.toString().toInt()
                progressBar3.setProgress(total.toInt())

                Backendless.UserService.CurrentUser().setProperty(
                    "goal",
                    stats_editText_goal.text.toString().toInt()
                )
                Backendless.UserService.update(
                    Backendless.UserService.CurrentUser(),
                    object : AsyncCallback<BackendlessUser?> {
                        override fun handleResponse(user: BackendlessUser?) {
                            Log.d("Update: ", "User has been updated")
                            stats_textView_progressNum.text = "(" + total.toInt()
                                .toString() + "/" + stats_editText_goal.text.toString() + ") miles"

                        }

                        override fun handleFault(fault: BackendlessFault) {
                            // user update failed, to get the error code call fault.getCode()
                        }
                    })



            }
            else {
                Toast.makeText(activity, "Please enter a number", Toast.LENGTH_SHORT).show()
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatsFragment.
         */
        // TODO: Rename and change types and number of parameters


    }
}