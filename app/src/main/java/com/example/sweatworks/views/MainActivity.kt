package com.example.sweatworks.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sweatworks.R
import com.example.sweatworks.models.User
import com.example.sweatworks.utils.SweatApp
import com.example.sweatworks.utils.UsersRecyclerAdapter
import com.example.sweatworks.viewmodels.UserListViewModel
import com.example.sweatworks.viewmodels.UserListViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var disposables = CompositeDisposable()

    @Inject
    lateinit var userListViewModelFactory:UserListViewModelFactory

    lateinit var userListViewModel: UserListViewModel

    lateinit var usersRecyclerAdapter: UsersRecyclerAdapter

    lateinit var savedUsersRecyclerAdapter: UsersRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as SweatApp).appComponent.inject(this)
        userListViewModel = ViewModelProviders.of(this,userListViewModelFactory).get(UserListViewModel::class.java)
        usersRecyclerAdapter = UsersRecyclerAdapter()
        savedUsersRecyclerAdapter = UsersRecyclerAdapter()
        recyclerSetup()
        savedUserRecyclerSetup()
        subscribeToApiData()
        observeSavedUsers()

    }

    private fun savedUserRecyclerSetup(){
        mainActivityFavUsersRecycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        mainActivityFavUsersRecycler.adapter = savedUsersRecyclerAdapter
        mainActivityFavUsersRecycler.setHasFixedSize(true)
        val d = savedUsersRecyclerAdapter.onClickObservable.subscribe{
            startDetailWithUser(it)
        }
        disposables.add(d)
    }
    private fun recyclerSetup(){
        mainActivityRecyclerView.layoutManager = GridLayoutManager(this,4)
        mainActivityRecyclerView.setHasFixedSize(true)
        mainActivityRecyclerView.adapter = usersRecyclerAdapter
        val d = usersRecyclerAdapter.onClickObservable.subscribe {
            startDetailWithUser(it)
        }
        disposables.add(d)
        mainActivityRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(!recyclerView.canScrollVertically(1) && dy !=0){
                    Toast.makeText(this@MainActivity,"LOADING MORE ITEMS",Toast.LENGTH_LONG).show()
                    userListViewModel.loadMore()
                }
            }
        })
    }
    private fun startDetailWithUser(user:User){
        var intent = Intent(this,UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.USER_PARAM,user)
        startActivity(intent)
    }

    private fun subscribeToApiData(){
        val d = userListViewModel.usersObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(usersRecyclerAdapter.itemCount==0){
                    usersRecyclerAdapter.addData(it.first.toMutableList())
                }else{
                    val end = 100 * it.second
                    val start = end - 100
                    usersRecyclerAdapter.addData(it.first.subList(start,end-1).toMutableList())
                }
                usersRecyclerAdapter.notifyDataSetChanged()
            },{

            })
        disposables.add(d)
    }

    private fun observeSavedUsers(){
        val d = userListViewModel.getAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.isEmpty()){
                    setSavedUsersSectionVisibility(View.GONE)
                }else{
                    setSavedUsersSectionVisibility(View.VISIBLE)
                    savedUsersRecyclerAdapter.replaceData(it.toMutableList())
                }
            },{

            })
        disposables.add(d)
    }
    private fun setSavedUsersSectionVisibility(visibility:Int){
        mainActivityFavUsersRecycler.visibility = visibility
        mainActivityTitleSaved.visibility = visibility
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
