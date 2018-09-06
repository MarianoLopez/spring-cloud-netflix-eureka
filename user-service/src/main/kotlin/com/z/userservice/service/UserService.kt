package com.z.userservice.service

import com.z.userservice.domain.User
import com.z.userservice.domain.UserDAO
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service("UserService")
class UserService(val userDAO: UserDAO, private val encoder: PasswordEncoder): UserDetailsService{
    fun update(user: User): User = userDAO.save(user.apply { this.password = encoder.encode(this.password) })
    fun insert(user: User): User = userDAO.insert(user.apply { this.password =  encoder.encode(this.password) })

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {//load user info (security core)
        if (username.isNotEmpty()){
            userDAO.findFirstByName(username)?.let {
                return org.springframework.security.core.userdetails.User(it.name,it.password,it.state,
                        true,true,true,
                        it.roles.map { role -> SimpleGrantedAuthority("ROLE_${role.name}") })
            }
        }
        throw UsernameNotFoundException("Username not found")
    }
}