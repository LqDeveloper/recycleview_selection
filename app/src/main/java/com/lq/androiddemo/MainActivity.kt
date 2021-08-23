package com.lq.androiddemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.lq.androiddemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MainAdapter
    var tracker: SelectionTracker<Long>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        binding.recycle.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = MainAdapter()
        binding.recycle.adapter = adapter
        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            binding.recycle,
            MyItemKeyProvider(binding.recycle),
            MyItemDetailsLookup(binding.recycle),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        adapter.tracker = tracker
        adapter.notifyDataSetChanged()

        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                tracker?.let {
                    Log.d("Main-Size", " ${it.selection.toString()}");
                }
            }
        })
    }
}