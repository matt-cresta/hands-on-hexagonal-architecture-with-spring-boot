package ai.cresta.controller;


import ai.cresta.data.ConversationSubscriptionRequestDto;
import ai.cresta.data.ConversationSubscriptionResponseDto;
import ai.cresta.data.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;

import ai.cresta.data.UserDto;
import ai.cresta.ports.api.MessageServicePort;
import ai.cresta.ports.api.UserServicePort;

import java.security.Principal;


@RestController
@RequestMapping("/genesys")
public class GenesysChatController {
    
    @Autowired
    @Qualifier("webChat")
    private MessageServicePort messageServicePort;

    @Autowired
    private UserServicePort userServicePort;

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public ResponseEntity<MessageDto> getPrivateMessage(final MessageDto message,
                                                        final Principal principal) throws InterruptedException {
        return ResponseEntity.ok(message);
    }

    @PostMapping("/webChat/addUser")
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
