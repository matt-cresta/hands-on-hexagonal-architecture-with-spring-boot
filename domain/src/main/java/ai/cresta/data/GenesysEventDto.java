package ai.cresta.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenesysEventDto {
    private GenericGenesysEventDto eventBody;

    private MetaData metaData;

    private String topicName;

    private String version;

    private class MetaData{
        private String correlationId; 
        private String type;
    }
}
