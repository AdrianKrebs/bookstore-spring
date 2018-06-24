package ch.bfh.eadj.bookstore.application.service;


import ch.bfh.eadj.bookstore.application.exception.*;
import ch.bfh.eadj.bookstore.persistence.dto.OrderInfo;
import ch.bfh.eadj.bookstore.persistence.entity.*;
import ch.bfh.eadj.bookstore.persistence.enumeration.OrderStatus;
import ch.bfh.eadj.bookstore.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    CatalogService catalogService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${ch.bfh.creditCardPaymentLimit}")
    private Long PAYMENT_LIMIT = 10000L;

    public void cancelOrder(Long nr)
            throws OrderNotFoundException, OrderAlreadyShippedException, OrderProcessingException, OrderAlreadyCanceledException {
        Order order = findOrder(nr);
        if (OrderStatus.SHIPPED.equals(order.getStatus())) {
            throw new OrderAlreadyShippedException();
        } else if (OrderStatus.CANCELED.equals(order.getStatus())) {
            throw new OrderAlreadyCanceledException();
        }

        order.setStatus(OrderStatus.CANCELED);
        Order savedOrder = orderRepo.saveAndFlush(order);
        sendMessage(savedOrder);
    }

    private void calculateOrderAmount(Order order) {
        BigDecimal orderAmount = new BigDecimal(0);
        for (OrderItem orderItem : order.getItems()) {
            orderAmount = orderAmount.add(orderItem.getBook().getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        order.setAmount(orderAmount);
    }

    private void copyCustomerInfos(Customer customer, Order order) {
        order.setCreditCard(customer.getCreditCard());
        order.setAddress(customer.getAddress());
    }

    public Order findOrder(Long nr) throws OrderNotFoundException {
        Order orders = orderRepo.findByNr(nr);
        if (orders == null) {
            throw new OrderNotFoundException();
        }
        return orders;
    }

    private boolean isPaymentLimitExceeded(Order order) {
        return order.getAmount().compareTo(new BigDecimal(PAYMENT_LIMIT)) > 0;
    }

    private boolean isCreditCardExpired(LocalDate now, CreditCard creditCard) {
        return creditCard.getExpirationYear() < now.getYear() && creditCard.getExpirationMonth() < now.getMonthValue();
    }

    public Order placeOrder(Customer customer, List<OrderItem> items) throws PaymentFailedException, OrderProcessingException, BookNotFoundException {
        storeBooksIfNotPresent(items);
        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(items);
        order.setDate(new java.util.Date());
        copyCustomerInfos(customer, order);
        calculateOrderAmount(order);
        validateOrderPlacement(order);
        order.setStatus(OrderStatus.ACCEPTED);
        Order savedOrder = orderRepo.saveAndFlush(order);
        sendMessage(savedOrder);
        return savedOrder;
    }

    void storeBooksIfNotPresent(List<OrderItem> items) throws BookNotFoundException {
        for (OrderItem item : items) {
            Book book = item.getBook();
            if (book != null) {
                Book bookFromDb = catalogService.findBook(book.getIsbn());
                if (bookFromDb == null) {
                    try {
                        Book bookFromAmazon = catalogService.findBookOnAmazon(book.getIsbn());
                        catalogService.addBook(bookFromAmazon);
                        item.setBook(bookFromAmazon);
                    } catch (BookAlreadyExistsException e1) {
                        throw new IllegalStateException("should not happen.... book is not supposed to be in DB yet");
                    }

                } else {
                    item.setBook(bookFromDb);
                }
            }
        }
    }

    private void sendMessage(Order order) throws OrderProcessingException {
        jmsTemplate.convertAndSend("jms/orderQueue",order.getNr());
    }

    public void removeOrder(Order order) {
        orderRepo.delete(order);
    }


    public List<OrderInfo> searchOrders(Customer customer, Integer year) {
        return orderRepo.findByCustomerAndYear(customer, year);
    }

    private void validateOrderPlacement(Order order) throws PaymentFailedException {
        LocalDate now = LocalDate.now();
        CreditCard creditCard = order.getCreditCard();
        if (isCreditCardExpired(now, creditCard)) {
            throw new PaymentFailedException(PaymentFailedException.Code.CREDIT_CARD_EXPIRED);
        }

        if (isPaymentLimitExceeded(order)) {
            throw new PaymentFailedException(PaymentFailedException.Code.PAYMENT_LIMIT_EXCEEDED);
        }

        if (creditCard.getNumber().length() != 16) {
            throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
        }
    }

}
