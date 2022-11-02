package ai.cresta.controller;

import ai.cresta.data.ConversationSubscriptionRequestDto;
import ai.cresta.data.ConversationSubscriptionResponseDto;
import ai.cresta.data.UserDto;
import ai.cresta.ports.api.MessageServicePort;
import ai.cresta.ports.api.UserServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/genesys")
public class GenesysMessagingController {
    @Autowired
    private MessageServicePort messageServicePort;

    @Autowired
    private UserServicePort userServicePort;

    @PostMapping("/webMessage/addUser")
    public ResponseEntity<String> addUser(@RequestBody final UserDto userDto){
        userServicePort.addUser(userDto);
        return ResponseEntity.ok("");
    }

    @PostMapping("/webChat/connectToConversation")
    public ResponseEntity<ConversationSubscriptionResponseDto> subscribeToConversation(@RequestBody final ConversationSubscriptionRequestDto conversationSubscriptionRequestDto){
        ConversationSubscriptionResponseDto conversationSubscriptionResponseDto
                = messageServicePort.subscribeToConversation(conversationSubscriptionRequestDto);
        return ResponseEntity.ok(conversationSubscriptionResponseDto);
    }
}
