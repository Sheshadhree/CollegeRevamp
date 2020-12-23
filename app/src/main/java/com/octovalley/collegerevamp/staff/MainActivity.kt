package com.octovalley.collegerevamp.staff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.octovalley.collegerevamp.R
import com.octovalley.collegerevamp.admin.AdminLoginActivity
import com.octovalley.collegerevamp.model.Staff
import com.octovalley.collegerevamp.prevalent.Staffvalent
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_join_now_btn.setOnClickListener {
            val i = Intent(this, JoinNow::class.java)
            startActivity(i)
        }
        main_login_btn.setOnClickListener {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }

        main_admin_login.setOnLongClickListener(object:View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                val i = Intent(this@MainActivity, AdminLoginActivity::class.java)
                startActivity(i)
                return true
            }

        })

        Paper.init(this)
        val phone = Paper.book().read<String>(Staffvalent.userPhoneKey)
        val password = Paper.book().read<String>(Staffvalent.userPasswordKey)
        val collegeCode = Paper.book().read<String>(Staffvalent.userCollegeCode)

        if(phone!=null && password!=null&&collegeCode!=null)
        {
            if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(collegeCode))
            {
                authorizeStaff(phone,password,collegeCode)
            }
        }

    }

    private fun authorizeStaff(phone: String, password: String, collegeCode: String) {


        val databaseRef = FirebaseDatabase.getInstance().reference
        databaseRef.addValueEventListener(object : ValueEventListener {
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
                                Toast.makeText(this@MainActivity,"Welcome User , Logged in Successfully...!", Toast.LENGTH_LONG).show()
                                Staffvalent.currentOnlineStaff = usersData
                                val i = Intent (this@MainActivity , Home :: class.java)
                                startActivity(i)

                            }
                            else
                            {

                                Toast.makeText(this@MainActivity,"$collegeCode is  incorrect...!", Toast.LENGTH_LONG).show()
                            }
                        }
                        else
                        {

                            Toast.makeText(this@MainActivity,"$password is  incorrect...!", Toast.LENGTH_LONG).show()
                        }
                    }
                    else
                    {

                        Toast.makeText(this@MainActivity,"$phone is  not found...!", Toast.LENGTH_LONG).show()
                    }
                }
                else
                {


                    Toast.makeText(this@MainActivity,"User is not Found...!", Toast.LENGTH_LONG).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}