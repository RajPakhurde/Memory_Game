package com.rajpakhurde.memorygame.models

import com.google.firebase.firestore.PropertyName

class UserImageList {
    @PropertyName("images") val images: List<String>? = null
}