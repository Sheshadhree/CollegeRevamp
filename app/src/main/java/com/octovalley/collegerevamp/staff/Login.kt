package com.octovalley.collegerevamp.staff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.octovalley.collegerevamp.R
import com.octovalley.collegerevamp.model.Staff
import com.octovalley.collegerevamp.prevalent.Staffvalent
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var checkBox:com.rey.material.widget.CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkBox = login_remember_me
        Paper.init(this)


        login_login_btn.setOnClickListener{
            loginClient()
        }

        login_forget_password.setOnClickListener {
            val i  = Intent(this, ForgetPasswordActivity::class.java)
            i.putExtra("check","login")
            startActivity(i)
        }



    }

    private fun loginClient() {
        val phone = login_phone_num.text.toString()
        val password = login_password.text.toString()
        val collegeCode = login_college_code.text.toString()

        if(phone == null || password == null||collegeCode == null)
        {
            Toast.makeText(this,"Please enter the credentials!",Toast.LENGTH_LONG).show()

        }
        else
        {
            login_progress_bar.visibility = View.VISIBLE
            authorizeStaff(phone,password,collegeCode)
        }


    }

    private fun authorizeStaff(phone: String, password: String, collegeCode: String) {

        if(checkBox.isChecked)
        {
            Paper.book().write(Staffvalent.userPhoneKey,phone)
            Paper.book().write(Staffvalent.userPasswordKey,password)
            Paper.book().write(Staffvalent.userCollegeCode,collegeCode)
        }

        val databaseRef = FirebaseDatabase.getInstance().reference
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.child(collegeCode).child(phone).exists()) {
                    val usersData: Staff =
                        snapshot.child(collegeCode).child(phone).getValue(Staff::class.java)!!
                    if (usersData.phoneNumber == phone)
                    {
                        if (usersData.password == password)
                        {
                            if(usersData.collegeCode == collegeCode)
                            {
                                login_progress_bar.visibility = View.INVISIBLE
                                Toast.makeText(this@Login,"Welcome User , Logged in Successfully...!",Toast.LENGTH_LONG).show()
                                Staffvalent.currentOnlineStaff = usersData
                                val i = Intent (this@Login , Home :: class.java)
                                startActivity(i)

                            }
                            else
                            {
                                login_progress_bar.visibility = View.INVISIBLE

                                Toast.makeText(this@Login,"$collegeCode is  incorrect...!",Toast.LENGTH_LONG).show()
                            }
                        }
                        else
                        {
                            login_progress_bar.visibility = View.INVISIBLE

                            Toast.makeText(this@Login,"$password is  incorrect...!",Toast.LENGTH_LONG).show()
                        }
                    }
                    else
                    {
                        login_progress_bar.visibility = View.INVISIBLE

                        Toast.makeText(this@Login,"$phone is  not found...!",Toast.LENGTH_LONG).show()
                    }
                }
                else
                {

                    login_progress_bar.visibility = View.INVISIBLE

                    Toast.makeText(this@Login,"User is not Found...!",Toast.LENGTH_LONG).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}