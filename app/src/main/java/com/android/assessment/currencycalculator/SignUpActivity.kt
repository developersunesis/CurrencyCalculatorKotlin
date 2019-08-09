package com.android.assessment.currencycalculator

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

/*Signature: Uche Emmanuel
 * Developersunesis*/

/**
 * Please note, this activity is just a backend camouflage
 * The form doesn't really submit to any server or so
 * But that cant be easy to implement
 * Ofcourse you might have to do that (smiles)
 */

class SignUpActivity : AppCompatActivity() {

    /*
    *   Declare all the views from layout
     */
    internal lateinit var signup: Button
    internal lateinit var email: EditText
    internal lateinit var password: EditText
    internal lateinit var confirm_password: EditText
    internal lateinit var close: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup = findViewById(R.id.signup)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirm_password = findViewById(R.id.confirm_password)
        close = findViewById(R.id.close)

        //set close onClickListener
        close.setOnClickListener { finish() }

        //set signup onClickListener
        signup.setOnClickListener {
            if (!email.text.toString().isEmpty() && !password.text.toString().isEmpty() && !confirm_password.text.toString().isEmpty()) {
                if (password.text.toString() == confirm_password.text.toString()) {
                    if (ValidateEmail.isValidEmail(email.text.toString())) {
                        Toast.makeText(applicationContext, "Registration Complete!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Please enter a valid email address!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Password do not match!", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "Please complete the form!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
