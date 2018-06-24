package ch.bfh.eadj.bookstore.application.service;


import ch.bfh.eadj.bookstore.application.exception.*;
import ch.bfh.eadj.bookstore.persistence.dto.OrderInfo;
import ch.bfh.eadj.bookstore.persistence.entity.*;
import ch.bfh.eadj.bookstore.persistence.enumeration.Country;
import ch.bfh.eadj.bookstore.persistence.enumeration.CreditCardType;
import ch.bfh.eadj.bookstore.persistence.enumeration.OrderStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //ganzer container fährt hoch
@ExtendWith(SpringExtension.class)
@Transactional
public class OrderServiceIT {

    public static final String ISBN = "0099590085";

    @Autowired
    OrderService orderService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    CustomerService customerService;

    private Book book;
    private Customer customer;
    private Order order;
    private  List<OrderItem> items;

    private Integer year = LocalDate.now().getYear();

    @BeforeAll
    static void setUp() throws Exception {
        Context jndiContext = new InitialContext();

    }

    @BeforeEach
    void prepare() throws BookNotFoundException, EmailAlreadyUsedException, CustomerNotFoundException {
        String isbn = ISBN;
        book = catalogService.findBookOnAmazon(isbn);
        items = createOrderItems(3, book);
        customer = createCustomer();
        Long userId = customerService.registerCustomer(customer, "pwd");
        customer = customerService.findCustomer(userId);
    }



    static List<OrderItem> createOrderItems(int items, Book book) {
        List<OrderItem> orderItems = new ArrayList<>();
        for ( int i = 0; i < items; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setQuantity(1);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private  Address createAdddress() {
        return new Address("Bahnstrasse", "Burgdorf", "3400", Country.CH);
    }

    private  CreditCard createCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setExpirationMonth(8);
        creditCard.setExpirationYear(LocalDate.now().getYear()+1);
        creditCard.setNumber("2322322212312111");
        creditCard.setType(CreditCardType.MASTERCARD);
        return creditCard;
    }

    protected  Customer createCustomer() {
        Customer cust  = new Customer();
        cust.setEmail("hans" + Integer.toString(new Random().nextInt(10000)) + "@dampf.ch");
        cust.setFirstName("Hans");
        cust.setLastName("Dampf");
        cust.setCreditCard(createCreditCard());
        cust.setAddress(createAdddress());
        return cust;
    }


    @Test
    public void shouldFindOrder() throws Exception {
        //given
        order = orderService.placeOrder(customer, items);

        //when
        Order orderFromDb = orderService.findOrder(order.getNr());

        //then
        assertEquals(orderFromDb.getAmount(), order.getAmount());
        assertEquals(orderFromDb.getNr(), order.getNr());
    }

    @Test
    public void shouldFailFindOrder() throws OrderNotFoundException {
            //when
        Executable findOrder = () -> orderService.findOrder(222L);
        assertThrows(OrderAlreadyCanceledException.class, findOrder);
    }

    @Test
    public void shouldPlaceOrder() throws Exception {

        //when
        order = orderService.placeOrder(customer, items);

        //then
        assertThat(order.getStatus(), is(OrderStatus.ACCEPTED));
        assertEquals(order.getCustomer().getEmail(), customer.getEmail());
    }

    @Test
    public void shouldFailPlaceOrderLimitExceeded() throws Exception {
        //given
        List<OrderItem> items2 = createOrderItems(30, book);
        try {
            //when
            order = orderService.placeOrder(customer, items2);

            //then
        } catch (PaymentFailedException e) {
            assertTrue(e.getCode().equals(PaymentFailedException.Code.PAYMENT_LIMIT_EXCEEDED));
        }
    }

    @Test
    public void shouldFailPlaceOrderExpiredCreditCard() throws Exception {
        //given
        List<OrderItem> items = createOrderItems(5, book);
        customer.getCreditCard().setExpirationYear(2016);
        customer.getCreditCard().setNumber("1111222233334444");
        customerService.updateCustomer(customer);
        try {
            //when
            order = orderService.placeOrder(customer, items);

            //then
        } catch (PaymentFailedException e) {
            assertTrue(e.getCode().equals(PaymentFailedException.Code.CREDIT_CARD_EXPIRED));
        }
    }

    @Test
    public void shouldFailPlaceOrderInvalidCard() throws Exception {
        //given
        List<OrderItem> items = createOrderItems(5, book);
        customer.getCreditCard().setExpirationYear(year);
        customer.getCreditCard().setNumber("111122223333444");
        customerService.updateCustomer(customer);
        try {
            //when
            order = orderService.placeOrder(customer, items);

            //then
        } catch (PaymentFailedException e) {
            assertTrue(e.getCode().equals(PaymentFailedException.Code.INVALID_CREDIT_CARD));
        }
    }

    @Test
    public void shouldSearchOrders() throws Exception {
        order = orderService.placeOrder(customer, items);

        //when
        List<OrderInfo> orderInfoList = orderService.searchOrders(customer, year);

        //then
        assertFalse(orderInfoList.isEmpty());
    }

    @Test
    public void shouldFailSearchOrders() throws Exception {
        order = orderService.placeOrder(customer, items);

        //when
        List<OrderInfo> orderInfoList = orderService.searchOrders(customer, 2015);

        //then
        assertTrue(orderInfoList.isEmpty());
    }
}