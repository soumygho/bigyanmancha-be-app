package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.repository.mysql.VigyanKendraMySqlRepository;
import com.vigyanmancha.backend.repository.postgres.VigyanKendraRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hello")
@Tag(name = "Hello api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
@Slf4j
public class HelloController {
    private final VigyanKendraMySqlRepository vigyanKendraMySqlRepository;
    private final VigyanKendraRepository  vigyanKendraRepository;
    @GetMapping
    public String sayHello() {
        vigyanKendraRepository.count();
        vigyanKendraMySqlRepository.count();
        return "Hello I am alive";
    }
}
