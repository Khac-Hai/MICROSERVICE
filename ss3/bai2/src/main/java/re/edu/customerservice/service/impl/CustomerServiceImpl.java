package re.edu.customerservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import re.edu.customerservice.dto.CustomerRequestDTO;
import re.edu.customerservice.dto.CustomerResponseDTO;
import re.edu.customerservice.entity.Customer;
import re.edu.customerservice.exception.ResourceNotFoundException;
import re.edu.customerservice.repository.CustomerRepository;
import re.edu.customerservice.service.CustomerService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public CustomerResponseDTO register(CustomerRequestDTO request) {
        // check if email exists
        Optional<Customer> existing = customerRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already used");
        }

        Customer customer = Customer.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Customer saved = customerRepository.save(customer);

        return CustomerResponseDTO.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .build();
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));

        return CustomerResponseDTO.builder()
                .id(c.getId())
                .fullName(c.getFullName())
                .email(c.getEmail())
                .build();
    }

    @Override
    public CustomerResponseDTO login(String email, String password) {
        Customer c = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email or password incorrect"));

        if (!passwordEncoder.matches(password, c.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email or password incorrect");
        }

        return CustomerResponseDTO.builder()
                .id(c.getId())
                .fullName(c.getFullName())
                .email(c.getEmail())
                .build();
    }
}

