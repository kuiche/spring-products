package com.kui.products.api;

import com.kui.products.forex.ForexService;
import com.kui.products.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.Currency;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {
    private ProductService productService;
    private ForexService forexService;

    @Autowired
    public ProductApiController(ProductService productService, ForexService forexService) {
        this.productService = productService;
        this.forexService = forexService;
    }

    @GetMapping
    public ResponseEntity<ProductView[]> list(@RequestParam(value = "currency", required = false) @Nullable Currency currency) {
        return ResponseEntity.ok(
            ProductView.fromProducts(productService.listProducts(), currency, forexService)
        );
    }
}
