package ch.bfh.eadj.bookstore.application.service;


import ch.bfh.eadj.bookstore.persistence.entity.Order;
import ch.bfh.eadj.bookstore.persistence.enumeration.OrderStatus;
import ch.bfh.eadj.bookstore.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@Transactional
public class OrderProcessor {

    @Autowired
    OrderRepository orderRepository;

    //  @Autowired
    //  private MailService mailService;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private ShipmentService shipmentService;

    @Value("${ch.bfh.orderProcessingTime}")
    private Long timePeriod;

    private void cancelOrder(Long orderNr) {
        Order order = orderRepository.findByNr(orderNr);
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

      /*  Collection<Timer> timers = timerService.getTimers();
        for (Timer timer : timers) {
            Order timedOrder = (Order) timer.getInfo();
            if (timedOrder.getNr().equals(order.getNr())) {
                timer.cancel();
            }
        }*/
    }

    @JmsListener(destination = "jms/orderQueue")
    private void processOrder(Long orderNr) {
        Order order = orderRepository.findByNr(orderNr);
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);
//        mailService.sendProccessStartedMail(order);
        Instant startTime = Instant.now().plusMillis(timePeriod);
        taskScheduler.schedule(() -> shipmentService.shipOrder(order), startTime);
    }

}
