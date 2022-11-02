package ai.cresta.controller;


import ai.cresta.data.ConversationSubscriptionRequestDto;
import ai.cresta.data.ConversationSubscriptionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.cresta.data.UserDto;
import ai.cresta.ports.api.MessageServicePort;
import ai.cresta.ports.api.UserServicePort;


@RestController
@RequestMapping("/genesys")
public class GenesysChatController {
    
    @Autowired
    @Qualifier("webChat")
    private MessageServicePort messageServicePort;

    @Autowired
    private UserServicePort userServicePort;

    @PostMapping("/send-chat")
    public void sendChat(@RequestBody final String message){
        messageServicePort.notifyFrontend("message");
    }

    @PostMapping("/send-user-chat/{id}")
    public void sendUserChat(@PathVariable final String id,
                             @RequestBody final String message){
        messageServicePort.notifyUser(id, "message");
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
