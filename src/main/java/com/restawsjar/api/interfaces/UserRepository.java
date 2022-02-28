package com.restawsjar.api.interfaces;

import com.restawsjar.api.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
