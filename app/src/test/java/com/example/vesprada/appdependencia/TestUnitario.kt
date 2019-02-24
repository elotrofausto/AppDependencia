package com.example.vesprada.appdependencia

import com.example.vesprada.appdependencia.Models.XAvisoModel
import com.example.vesprada.appdependencia.Utils.PdfFromXmlFile
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

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
    fun getFechaInMillis() {
        val date = Date()
        val aviso = XAvisoModel()
        aviso.fecDesde = date
        assertEquals(date.time, aviso.fecDesdeLong)
    }

}
