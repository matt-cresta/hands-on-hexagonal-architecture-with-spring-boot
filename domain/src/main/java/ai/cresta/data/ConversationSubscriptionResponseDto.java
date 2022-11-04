package ai.cresta.data;

import com.mypurecloud.sdk.v2.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationSubscriptionResponseDto {
    private Participant agent;

    private Participant customer;
}
