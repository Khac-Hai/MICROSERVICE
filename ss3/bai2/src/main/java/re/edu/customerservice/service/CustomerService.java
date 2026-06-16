package re.edu.customerservice.service;

import re.edu.customerservice.dto.CustomerRequestDTO;
import re.edu.customerservice.dto.CustomerResponseDTO;

public interface CustomerService {
    CustomerResponseDTO register(CustomerRequestDTO request);
    CustomerResponseDTO getCustomerById(Long id);
    CustomerResponseDTO login(String email, String password);
}

