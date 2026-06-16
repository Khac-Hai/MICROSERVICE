package re.edu.customerservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.edu.customerservice.dto.CustomerRequestDTO;
import re.edu.customerservice.dto.CustomerResponseDTO;
import re.edu.customerservice.dto.LoginRequestDTO;
import re.edu.customerservice.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> register(@Valid @RequestBody CustomerRequestDTO request){
        CustomerResponseDTO response = customerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Long id){
        CustomerResponseDTO response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerResponseDTO> login(@Valid @RequestBody LoginRequestDTO request){
        CustomerResponseDTO response = customerService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}

