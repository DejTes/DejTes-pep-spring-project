package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;



import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import java.util.List;
import java.util.Map;
import java.util.Optional;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


@RestController
//@RequestMapping("/account")
public class SocialMediaController {

   @Autowired
    private  AccountService accountService;

   @Autowired
    private  MessageService messageService;


    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

  
// Account Endpoints
@PostMapping("/register")
public ResponseEntity<Account> register(@RequestBody Account account) {
    Account existingAccount = accountService.getAccountByUsername(account.getUsername());
    if (existingAccount != null) {
        return new ResponseEntity<>(HttpStatus.CONFLICT); 
    }
    if (account.getUsername() == null || account.getUsername().isEmpty() || account.getPassword() == null || account.getPassword().length() < 4) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Account savedAccount = accountService.saveAccount(account);
    return new ResponseEntity<>(savedAccount, HttpStatus.OK); 
}

@PostMapping("/login")
public ResponseEntity<Account> login(@RequestBody Account account) {
    try {
        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
        return ResponseEntity.ok(loggedInAccount);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
    }
}
    



    // Message Endpoints
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        try {
            Message createdMessage = messageService.createMessage(newMessage);
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        return message.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(null));
    }

@DeleteMapping("/messages/{messageId}")
public ResponseEntity<?> deleteMessageById(@PathVariable Integer messageId) {
    int rowsDeleted = messageService.deleteMessageById(messageId);
    if (rowsDeleted == 1) {
        return ResponseEntity.ok(rowsDeleted); 
    } else {
        return ResponseEntity.ok().build(); 
    }
}


@PatchMapping("/messages/{messageId}")
public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Map<String, String> payload) {
    // Extract newMessageText from the payload
    String newMessageText = payload.get("messageText");
    if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
    }

    try {
   
        Message updatedMessage = messageService.updateMessage(messageId, newMessageText);
        if (updatedMessage != null) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
    }
}




    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);
        return ResponseEntity.ok(messages);
    }

}
