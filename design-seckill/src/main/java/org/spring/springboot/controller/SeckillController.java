package org.spring.springboot.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.springboot.domain.Product;
import org.spring.springboot.repository.ProductRepository;
import org.spring.springboot.service.SeckillService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/seckill")
@RequiredArgsConstructor
@Slf4j
public class SeckillController {

    private final SeckillService seckillService;

    private final ProductRepository productRepository;

    @PostMapping("/{productId}")
    public String seckill(@PathVariable Long productId) {
        String result = seckillService.seckill(productId);
        log.info("请求结果：{}", result);
        return result;
    }
}

