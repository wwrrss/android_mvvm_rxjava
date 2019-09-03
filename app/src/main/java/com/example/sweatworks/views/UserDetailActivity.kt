package com.example.sweatworks.views

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.sweatworks.R
import com.example.sweatworks.models.User
import com.example.sweatworks.utils.SweatApp
import com.example.sweatworks.viewmodels.UserDetailViewModel
import com.example.sweatworks.viewmodels.UserDetailViewModelFactory
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.content_user_detail.*
import javax.inject.Inject

class UserDetailActivity : AppCompatActivity() {

    companion object{
        const val USER_PARAM = "USER_PARAM"
    }

    @Inject
    lateinit var userDetailViewModelFactory: UserDetailViewModelFactory

    lateinit var userDetailViewModel: UserDetailViewModel

    var disposable = CompositeDisposable()

    var user:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SweatApp).appComponent.inject(this)
        userDetailViewModel = ViewModelProviders.of(this,userDetailViewModelFactory).get(UserDetailViewModel::class.java)
        setContentView(R.layout.activity_user_detail)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            insertFavUser()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent.extras?.let {bundle ->
            user = bundle.getSerializable(USER_PARAM) as? User
            setupUi()
        }
        observeUserFav()
    }

    private fun setupUi(){
        user?.let { u->
            Picasso.get().load(u.picture.large).fit().centerCrop().into(userDetailImageView)
            userDetailTitle.text = "${u.name.first} ${u.name.last}"
            userDetailEmail.text = u.email
            userDetailPhone.text = u.phone
        }
    }

    private fun observeUserFav(){
        user?.let {
            val d = userDetailViewModel.getUsersByEmail(it.email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result->
                    if(result.isEmpty()){
                        userDetailFavAnim.visibility = View.GONE
                    }else{
                        userDetailFavAnim.visibility = View.VISIBLE
                        userDetailFavAnim.playAnimation()
                        fab.hide()
                    }
                },{

                })
            disposable.add(d)
        }
    }

    private fun insertFavUser(){
        user?.let {
            it.isSaved = true
            val d = userDetailViewModel.insertFavUser(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showMessage("Fav user saved!")
                },{
                    showMessage("Something went wrong!")
                })
            disposable.add(d)
        }
    }

    private fun showMessage(message:String){
        Snackbar.make(fab,message,Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
