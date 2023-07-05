package jpabook.jpashop.service;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.exception.NotEnoughStockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired MemberService memberService;
    @Autowired OrderService orderService;
    @Autowired ItemService itemService;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        Address address = new Address("a","b","c");
        member.setAddress(address);
        memberService.join(member);

        Item item = new Item();
        item.setName("aItem");
        item.setPrice(100);
        item.setStockQuantity(100);

        Item item2 = new Item();
        item2.setName("bItem");
        item2.setPrice(200);
        item2.setStockQuantity(200);
        itemService.saveItem(item);
        itemService.saveItem(item2);

        //when
        orderService.order(member.getId(), item.getId(), 50);

        //then
        Assertions.assertThat(itemService.findOne(item.getId()).getStockQuantity()).isEqualTo(50);
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        Address address = new Address("a","b","c");
        member.setAddress(address);
        memberService.join(member);

        Item item = new Item();
        item.setName("aItem");
        item.setPrice(100);
        item.setStockQuantity(100);

        Item item2 = new Item();
        item2.setName("bItem");
        item2.setPrice(200);
        item2.setStockQuantity(200);
        itemService.saveItem(item);
        itemService.saveItem(item2);

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), 50);

        //then
        Assertions.assertThat(itemService.findOne(item.getId()).getStockQuantity()).isEqualTo(50);
        Order foundOrder = orderService.findOne(orderId);
        foundOrder.orderCancel();
        Assertions.assertThat(itemService.findOne(item.getId()).getStockQuantity()).isEqualTo(100);
    }
    @Test
    public void 재고수량초과() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        Address address = new Address("a","b","c");
        member.setAddress(address);
        memberService.join(member);

        Item item = new Item();
        item.setName("aItem");
        item.setPrice(100);
        item.setStockQuantity(100);

        Item item2 = new Item();
        item2.setName("bItem");
        item2.setPrice(200);
        item2.setStockQuantity(200);
        itemService.saveItem(item);
        itemService.saveItem(item2);

        //when


        //then
        Assertions.assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), 101)).
                isInstanceOf(NotEnoughStockException.class);
    }

}