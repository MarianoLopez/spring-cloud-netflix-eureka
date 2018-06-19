package com.z.cloud.eurekaclient2.data

data class Person(var id:String?,val name:String){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Person
        if(other.id!=id) return false
        return true
    }
}