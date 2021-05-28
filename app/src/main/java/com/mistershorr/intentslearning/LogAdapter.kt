package com.mistershorr.intentslearning

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mistershorr.intentslearning.Logs

class LogAdapter(private val logsList: List<Logs?>?) :RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view  = LayoutInflater.from(parent.context).inflate(R.layout.log_item,parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        if (logsList != null) {
            return logsList.size
        }
        return 0
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Log.d("Response", "List Count :${logsList.size} ")


        return holder.bind(logsList?.get(position)!!)


    }
    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView) {


        //var imageView = itemView.findViewById<ImageView>(R.id.ivFlag)
        var runText = itemView.findViewById<TextView>(R.id.run_textView_distance)
        var distanceText = itemView.findViewById<TextView>(R.id.run_textView_run)
        var time = itemView.findViewById<TextView>(R.id.run_textView_time)
        fun bind(log: Logs) {

            val name ="Cases :${log.distance.toString()}"
            distanceText.text = log.distance.toString() + " Miles"
            runText.text = log.name
            time.text = log.time.toString() + " Minutes"

            //Picasso.get().load(country.countryInfo.flag).into(imageView)
            itemView.setOnClickListener {
                val specificLogIntent = Intent(itemView.context, LogDetailActivity::class.java)
                specificLogIntent.putExtra(LogDetailActivity.EXTRA_LOG_NAME, log.name)
                specificLogIntent.putExtra(LogDetailActivity.EXTRA_LOG_DISTANCE, log.distance.toString())
                specificLogIntent.putExtra(LogDetailActivity.EXTRA_LOG_SPLIT, log.splitTime.toString())
                specificLogIntent.putExtra(LogDetailActivity.EXTRA_LOG_TIME, log.time.toString())
                specificLogIntent.putExtra(LogDetailActivity.EXTRA_LOG_COMMENT, log.notes)
                specificLogIntent.putExtra(LogDetailActivity.EXTRA_LOG_ID, log.objectId.toString())
                Log.d("Asf", log.objectId.toString())
                itemView.context.startActivity(specificLogIntent)
            }
        }

    }
}