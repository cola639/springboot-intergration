package org.spring.springboot.controller;


import lombok.extern.slf4j.Slf4j;
import org.spring.springboot.domain.Product;
import org.spring.springboot.repository.ProductRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private final ProductRepository productRepository;

    // 自己写构造函数，Spring 会自动注入
    public SeckillController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/{productId}")
    public String seckill(@PathVariable Long productId) {

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            return "商品不存在";
        }

        Product product = optionalProduct.get();
        if (product.getStock() <= 0) {
            return "已售罄";
        }

        product.setStock(product.getStock() - 1);
        productRepository.save(product);

        log.info("成功秒杀商品: {}", productId);
        return "秒杀成功";
    }
}

