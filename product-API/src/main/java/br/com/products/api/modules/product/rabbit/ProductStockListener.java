package br.com.products.api.modules.product.rabbit;

import br.com.products.api.modules.product.dto.ProductStockDto;
import br.com.products.api.modules.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ProductStockListener {

    @Autowired
    private ProductService productService;


    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveProductStockMessage(ProductStockDto productStockDto) throws JsonProcessingException {

        log.info("Receiving message with data:{} and transactonid: {}", new ObjectMapper().writeValueAsString(productStockDto),
                productStockDto.getTransactinid());
        productService.updateProductStock(productStockDto);
    }
}
