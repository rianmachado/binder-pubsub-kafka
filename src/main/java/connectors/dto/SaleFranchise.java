package connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleFranchise {
    @JsonProperty("numero")
    private int number;

    @JsonProperty("codigo")
    private int code;

    @JsonProperty("data")
    private String date;

    private String status;

    @JsonProperty("itens")
    private Items items;

    @JsonProperty("pagamentos")
    private Payments payments;

    @JsonProperty("consumidor")
    private Consumer consumer;

    @JsonProperty("identificadorPDV")
    private int POSIdentifier;

    @JsonProperty("funcionarioFranquia")
    private FranchiseEmployee employeeFranchise;

    @JsonProperty("franquia")
    private Franchise franchise;

    @JsonProperty("numeroPedidoExterno")
    private String externalOrderNumber;

    @JsonProperty("origemPedidoExterno")
    private String externalOrderOrigin;
}
