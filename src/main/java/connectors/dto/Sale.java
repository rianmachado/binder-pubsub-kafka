package connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @JsonProperty("sistemaOrigem")
    private String originSystem;

    @JsonProperty("vendasFranquia")
    private SalesFranchise salesFranchise;
}
