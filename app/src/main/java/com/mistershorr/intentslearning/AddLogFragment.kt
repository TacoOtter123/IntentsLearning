package com.mistershorr.intentslearning

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_add_log.*
import kotlinx.android.synthetic.main.fragment_log_list.*
import kotlinx.android.synthetic.main.fragment_stats.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddLogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddLogFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var userId : String
    private var nameList : ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add_log, container, false)



        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        button_main_submit.setOnClickListener() {

            val userId = Backendless.UserService.CurrentUser().userId
            Log.d("asdf", editTextNunberDecimal_main_distance.text.toString())
            val log = Logs(distance = editTextNunberDecimal_main_distance.text.toString().toDouble(),
                name = editText_main_runname.text.toString(),
                notes = editTextTextMultiLine.text.toString(),
                time = editText_main_time.text.toString().toDouble(),
                splitTime = BigDecimal(editTextNunberDecimal_main_distance.text.toString().toDouble()/editText_main_time.text.toString().toDouble()).setScale(2, RoundingMode.HALF_EVEN).toDouble()

            )

//


            log.ownerId = userId

            Backendless.Data.of(Logs::class.java).save(log, object : AsyncCallback<Logs?> {
                override fun handleResponse(response: Logs?) {
                    // new Contact instance has been saved
                    Toast.makeText(activity, "Log Saved", Toast.LENGTH_SHORT).show()
                    view.findNavController().navigate(R.id.action_addLogFragment_to_fragment_log_list)
                }

                override fun handleFault(fault: BackendlessFault) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Log.d("TAG", "handleFault: ${fault.message}")
                }
            })
        }
    }


    companion object {

    }
}