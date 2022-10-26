package connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesFranchise {

    @JsonProperty("vendaFranquia")
    private List<SaleFranchise> saleFranchise;
}
