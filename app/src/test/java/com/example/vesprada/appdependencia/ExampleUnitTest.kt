package com.example.vesprada.appdependencia

import android.app.Activity
import com.example.vesprada.appdependencia.Activities.LoginActivity
import com.example.vesprada.appdependencia.Activities.RedButtonActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    lateinit var loginActivity: LoginActivity
    lateinit var RedButtonActivity: RedButtonActivity

    @Before
    fun setUp(){
        loginActivity = LoginActivity()
        RedButtonActivity = RedButtonActivity()
    }

    @Test
    fun autentication_worked(){
        loginActivity.testLogin("28888810k", "1234")
        assertEquals(loginActivity.loginPb.tag.toString(), "LoginCorrect")
    }

    @Test
    fun autentication_failed(){
        loginActivity.testLogin("1234", "1234")
        assertEquals(loginActivity.loginPb.tag.toString(), "LoginIncorrect")
    }

    @Test
    fun cambioTemaADia(){
        assertEquals(RedButtonActivity.cambiarModoNocturnoDiurno(true), true)
    }

    @Test
    fun cambioTemaNoche(){
        assertEquals(RedButtonActivity.cambiarModoNocturnoDiurno(false), false)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
