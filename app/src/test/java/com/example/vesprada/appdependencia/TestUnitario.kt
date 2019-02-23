package com.example.vesprada.appdependencia

import android.app.Activity
import android.content.Context
import com.example.vesprada.appdependencia.Activities.LoginActivity
import com.example.vesprada.appdependencia.Activities.RedButtonActivity
import com.example.vesprada.appdependencia.Utils.CreateIntent
import com.example.vesprada.appdependencia.Utils.PdfFromXmlFile
import kotlinx.android.synthetic.main.activity_login.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class TestUnitario {

    //lateinit var createIntent: CreateIntent
    var pdfFromXmlFile: PdfFromXmlFile? = null

    @Before
    fun setUp(){
        //createIntent = CreateIntent()


    }

    @Test
    fun createReportUrl(){

            pdfFromXmlFile = PdfFromXmlFile(1,"28888810k")
            assertEquals(pdfFromXmlFile!!.url.toString(), "http://149.202.8.235:8080/jasperserver/rest_v2/reports/reports/grup2/InformeMedicamentoList.pdf?j_username=grup2&j_password=Grupo-312&user=28888810k")
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
