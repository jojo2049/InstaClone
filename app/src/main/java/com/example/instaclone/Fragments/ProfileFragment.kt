package com.example.instaclone.Fragments

import android.util.Log
import com.example.instaclone.MainActivity
import com.example.instaclone.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: FeedFragment() {

    override fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //find all post objects
        query.include(Post.KEY_USER)
        //only return post for current user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
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
}