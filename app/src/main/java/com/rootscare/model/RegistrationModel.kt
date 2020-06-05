package com.rootscare.model

import java.io.File

 class RegistrationModel(var userType: String?=null,
                        var firstName: String?=null,
                        var lastName: String?=null,
                        var emailAddress: String?=null,
                        var mobileNumber: String?=null,
                        var dob: String?=null,
                        var gender: String?=null,
                        var password:String?=null,
                        var confirmPassword: String?=null,
                        var imageFile: File?=null,
                        var imageName: String?=null,
                        var certificateFile: File?=null,
                        var certificatename: String?=null,
                         var qualification: String?=null,
                        var passingYear: String?=null,
                        var institude: String?=null,
                        var description: String?=null,
                        var experience : String?=null,
                        var availableTime : String?=null,
                        var fees: String?=null,
                         var department: String?=null)