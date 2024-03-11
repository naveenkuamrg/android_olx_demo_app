package com.application.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.application.entity.InterestedList
import com.application.entity.Notification
import com.application.entity.ProductDetails
import com.application.entity.User
import com.application.entity.WishList
import com.application.entity.relations.NotificationWithProductDetails
import com.application.entity.relations.NotificationWithUser
import com.application.entity.relations.ProductsWithInterestedProfile
import com.application.entity.relations.ProfileWithInterestedProducts
import com.application.entity.relations.ProfileWithWishListProducts

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user : User)


    @Query("select * from user")
    suspend fun getProfile() : List<User>


    @Insert
    suspend fun insertProduct(product : ProductDetails)


    @Query("select * from product_details")
    fun getProduct() : ProductDetails


    @Insert
    fun insertWishList(wishList: WishList)


    @Query("select * from user where user_id Like :id")
    fun getWishList(id : Long) : ProfileWithWishListProducts



    @Insert
    fun insertInterestedList(interestedList: InterestedList)

    @Query("select * from product_details where product_id Like :id")
    fun getProductWithProfile(id : Long) : ProductsWithInterestedProfile


    @Query("select * from user where user_id Like :id")
    fun getProfileWithProduct(id : Long) : ProfileWithInterestedProducts


    @Insert
    fun insertNotification(notification : Notification)


    @Query("select * from notification where recipientId Like :id")
    fun getNotification(id : Long) : List<Notification>


    @Query("select * from notification where id  Like :id")
    fun getNotificationWithProfile(id : Long) : NotificationWithUser

    @Query("select * from notification where id  Like :id")
    fun getNotificationWithProduct(id : Long) : NotificationWithProductDetails

    @Query("select email from user where email LIKE :email")
    fun getEmail(email: String) : String


    //getUserId in signIn
    @Query("select user_id from user where email LIKE :email")
    fun getUserId(email: String) : Long

    @Query("select password from user where email LIKE :email")
    fun getUserPassword(email : String) : String

    @Query("select EXISTS(select email from user where email LIKE :email)")
    fun isEmailExist(email: String) : Boolean

    @Query("select EXISTS(select email from user where email LIKE :email and password LIKE :password)")
    fun isPasswordMatch(email: String,password: String) : Boolean

    @Query("select * from user where email LIKE :email")
    fun getUser(email: String) : User?
}
