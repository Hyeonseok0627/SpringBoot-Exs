package com.example.ch5test1.entity;

import com.example.ch5test1.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 즉시 로딩 테스트 내용
//    @ManyToOne()
    // 지연 로딩 테스트 내용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // lazy로 되어있으면 연관성 있는 것만 조회하게 함(테스트 눌러보면서 차이점을 확인하기)
    // 지연 로딩 테스트 내용
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)

    // 고아객체 테스트하기위해 설정(즉시 로딩 테스트 내용)
//   @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
//           , orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // orderItems이라는 속성이 Order 엔티티 클래스에 있음

}