package connectors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @JsonProperty("valor")
    private String value;

    @JsonProperty("tipoDocumento")
    private String documentType;
}
