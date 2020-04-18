package ru.skillbranch.devintensive.ui.group

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.activity_group.toolbar
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.ui.adapters.UserAdapter
import ru.skillbranch.devintensive.viewmodels.GroupViewModel

class GroupActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var groupViewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        initToolBar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView?
        searchView?.queryHint = "Введите имя пользователя"
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                groupViewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                groupViewModel.handleSearchQuery(text)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        }else{
            super.onOptionsItemSelected(item)
        }
    }

    private fun initToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews(){
        userAdapter = UserAdapter { groupViewModel.handleSelectedItem(it.id) }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        with(users_list){
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@GroupActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            groupViewModel.handleCreateGroup()
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
        }
    }

    private fun initViewModel(){
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
        groupViewModel.getUserData().observe(this, Observer { userAdapter.updateUserdata(it) })
        groupViewModel.getSelectedData().observe(this, Observer {
            updateChips(it)
            toggleFab(it.size > 1)
        })
    }

    private fun toggleFab(isShow: Boolean) {
        if(isShow) fab.show()
        else fab.hide()
    }

    private fun addChipToGroup(user: UserItem){
        val chip = Chip(this).apply {
            text = user.fullName
            chipIcon = resources.getDrawable(R.drawable.avatar_default, theme)
            isCloseIconVisible = true
            tag = user.id
            isCheckable = true
            closeIconTint = ColorStateList.valueOf(Color.WHITE)
            chipBackgroundColor = ColorStateList.valueOf(getColor(R.color.color_primary_light))
            setTextColor(Color.WHITE)
        }
        chip.setOnCloseIconClickListener { groupViewModel.handleRemoveChip(it.tag.toString()) }
        chip_group.addView(chip)
    }

    private fun updateChips(listUsers: List<UserItem>){
        chip_group.visibility = if(listUsers.isEmpty()) View.GONE else View.VISIBLE

        val users = listUsers
            .associate { user -> user.id to user }
            .toMutableMap()

        val views = chip_group.children.associate { view -> view.tag to view }

        for((k,v) in views){
            if(!users.containsKey(k)) chip_group.removeView(v)
            else users.remove(k)
        }

        users.forEach { (_,v) -> addChipToGroup(v) }
    }
}
