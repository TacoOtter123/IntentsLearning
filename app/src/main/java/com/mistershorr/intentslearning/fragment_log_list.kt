package com.mistershorr.intentslearning
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.backendless.persistence.LoadRelationsQueryBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_log_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_log_list.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_log_list : Fragment() {
    companion object{
        val TAG = "FragmentLogList"
    }
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
        val view = inflater.inflate(R.layout.fragment_log_list, container, false)
        val bottomNavigationView =
            view.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        val fab = view.findViewById<View>(R.id.logList_fab_new)





        userId = Backendless.UserService.CurrentUser().userId



        // do an advanced data retrieval with a where clause that matches the ownerId to the current userId
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        queryBuilder.setSortBy("created")

        Backendless.Data.of(Logs::class.java).find(queryBuilder,
            object : AsyncCallback<List<Logs?>?> {
                override fun handleResponse(foundLogs: List<Logs?>?) {
                    // the "foundGrades" collection now contains instances of the Contact class.
                    // each instance represents an object stored on the server.
                    logList = foundLogs?.reversed()
                    Log.d(TAG, "found: " + logList.toString())

                    log_recycler.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        adapter = LogAdapter(logList)
                    }
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d(TAG, fault.toString())
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                }
            })






        //log_recycler.apply {
        //    setHasFixedSize(true)
        //    layoutManager = LinearLayoutManager(getActivity())
            //adapter = FactsAdapter(response.body()!!)
        //}



        bottomNavigationView.setSelectedItemId(R.id.action_logs);

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {


                R.id.action_logs -> {

                }       // do something here
                R.id.action_search -> {
                }     // do something here
                R.id.action_stats -> {
                    view.findNavController().navigate(R.id.action_fragment_log_list_to_statsFragment)
                }
            }
            true
        }

        fab.setOnClickListener{
            view.findNavController().navigate(R.id.action_fragment_log_list_to_addLogFragment)
        }

        return view
    }



}