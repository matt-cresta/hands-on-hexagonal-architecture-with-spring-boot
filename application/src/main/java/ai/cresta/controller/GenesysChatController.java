package ai.cresta.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.cresta.data.UserDto;
import ai.cresta.ports.api.MessageServicePort;
import ai.cresta.ports.api.UserServicePort;


@RestController
@RequestMapping("/genesys")
public class GenesysChatController {
    
    @Autowired
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

}
