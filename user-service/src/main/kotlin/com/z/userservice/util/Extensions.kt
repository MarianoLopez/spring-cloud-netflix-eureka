package com.z.userservice.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.io.IOException
import java.util.concurrent.TimeUnit


fun String.asInts() = this.removeSurrounding("[","]").split(",").mapNotNull { it.toIntOrNull()}.toList() //String to List<Int>
fun String.asStrings() = this.removeSurrounding("[","]").split(",").map { it }.toList() //String to List<Int>

fun Map<String,String>.valuesToStringList () = this.mapValues { it.value.asStrings() }.toMutableMap()
fun Map<String,String>.valuesToIntList () = this.mapValues { it.value.asInts() }.toMutableMap()

/*date*/
fun LocalDate.asDate(): Date = Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
fun LocalDateTime.asDate():Date = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
fun Date.asLocalDate():LocalDate = Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()).toLocalDate()
fun Date.asLocalDateTime():LocalDateTime = Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()).toLocalDateTime()

fun String.run(timeoutInSeconds:Long = 5L):String{
    return try{
        ProcessBuilder("bash",*this.split("[\\r\\n]+").toTypedArray())
                .inheritIO()
                .start().apply { waitFor(timeoutInSeconds, TimeUnit.SECONDS) }.inputStream.bufferedReader().use { it.readText() }
    }catch (ioe: IOException){
        ioe.message ?: ioe.toString()
    }
}

inline fun <T> MutableList<T>.mapInPlace(mutator: (T)->T) {
    val iterate = this.listIterator()
    while (iterate.hasNext()) {
        val oldValue = iterate.next()
        val newValue = mutator(oldValue)
        if (newValue !== oldValue) {
            iterate.set(newValue)
        }
    }
}