package ru.skillbranch.devintensive.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallBack
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initToolbar(){
        setSupportActionBar(toolbar)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer {
            chatAdapter.updateData(it)
        })
    }

    private fun initViews(){
        chatAdapter = ChatAdapter(listener = {
            Snackbar.make(rv_chats_list, it.title, Snackbar.LENGTH_SHORT).show()
        })
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallBack(chatAdapter) {
            viewModel.addToArchive(it.id)
            Snackbar.make(rv_chats_list, "Вы точно хотите добавить ${it.title} в архив", Snackbar.LENGTH_SHORT).show()
        }
        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(rv_chats_list)

        with(rv_chats_list){
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = chatAdapter
            addItemDecoration(divider)
        }

        add_chat_item.setOnClickListener {
            viewModel.addItems()
        }
    }
}
