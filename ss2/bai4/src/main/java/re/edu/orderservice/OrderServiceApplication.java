package re.edu.orderservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import re.edu.orderservice.entity.Order;
import re.edu.orderservice.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    // create a sample order on startup for manual testing
    @Bean
    CommandLineRunner init(OrderRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Order order = new Order();
                order.setCustomerName("Nguyen Van A");
                order.setTotal(new BigDecimal("123.45"));
                order.setCreatedAt(LocalDateTime.now());
                repository.save(order);
            }
        };
    }
}

