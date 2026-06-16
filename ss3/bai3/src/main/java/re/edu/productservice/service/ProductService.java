package re.edu.productservice.service;

import re.edu.productservice.dto.ProductRequestDTO;
import re.edu.productservice.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO request);

    ProductResponseDTO getProductById(Long id);

    List<ProductResponseDTO> getAllProducts();
}

