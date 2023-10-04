package com.example.ch5test1.repository;


import com.example.ch5test1.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}