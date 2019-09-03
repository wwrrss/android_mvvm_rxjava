package com.example.sweatworks.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.sweatworks.R
import com.example.sweatworks.models.User
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject

class UsersRecyclerAdapter:RecyclerView.Adapter<UsersViewHolder>(){

    companion object{
        const val TYPE_API = 1;
        const val TYPE_SAVED = 2;
    }

    var onClickObservable = PublishSubject.create<User>()

    private var data:MutableList<User> = mutableListOf()

    fun addData(newData:MutableList<User>){
        data.addAll(newData)

    }

    fun replaceData(newData:MutableList<User>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount():Int{
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return if(item.isSaved) TYPE_SAVED else TYPE_API
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = data[position]
        Picasso.get().load(item.picture.medium).fit().into(holder.imageViewUser)
        holder.imageViewUser.setOnClickListener {
            onClickObservable.onNext(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val viewValue:Int
        if(viewType == TYPE_API){
            viewValue = R.layout.main_list_row
        }else{
            viewValue = R.layout.main_list_saved_row
        }
        val view = LayoutInflater.from(parent.context).inflate(viewValue,parent,false)
        return UsersViewHolder(view)
    }
}

class UsersViewHolder(private val view:View):RecyclerView.ViewHolder(view){
    val imageViewUser:ImageView = view.findViewById(R.id.mainListUserImage)
}

