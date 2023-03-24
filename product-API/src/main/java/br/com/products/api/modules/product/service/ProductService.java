package br.com.products.api.modules.product.service;

import br.com.products.api.config.RequestUtil;
import br.com.products.api.config.exception.SuccessResponse;
import br.com.products.api.config.exception.ValidationException;
import br.com.products.api.modules.category.service.CategoryService;
import br.com.products.api.modules.product.dto.*;
import br.com.products.api.modules.product.model.Product;
import br.com.products.api.modules.product.repository.ProductRepository;
import br.com.products.api.modules.sales.client.SalesClient;
import br.com.products.api.modules.sales.dto.SalesConfirmationDto;
import br.com.products.api.modules.sales.dto.SalesProductResponse;
import br.com.products.api.modules.sales.enums.SalesStatus;
import br.com.products.api.modules.sales.rabbitmq.SalesConfirmationSender;
import br.com.products.api.modules.supplier.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.products.api.config.RequestUtil.getCurrentRequest;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class ProductService {
    private static final Integer ZERO = 0;
    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";
    private static final String SERVICE_ID = "serviceid";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SalesClient salesClient;


    @Autowired
    private SalesConfirmationSender salesConfirmationSender;

    public List<ProductResponse> findAll(){
        return productRepository
                .findAll()
                .stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }
    public  List<ProductResponse> findByname(String name){
        return productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }

    public ProductResponse save(ProductRequest request){
        validateProductNotInformed(request);
        validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request,supplier, category));
        return ProductResponse.of(product);

    }

    public ProductResponse update(ProductRequest request, Integer id){
        validateProductNotInformed(request);
        validateInformedId(id);
        validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = Product.of(request, supplier, category);
        product.setId(id);
        productRepository.save(product);
        return ProductResponse.of(product);
    }

    public ProductResponse findByIdResponse(Integer id){
        return ProductResponse.of(findById(id));
    }


    public Product findById(Integer id){
        return productRepository
                .findById(id)
                .orElseThrow( ()-> new ValidationException("There's no Product for the given ID"));
    }
    private void validateProductNotInformed(ProductRequest request){
        if(isEmpty(request.getName())){
            throw new ValidationException("The category name was not informed!");
        }
        if(isEmpty(request.getQuantityAvailable())){
            throw new ValidationException("The product quantity was not informed");
        }
        if(request.getQuantityAvailable() <= 0){
            throw new ValidationException("The quantity should be more than 0");
        }
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId){
        if(isEmpty(supplierId)){
            throw new ValidationException("The Product Supplier ID name must be informed");
        }
        return productRepository
                .findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
    public List<ProductResponse> findByDescriptionIgnoreCaseContaining(String categoryName){
        if(isEmpty(categoryName)){
            throw new ValidationException("The Product Category ID name must be informed");
        }
        return productRepository
                .findByNameIgnoreCaseContaining(categoryName)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
    public List<ProductResponse> findByCategoryId(Integer categoryId){
        if(isEmpty(categoryId)){
            throw new ValidationException("The Product Category ID name must be informed");
        }
        return productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
//    public List<ProductResponse> findByCategoryId(Integer categoryId){
//        if(isEmpty(categoryId)){
//            throw new ValidationException("The Product Category ID name must be informed");
//        }
//        return productRepository
//                .findByCategoryId(categoryId)
//                .stream()
//                .map(ProductResponse::of)
//                .collect(Collectors.toList());
//    }

    private void validateCategoryAndSupplierInformed(ProductRequest request){
        if(isEmpty(request.getCategoryId())){
            throw new ValidationException("The Product name was not informed!");
        }
        if(isEmpty(request.getSupplierId())){
            throw new ValidationException("The Product name was not informed!");
        }
    }

    public Boolean existsbyCategoryId(Integer categoryId){
        return productRepository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId){
        return productRepository.existsByCategoryId(supplierId);
    }
    public SuccessResponse delete(Integer id) {
        validateInformedId(id);

        productRepository.deleteById(id);
        return SuccessResponse.create("The Product was deleted.");

    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The Supplier ID must informed.");
        }
    }

    public void updateProductStock(ProductStockDto productStockDto){
        try {
            validateStockUpdateData(productStockDto);
            updateStock(productStockDto);
        }catch (Exception ex){
            log.error("Error While trying to update stock for message with error: {}", ex.getMessage(), ex);
            var rejectedMessage = new SalesConfirmationDto(productStockDto.getSalesId(), SalesStatus.REJECT,productStockDto.getTransactinid());
            salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);

        }
    }
    private void updateStock(ProductStockDto productStockDto){
        List<Product> productStockForUpdate = new ArrayList<>();

        productStockDto
                .getProducts()
                .forEach(salesProduct -> {
                    var existingProduct = findById(salesProduct.getProductId());
                    validateQuantityInStock(salesProduct, existingProduct);
                    existingProduct.updateStock(salesProduct.getQuantity());
                    productStockForUpdate.add(existingProduct);
                });

            if(!isEmpty(productStockForUpdate)){
                productRepository.saveAll(productStockForUpdate);
                var aprovedMessage = new SalesConfirmationDto(productStockDto.getSalesId(), SalesStatus.APPROVED,productStockDto.getTransactinid());
                salesConfirmationSender.sendSalesConfirmationMessage(aprovedMessage);
            }
    }

    @Transactional
    private void validateStockUpdateData(ProductStockDto productStockDto){
        if(isEmpty(productStockDto)
        || isEmpty(productStockDto.getSalesId())
        ){
            throw new ValidationException("The product data and sales ID must be informed");
        }

        if(isEmpty(productStockDto.getProducts())){
            throw new ValidationException("The sales products must be informed");
        }

        productStockDto
                .getProducts()
                .forEach(salesProduct -> {
                    if (isEmpty(salesProduct.getQuantity())
                            || isEmpty(salesProduct.getProductId())
                    ) {

                        throw new ValidationException("The product ID and the quantity must be informed");
                    }
                });
    }

    private void validateQuantityInStock(ProductQuantityDto salesProduct, Product existingProduct){
        if(salesProduct.getQuantity() >  existingProduct.getQuantityAvailable()){
            throw new ValidationException(
                    String.format("The product %s is out of stock. ", existingProduct.getId())
            );
        }
    }


    private SalesProductResponse getSalesByProductId(Integer productId) {
        try {
            var currentRequest = getCurrentRequest();
            var token = currentRequest.getHeader(AUTHORIZATION);
            var transactionid = currentRequest.getHeader(TRANSACTION_ID);
            var serviceid = currentRequest.getAttribute(SERVICE_ID);
            log.info("Sending GET request to orders by productId with data {} | [transactionID: {} | serviceID: {}]",
                    productId, transactionid, serviceid);
            var response = salesClient
                    .findSalesByProductId(productId, token, transactionid)
                    .orElseThrow(() -> new ValidationException("The sales was not found by this product."));
            log.info("Recieving response from orders by productId with data {} | [transactionID: {} | serviceID: {}]",
                    new ObjectMapper().writeValueAsString(response), transactionid, serviceid );
            return response;
        } catch (Exception ex) {
            log.error("Error trying to call Sales-API: {}", ex.getMessage());
            throw new ValidationException("The sales could not be found.");
        }
    }

   public ProductSalesResponse findProductSales(Integer id){
       var currentRequest = getCurrentRequest();
       var token = currentRequest.getHeader(AUTHORIZATION);
       var transactionid = currentRequest.getHeader(TRANSACTION_ID);
       var serviceid = currentRequest.getAttribute(SERVICE_ID);
        var product = findById(id);
        try {
            var sales = salesClient.findSalesByProductId(id, token, transactionid)
                    .orElseThrow(() -> new ValidationException("The sales was not found by this product."));
            log.info("Recieving response from orders by productId with data {} | [transactionID: {} | serviceID: {}]",
                    new ObjectMapper().writeValueAsString("response"), transactionid, serviceid );
            return ProductSalesResponse.of(product, sales.getSalesId());
        }catch (Exception ex){
            throw new ValidationException("There was an error trying to get the product's sales");
        }
    }

    public  SuccessResponse checkProductsStock(ProductCheckStockRequest request){
        var currentRequest = getCurrentRequest();
        var transactionid = currentRequest.getHeader(TRANSACTION_ID);
        var serviceid = currentRequest.getHeader(SERVICE_ID);
        try {
            log.info("Request to POST product stock with data {} || [Transaction ID: {} | ServiceId: {}]",
                    new ObjectMapper().writeValueAsString(request), transactionid, serviceid
            );
            if(isEmpty(request) || isEmpty(request.getProducts())){
                throw new ValidationException("The request data must be informed!");
            }
            request
                    .getProducts()
                    .forEach(this::validateStock);

            var response =   SuccessResponse.create("The stock is ok!");
            log.info("Response to POST product stock with data {} || [Transaction ID: {} | ServiceId: {}]",
                    new ObjectMapper().writeValueAsString(response), transactionid, serviceid
            );
            return response;
        }catch (Exception ex){
            throw new ValidationException(ex.getMessage());
        }


    }

    public void validateStock(ProductQuantityDto productQuantityDto){
        if(isEmpty(productQuantityDto.getProductId()) || isEmpty(productQuantityDto.getQuantity())){
            throw new ValidationException("Product ID and quantity must be informed!");
        }
        var product = findById(productQuantityDto.getProductId());
        if(productQuantityDto.getQuantity() > product.getQuantityAvailable()  ){
            throw new ValidationException(String.format(" The product %s is out of stock.", product.getId()));
        }
    }
}
