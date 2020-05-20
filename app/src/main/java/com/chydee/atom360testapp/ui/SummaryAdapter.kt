package com.chydee.atom360testapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chydee.atom360testapp.R
import com.chydee.atom360testapp.data.pojo.Summary
import com.chydee.atom360testapp.ui.diffUtil.AutoUpdatableAdapter
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class SummaryAdapter internal constructor(val context: Context) :
    RecyclerView.Adapter<SummaryAdapter.ViewHolder>(),
    AutoUpdatableAdapter, Filterable {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items: ArrayList<Summary.SummaryItem> by Delegates.observable(ArrayList()) { prop, oldList, newList ->
        autoNotify(oldList, newList) { o, n -> o.country == n.country }
    }
    private lateinit var itemsFilter: MutableList<Summary.SummaryItem>
    var DURATION: Long = 500
    private var onAttach = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.summary_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val current = items[position]
        holder.country.text = current.country
        holder.totalDead.text = context.getString(R.string.total_dead, current.deaths.toString())
        holder.totalConfirmed.text = current.cases.toString()
        holder.totalRecovered.text = current.recovered.toString()
        holder.newlyConfirmed.text = current.tests.toString()
        Picasso.get().load(current.countryInfo.flag).into(holder.countryFlag)
        leftToRightAnimation(itemView = holder.itemView, i = position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val country: TextView = itemView.findViewById(R.id.country_name)
        val totalDead: TextView = itemView.findViewById(R.id.total_dead)
        val totalConfirmed: TextView = itemView.findViewById(R.id.total_confirmed)
        val totalRecovered: TextView = itemView.findViewById(R.id.total_recovered)
        val newlyConfirmed: TextView = itemView.findViewById(R.id.new_confirmed)
        val countryFlag: ImageView = itemView.findViewById(R.id.country_flag)
    }

    internal fun setItems(summary: ArrayList<Summary.SummaryItem>) {
        this.items = summary
        itemsFilter = ArrayList(summary)
    }

    private fun leftToRightAnimation(itemView: View, i: Int) {
        var i = i
        if (!onAttach) {
            i = -1
        }
        val notFirstItem = i == -1
        i += 1
        itemView.translationX = -400f
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateY =
            ObjectAnimator.ofFloat(itemView, "translationX", -400f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animatorTranslateY.startDelay = if (notFirstItem) DURATION else i * DURATION
        animatorTranslateY.duration = (if (notFirstItem) 2 else 1) * DURATION
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

    override fun getFilter(): Filter = itemFilter

    private val itemFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<Summary.SummaryItem> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(itemsFilter)
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase(Locale.ENGLISH).trim { it <= ' ' }
                for (item in itemsFilter) {
                    if (item.country.toLowerCase(Locale.ENGLISH).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            items = results.values as ArrayList<Summary.SummaryItem>
        }
    }
}

