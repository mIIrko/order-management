package de.htwg.swqs.order;

import de.htwg.swqs.order.mail.EmailService;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.order.service.OrderServiceImpl;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@ComponentScan(basePackages = "de.htwg.swqs.order")
@EnableJpaRepositories("de.htwg.swqs.order.repository")
@EntityScan("de.htwg.swqs.order.model")
public class OrderConfiguration {

    @Bean
    public OrderService catalogService(ShippingCostService shippingCostService, OrderRepository orderRepository, CurrencyConverterService currencyConverterService, EmailService emailService) {
        return new OrderServiceImpl(shippingCostService, orderRepository, currencyConverterService, emailService);
    }

}
