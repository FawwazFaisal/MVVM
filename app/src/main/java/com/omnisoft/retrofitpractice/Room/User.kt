package com.omnisoft.retrofitpractice.Room

/**
 * Created by S.M.Mubbashir.A.Z. on 3/29/2021.
 */
data class User(
        var name: String = "",
        var lastName: String = "",
        var email: String = "",
        var phoneNo: String = "",
        var fcmToken: String = "")