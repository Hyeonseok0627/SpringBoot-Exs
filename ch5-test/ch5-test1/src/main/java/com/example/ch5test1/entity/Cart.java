package com.example.ch5test1.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne()
//    @OneToOne(fetch = FetchType.LAZY)
    // Member.java에 있는 Column name과 동일
    @JoinColumn(name="member_id")
    private Member member;

}