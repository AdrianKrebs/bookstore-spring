package ch.bfh.eadj.bookstore.application.service;

import ch.bfh.eadj.bookstore.persistence.entity.Order;
import ch.bfh.eadj.bookstore.persistence.enumeration.OrderStatus;
import ch.bfh.eadj.bookstore.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ShipmentService {

    @Autowired
    OrderRepository orderRepository;

    public void shipOrder(Order order) {
        if (order.getStatus().equals(OrderStatus.PROCESSING)) {
            order = orderRepository.findByNr(order.getNr());
            order.setStatus(OrderStatus.SHIPPED);
            orderRepository.save(order);
//            mailService.sendOrderShippedMail(order);
        } else {
            throw new IllegalStateException("Order Status '" + order.getStatus() +
                    "' kann nicht verarbeitet werden.");
        }
    }
}
