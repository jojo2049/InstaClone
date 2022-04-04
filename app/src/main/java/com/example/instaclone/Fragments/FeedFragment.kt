package com.example.instaclone.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.MainActivity
import com.example.instaclone.Post
import com.example.instaclone.PostAdapter
import com.example.instaclone.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class FeedFragment : Fragment() {

    lateinit var rvPosts: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set up views and click listeners

        rvPosts = view.findViewById(R.id.rvPost)

        //1. create layout for each row (item_post.xml)
        //2. create data source for each row {this is Post class)
        //3. create adapter that will bridge data and row layout (PostAdapter)
        //4. set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        rvPosts.adapter = adapter

        //5. Set layout manager on RecyclerView
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    // Query for all post in server
    open fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //find all post objects
        query.include(Post.KEY_USER)
        query.addDescendingOrder("createdAt");
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    //something is wrong
                    Log.e(MainActivity.TAG, "error fetching posts")
                } else{
                    if (posts != null) {
                        for (post in posts) {

                            Log.i(TAG, "Post: " + post.getDescription() + " ,username: " + post.getUser()?.username)
                        }
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })

    }
    companion object {
        const val TAG = "FeedFragment"
    }
}