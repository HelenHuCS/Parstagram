package com.example.parstagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Post")
class Post: ParseObject() {
    fun getDerscription():String? {
        return getString(KEY_DESCRIPTION)
    }

    fun setDescription(des:String) {
        put(KEY_DESCRIPTION,des)
    }

    fun setImage(parseFile: ParseFile) {
        put(KEY_IMAGE,parseFile)
    }

    fun getImage():ParseFile? {
        return getParseFile(KEY_IMAGE)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }

    fun setUser(parseUser: ParseUser) {
        put(KEY_USER,parseUser)
    }

    companion object {
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
    }
}