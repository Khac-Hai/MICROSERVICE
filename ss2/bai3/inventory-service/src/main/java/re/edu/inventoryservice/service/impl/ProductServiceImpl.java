package re.edu.inventoryservice.service.impl;

import org.springframework.stereotype.Service;
import re.edu.inventoryservice.entity.Product;
import re.edu.inventoryservice.repository.ProductRepository;
import re.edu.inventoryservice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }
}

