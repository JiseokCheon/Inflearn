package myjpabook.jpashop.api;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import myjpabook.jpashop.domain.Address;
import myjpabook.jpashop.domain.Order;
import myjpabook.jpashop.domain.OrderSearch;
import myjpabook.jpashop.domain.OrderStatus;
import myjpabook.jpashop.repository.OrderRepository;
import myjpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import myjpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 * */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - 양방향 관계때문에 문제 발생 -> 한쪽은 @JsonIgnore 해줘야 함
     * - 지연 로딩으로 인해서 실제 엔티티가 아닌 프록시 객체가 존재해서 오류발생
     * -> Hibernate5Module 모듈 등록, LAZY=null 처리
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        // Hibernate5Module 강제 지연 로딩 옵션 끄고 해야함
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }

        return all;
    }


    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    // 원래 List로 반환하는게 아닌 Result로 감싸야함
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> result = orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - fetch join으로 쿼리 1번 호출
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        // fetch join 사용한 메서드 (member와 delivery를 한번에 가져옴)
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> result = orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * V4. JPA에서 DTO로 바로 조회

     *-쿼리1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}

