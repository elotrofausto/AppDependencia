package com.example.vesprada.appdependencia.AdaptersAndClasses

import java.util.Date

class tareas_object(var img: Int, var descripcionPequenya: String?, var fecha: Date?, var descripcionCompleta: String?){

    fun getHora() : String{
        var date : String = fecha.toString()
        var array : List<String> = date.split(" ")
        return array[3].split(":")[0] + ":" + array[3].split(":")[0]
    }

}
