package com.mistershorr.intentslearning

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_log_detail.*
import kotlinx.android.synthetic.main.fragment_log_list.*

class LogDetailActivity : AppCompatActivity() {

    val TAG = "FactDetailActivity"
    private var logList: List<Logs?>? = listOf()
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_detail)
        detail_textView_name.text = intent.getStringExtra(EXTRA_LOG_NAME)
        detail_textView_dist.text = intent.getStringExtra(EXTRA_LOG_DISTANCE)
        detail_textView_time.text = intent.getStringExtra(EXTRA_LOG_TIME)
        detail_textView_split.text = intent.getStringExtra(EXTRA_LOG_SPLIT)
        detail_textView_comment.text = intent.getStringExtra(EXTRA_LOG_COMMENT)
        val userId = Backendless.UserService.CurrentUser().userId
        display_button_deleteLog.setOnClickListener() {
            val whereClause = "ownerId = '$userId'"
            val queryBuilder = DataQueryBuilder.create()
            queryBuilder.whereClause = whereClause


            Backendless.Data.of(Logs::class.java).find(queryBuilder,
                object : AsyncCallback<List<Logs?>?> {
                    override fun handleResponse(foundLogs: List<Logs?>?) {
                        // the "foundGrades" collection now contains instances of the Contact class.
                        // each instance represents an object stored on the server.
                        if (foundLogs != null) {
                            for (nlog in foundLogs) {
                                if (nlog != null) {
                                    Log.d(TAG, nlog.objectId.toString())
                                    Log.d(TAG, EXTRA_LOG_ID)
                                    if (nlog.objectId.toString().equals(intent.getStringExtra(EXTRA_LOG_ID))) {
                                        Backendless.Data.of(Logs::class.java).remove(nlog,
                                            object : AsyncCallback<Long?> {
                                                override fun handleResponse(response: Long?) {
                                                    Log.d(
                                                        TAG,
                                                        "handleResponse: item deleted at $response"
                                                    )
                                                    val fragmentIntent =
                                                        Intent(this@LogDetailActivity, FragmentActivity::class.java)
                                                    startActivity(fragmentIntent)
                                                    //close login screen

                                                    finish()
                                                    // Contact has been deleted. The response is the
                                                    // time in milliseconds when the object was deleted
                                                }

                                                override fun handleFault(fault: BackendlessFault) {
                                                    // an error has occurred, the error code can be
                                                    // retrieved with fault.getCode()
                                                    Log.d(TAG, "handleFault: ${fault.message}")
                                                }
                                            })
                                    }

                                }
                            }
                        }
                        Log.d(fragment_log_list.TAG, "found: " + logList.toString())


                    }

                    override fun handleFault(fault: BackendlessFault) {
                        Log.d(fragment_log_list.TAG, fault.toString())
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                    }
                })

        }
    }


    companion object {
        val EXTRA_LOG_NAME = "name"
        val EXTRA_LOG_DISTANCE = "distance"
        val EXTRA_LOG_TIME = "time"
        val EXTRA_LOG_SPLIT = "split"
        val EXTRA_LOG_COMMENT = "comment"
        val EXTRA_LOG_ID = "id"
    }
}