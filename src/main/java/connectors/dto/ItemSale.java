package connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSale {
    @JsonProperty("quantidade")
    private int quantity;

    @JsonProperty("valorTotal")
    private BigDecimal amount;

    @JsonProperty("produto")
    private Product product;

    @JsonProperty("valorDesconto")
    private BigDecimal amountValue;

    @JsonProperty("tipoItem")
    private String itemType;

    @JsonProperty("numeroSequencia")
    private int sequenceNumber;

    @JsonProperty("descontos")
    private Discounts discounts;
}
