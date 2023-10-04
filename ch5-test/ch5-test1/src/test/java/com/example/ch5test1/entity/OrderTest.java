package com.example.ch5test1.entity;


import com.example.ch5test1.constant.ItemSellStatus;
import com.example.ch5test1.repository.ItemRepository;
import com.example.ch5test1.repository.MemberRepository;
import com.example.ch5test1.repository.OrderItemRepository;
import com.example.ch5test1.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em; // 중간저장소에 있는 데이터를 강제로 DB에 저장, 지우도록(clear) 하도록 하는 역할

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());

        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for(int i=0;i<3;i++){
            // 테스트 상품 3개를 만들기.
            // 그냥 상품.
            Item item = this.createItem();
            // 중간 테이블(주문이 된 상품)에 저장.
            itemRepository.save(item);
            // 주문이 된 상품.
            OrderItem orderItem = new OrderItem();
            // 주문이 된 상품에, 위에 더미 상품을 추가.
            orderItem.setItem(item); // 중간 테이블에 해당 데이터를 넣는 코드
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            // 중요함.
            // 주문에 -> 주문이 된 상품을 넣어주는 비지니스 로직.
            order.getOrderItems().add(orderItem);
        }

        // 주문 엔티티 클래스의, 중간 테이블에 저장 및 실제 테이블에 저장.
        orderRepository.saveAndFlush(order);
        // 중간 테이블을 비우기.
        // 조회 시, 연관 관계 테이블이 조인이 되면서, 같이 참조되는 부분을 확인하기 위해서.
        em.clear();

        // 조회 시, 중간 테이블에 내용이 비워져서, 실제 테이블에서 내용을 가져올 때,
        // 참조하는 테이블이 뭐가 되는지 봐야함.
        // 현재, lazy로써, 지연 로딩이 되어 있지만,
        // 즉시 로딩인 eager, 이걸로 설정이 된 경우는, 연관이 없는 테이블도
        // 전부 다 조회를 해서, 성능 상 이슈가 발생함.
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    // Order 엔티티클래스의 fetch type: lazy를 쓰면 member를 조회 안하고 연관있는 것만 조회함(order만 조회하게 됨)
    // 하지만, lazy를 안하면 연관성없는 member도 조회하게 됨
    // 테스트 결과 예시: member도 조회가 됨-성능 이슈 발생)
/*    Hibernate:
    select
    order0_.order_id as order_id1_5_0_,
    order0_.reg_time as reg_time2_5_0_,
    order0_.update_time as update_t3_5_0_,
    order0_.created_by as created_4_5_0_,
    order0_.modified_by as modified5_5_0_,
    order0_.member_id as member_i8_5_0_,
    order0_.order_date as order_da6_5_0_,
    order0_.order_status as order_st7_5_0_,
    member1_.member_id as member_i1_3_1_,
    member1_.reg_time as reg_time2_3_1_,
    member1_.update_time as update_t3_3_1_,
    member1_.created_by as created_4_3_1_,
    member1_.modified_by as modified5_3_1_,
    member1_.address as address6_3_1_,
    member1_.email as email7_3_1_,
    member1_.name as name8_3_1_,
    member1_.password as password9_3_1_,
    member1_.role as role10_3_1_*/


    public Order createOrder(){
        // 주문에 담겨진 요소
        // 1) 주문_상품을 요소로 하는 리스트
        // 2) 멤버
        // 주문 -> 상품 추가
        // -> 주문 상품 추가
        // -> 주문 상품 아이템들을 요소로 가지는 리스트에 추가.
        // -> 회원 추가
        // -> 주문, 회원 추가(주문자) (return order; 까지의 내용)
        Order order = new Order();
        for(int i=0;i<3;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    // 고아객체가 삭제되는 것을 컨솔창에서 확인한 코드값
/*    Hibernate:
    delete
            from
    order_item
            where
    order_item_id=?*/

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        // 주문
        Order order = this.createOrder();
        // 주문 클래스 안에 필드로, 주문 아이템 리스트 멤버가 존재
        Long orderItemId = order.getOrderItems().get(0).getId();
        // 실제 DB에 반영하고
        em.flush();
        // 중간 저장소를 비우기 전에, orderItemId, 주문_상품의 번호를 기록.
        em.clear();
        // 주문 상품을 조회 시, 실제 DB에서 찾기를 할 때,
        // 연관이 있는 것만 조회(lazy 지연 로딩), 연관이 없는 것도 조회할거냐(eager 즉시 로딩)
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        System.out.println("===========================");
        orderItem.getOrder().getOrderDate();
        System.out.println("===========================");
    }

    // 지연 로딩 테스트 시, 즉시 로딩과는 달리 연관성 있는 order만 조회되서 콘솔창에 나옴
    // 예시)
/*    Hibernate:
    select
    order0_.order_id as order_id1_5_0_,
    order0_.reg_time as reg_time2_5_0_,
    order0_.update_time as update_t3_5_0_,
    order0_.created_by as created_4_5_0_,
    order0_.modified_by as modified5_5_0_,
    order0_.member_id as member_i8_5_0_,
    order0_.order_date as order_da6_5_0_,
    order0_.order_status as order_st7_5_0_*/


}