package com.example.ch5test1.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    // 지연 로딩
    @ManyToOne(fetch = FetchType.LAZY)
    // 즉시 로딩
//    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item item;

    // 지연 로딩
    @ManyToOne(fetch = FetchType.LAZY)
    // 즉시 로딩
//    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량

}