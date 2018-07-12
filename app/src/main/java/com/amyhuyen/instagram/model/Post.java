package com.amyhuyen.instagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_HANDLE = "handle";
    private static final String KEY_CREATED = "createdAt";
    private static final String KEY_PROFILE = "profilePic";


    // accessor and mutator for description
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    // accessor and mutator for image
    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    // accessor and mutator for profile images
    public ParseFile getProfileImage(){ return getUser().getParseFile(KEY_PROFILE);}

    // accessor and mutator for user
    public ParseUser getUser() {
        return super.getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    // accessor for handle
    public String getHandle(){
        return getUser().getString(KEY_HANDLE);
    }

    public static class Query extends ParseQuery<Post> {
        public Query(){
            super(Post.class);
        }
        public Query getTop(){
            setLimit(20);
            orderByDescending(KEY_CREATED);
            return this;
        }

        public Query withUser(){
            include("user");
            return this;
        }
    }
}
