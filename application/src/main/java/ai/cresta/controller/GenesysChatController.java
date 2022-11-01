package ai.cresta.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.cresta.data.UserDto;
import ai.cresta.ports.api.MessageServicePort;


@RestController
@RequestMapping("/genesys")
public class GenesysChatController {
    
    @Autowired
    private MessageServicePort messageServicePort;

    @PostMapping("/send-chat")
    public void sendChat(@RequestBody final String message){
        messageServicePort.notifyFrontend("message");
    }

    @PostMapping("/send-user-chat/{id}")
    public void sendUserChat(@PathVariable final String id,
                             @RequestBody final String message){
        messageServicePort.notifyUser(id, "message");
    }

    @PostMapping("/webChat/accessToken")
    public ResponseEntity storeAccessToken(@RequestBody final UserDto userDto ){
        messageServicePort.setAccessToken(userDto.getUserId(), userDto.getAccessToken());
        return ResponseEntity.ok().build();
    }

}
