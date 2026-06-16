package re.edu.customerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.customerservice.entity.Customer;
import re.edu.customerservice.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer registerCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customer.setFullName(customerDetails.getFullName());
            customer.setEmail(customerDetails.getEmail());
            customer.setPassword(customerDetails.getPassword());
            customer.setAddress(customerDetails.getAddress());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}

