package com.demo.kotlin.test

//局部函数
class Tip7LocalFunc {
    //重复的代码
    fun saveUser(user: Stu) {
        if (user.name.isEmpty()) {
            throw IllegalArgumentException("Can't save user ${user.id}: empty Name")
        }
        if (user.address.isEmpty()) {
            throw IllegalArgumentException("Can't save user ${user.id}: empty Address")
        }
        if (user.email.isEmpty()) {
            throw IllegalArgumentException("Can't save user ${user.id}: empty Email")
        }
        //save to db ...
    }

    fun saveUser2(user: Stu) {
        fun validate(value: String, field: String) {
            if (value.isEmpty()) {
                throw IllegalArgumentException("Can't save user ${user.id}: empty $field")
            }
        }
        validate(user.name, "Name")
        validate(user.address, "Address")
        validate(user.email, "Email")
        //save to db
    }

    //拓展函数
    fun Stu.validateAll() {
        fun validate(value: String, field: String) {
            if (value.isEmpty()) {
                throw IllegalArgumentException("Can't save user ${id}: empty $field")
            }
        }
        validate(name, "Name")
        validate(address, "Address")
        validate(email, "Email")
    }

    fun saveUser3(user: Stu) {
        user.validateAll()
        //save to db
    }
}

class Stu(val id: Int, val name: String, val address: String, val email: String)