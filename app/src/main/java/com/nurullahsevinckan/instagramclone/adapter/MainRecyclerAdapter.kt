package com.nurullahsevinckan.instagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.green
import androidx.recyclerview.widget.RecyclerView
import com.nurullahsevinckan.instagramclone.databinding.RecyclerRowBinding
import com.nurullahsevinckan.instagramclone.model.Post

class MainRecyclerAdapter(var postList : List<Post>) : RecyclerView.Adapter<PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text = postList.get(position).userMail
        holder.binding.recyclerContentText.text = postList.get(position).postDescription
    }
}