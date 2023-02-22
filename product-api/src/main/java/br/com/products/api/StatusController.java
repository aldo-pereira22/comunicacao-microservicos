package br.com.products.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class StatusController {
    @GetMapping("status")
    public ResponseEntity<HashMap<String, Object>> getStatus(){
        var response = new HashMap<String, Object>();
        response.put("Service:", "Product-API");
        response.put("Status:", "UPSTREAM";
        return  ResponseEntity.ok(response);
    }
}
