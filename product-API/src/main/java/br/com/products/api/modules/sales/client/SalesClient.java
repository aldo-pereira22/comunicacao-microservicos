package br.com.products.api.modules.sales.client;

import br.com.products.api.modules.sales.dto.SalesProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@FeignClient(
        name = "salesClient",
        contextId = "salesClient",
        url = "${app-config.services.sales}"
)

@RestController
public interface SalesClient {
    @GetMapping("/api/orders/products/{productId}")
    Optional<SalesProductResponse> findSalesByProductId(@PathVariable Integer productId,
                                                        @RequestHeader(name = "Authorization") String authorization,
                                                        @RequestHeader(name = "transactionId") String transactionId);
}
