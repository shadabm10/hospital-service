package com.rootscare.data.datasource.sharedpref

import android.content.Context
import android.content.SharedPreferences

class AppSharedPref(context: Context, prefFileName: String) {

    private val mPrefs: SharedPreferences


    companion object {

        private val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"

        private val PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL"

        private val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"

        private val PREF_KEY_USER_TYPE = "PREF_KEY_USER_TYPE"

        private val PREF_KEY_TRINNER_ID = "PREF_KEY_TRINNER_ID"

        private val PREF_KEY_fOR_INTRO_PAGE = "PREF_KEY_fOR_INTRO_PAGE"

        private val PREF_KEY_LOGIN_USER_DATA = "PREF_KEY_LOGIN_USER_DATA"
        private val PREF_KEY_LOGIN_STUDENT_NAME = "PREF_KEY_LOGIN_STUDENT_NAME"
        private val PREF_KEY_STUDENT_REFERAL_CODE = "PREF_KEY_STUDENT_REFERAL_CODE"
        private val PREF_KEY_LOGIN_STUDENT_IMAGE = "PREF_KEY_LOGIN_STUDENT_IMAGE"
        private val PREF_KEY_LOGIN_STUDENT_EMAIL = "PREF_KEY_LOGIN_STUDENT_EMAIL"
        private val PREF_KEY_LOGIN_STUDENT_LOGIN_PASSWORD = "PREF_KEY_LOGIN_STUDENT_LOGIN_PASSWORD"

        //NEW DATA ADDED
        private val PREF_KEY_LOGIN_MODEL_DATA = "PREF_KEY_LOGIN_MODEL_DATA"
        private val PREF_KEY_LOGIN_USER_TYPE = "PREF_KEY_LOGIN_USER_TYPE"
        private val PREF_KEY_IS_LOGIN_REMEMBER = "PREF_KEY_IS_LOGIN_REMEMBER"
        private val PREF_KEY_IS_LOGIN_USERID = "PREF_KEY_IS_LOGIN_USERID"


    }
    init {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    fun deleteCurrentUserEmail() {
        mPrefs.edit().remove(PREF_KEY_CURRENT_USER_EMAIL).apply()
    }

    //Save Login Model Data
    var loginmodeldata: String?
        get() = mPrefs.getString(PREF_KEY_LOGIN_MODEL_DATA, null)
        set(loginmodeldata) = mPrefs.edit().putString(PREF_KEY_LOGIN_MODEL_DATA, loginmodeldata).apply()

    fun deleteLoginModelData() {
        mPrefs.edit().remove(PREF_KEY_LOGIN_MODEL_DATA).apply()
    }

    var loginUserType: String?
        get() = mPrefs.getString(PREF_KEY_LOGIN_USER_TYPE, null)
        set(loginUserType) = mPrefs.edit().putString(PREF_KEY_LOGIN_USER_TYPE, loginUserType).apply()

    fun deleteLoginUserType() {
        mPrefs.edit().remove(PREF_KEY_LOGIN_USER_TYPE).apply()
    }

    var loginUserId: String?
        get() = mPrefs.getString(PREF_KEY_IS_LOGIN_USERID, null)
        set(loginUserId) = mPrefs.edit().putString(PREF_KEY_IS_LOGIN_USERID, loginUserId).apply()

    fun deleteLoginUserId() {
        mPrefs.edit().remove(PREF_KEY_IS_LOGIN_USERID).apply()
    }

    //Login Remember

    var isloginremember: String?
        get() = mPrefs.getString(PREF_KEY_IS_LOGIN_REMEMBER, null)
        set(isloginremember) = mPrefs.edit().putString(PREF_KEY_IS_LOGIN_REMEMBER, isloginremember).apply()
    fun deleteIsLogINRemember() {
        mPrefs.edit().remove(PREF_KEY_IS_LOGIN_REMEMBER).apply()
    }


    var accessToken: String?
        get() = mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null)
        set(accessToken) = mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply()


    var currentUserEmail: String?
        get() = mPrefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null)
        set(email) = mPrefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply()

    var userId: String?
        get() = mPrefs.getString(PREF_KEY_USER_ID, null)
        set(email) = mPrefs.edit().putString(PREF_KEY_USER_ID, email).apply()

    var userType: String?
        get() = mPrefs.getString(PREF_KEY_USER_TYPE, null)
        set(usertype) = mPrefs.edit().putString(PREF_KEY_USER_TYPE, usertype).apply()
    var trainnerid: String?
        get() = mPrefs.getString(PREF_KEY_TRINNER_ID, null)
        set(trainnerid) = mPrefs.edit().putString(PREF_KEY_TRINNER_ID, trainnerid).apply()


    var isIntoPageOn: Boolean
        get() = mPrefs.getBoolean(PREF_KEY_fOR_INTRO_PAGE, true)
        set(isOn) = mPrefs.edit().putBoolean(PREF_KEY_fOR_INTRO_PAGE, isOn).apply()

    var loginUserData: String?
        get() = mPrefs.getString(PREF_KEY_LOGIN_USER_DATA, null)
        set(email) = mPrefs.edit().putString(PREF_KEY_LOGIN_USER_DATA, email).apply()

    var studentName: String?
        get() = mPrefs.getString(PREF_KEY_LOGIN_STUDENT_NAME, null)
        set(studentName) = mPrefs.edit().putString(PREF_KEY_LOGIN_STUDENT_NAME, studentName).apply()

    var studentreferalcode: String?
        get() = mPrefs.getString(PREF_KEY_STUDENT_REFERAL_CODE, null)
        set(studentreferalcode) = mPrefs.edit().putString(PREF_KEY_STUDENT_REFERAL_CODE, studentreferalcode).apply()
    var farmerEmail: String?
        get() = mPrefs.getString(PREF_KEY_LOGIN_STUDENT_EMAIL, null)
        set(farmerEmail) = mPrefs.edit().putString(PREF_KEY_LOGIN_STUDENT_EMAIL, farmerEmail).apply()

    var studentLogInPassword: String?
        get() = mPrefs.getString(PREF_KEY_LOGIN_STUDENT_LOGIN_PASSWORD, null)
        set(studentLogInPassword) = mPrefs.edit().putString(PREF_KEY_LOGIN_STUDENT_LOGIN_PASSWORD, studentLogInPassword).apply()

    var studentProfileImage: String?
        get() = mPrefs.getString(PREF_KEY_LOGIN_STUDENT_IMAGE, null)
        set(studentProfileImage) = mPrefs.edit().putString(PREF_KEY_LOGIN_STUDENT_IMAGE, studentProfileImage).apply()



    fun deleteUserId() {
        mPrefs.edit().remove(PREF_KEY_USER_ID).apply()
    }
    fun deleteUserName() {
        mPrefs.edit().remove(PREF_KEY_LOGIN_STUDENT_NAME).apply()
    }
    fun deleteUserEmail() {
        mPrefs.edit().remove(PREF_KEY_LOGIN_STUDENT_EMAIL).apply()
    }
    fun deleteUserImage() {
        mPrefs.edit().remove(PREF_KEY_LOGIN_STUDENT_EMAIL).apply()
    }

    fun deleteUserType() {
        mPrefs.edit().remove(PREF_KEY_USER_TYPE).apply()
    }
    fun deleteStudentLogInPassword() {
        mPrefs.edit().remove(PREF_KEY_LOGIN_STUDENT_LOGIN_PASSWORD).apply()
    }

}
