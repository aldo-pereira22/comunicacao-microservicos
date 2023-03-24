package br.com.products.api.modules.sales.dto;

import br.com.products.api.modules.sales.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesConfirmationDto {
    private String salesId;
    private SalesStatus salesStatus;
    private String transactionid;
}
