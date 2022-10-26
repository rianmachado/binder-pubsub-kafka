package connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @JsonProperty("valor")
    private BigDecimal amount;

    @JsonProperty("tipo")
    private String type;

    @JsonProperty("parcelas")
    private int installments;

    @JsonProperty("identificador")
    private int identifier;
}
