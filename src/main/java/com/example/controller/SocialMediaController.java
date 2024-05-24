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



/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


@RestController
//@RequestMapping("/account")
public class SocialMediaController {

   
    private  AccountService accountService;
    private  MessageService messageService;


    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

  

// Account Endpoints
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account newAccount) {
        Account registeredAccount = accountService.registerAccount(newAccount);
        try {
            return ResponseEntity.ok(registeredAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
    

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        try {
            Account logedInAccount = accountService.login(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(logedInAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    

    //Message endpoints
    @PostMapping("/messages")
    
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        try {
            Message createdMessage = messageService.createMessage(newMessage);
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }

    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }



    @GetMapping("/messages/{messageId}") 
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return messageService.getMessageId(messageId).map(ResponseEntity::ok).orElse(ResponseEntity.status(200).body(null));
    }


    // @DeleteMapping("/messages/{messageId}")
    // public ResponseEntity<Void> deleteMessageById(@PathVariable Integer messageId) {
    //     messageService.deleteMessageById(messageId);
    //     return ResponseEntity.ok().build();
    // }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Integer messageId) {
        System.out.println("Received request to delete message with id: " + messageId);
        try {
            messageService.deleteMessageById(messageId);
            System.out.println("Successfully deleted message with id: " + messageId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Unexpected error while deleting message with id: " + messageId + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/messages/{messageId}")
public ResponseEntity<Message> updateMessage(@PathVariable Integer messageId, @RequestBody String newMessageText) {
    System.out.println("Received request to update message with id: " + messageId);
    try {
        Message updatedMessage = messageService.updateMessage(messageId, newMessageText);
        System.out.println("Successfully updated message with id: " + updatedMessage.getMessageId());
        return ResponseEntity.ok(updatedMessage);
    } catch (IllegalArgumentException e) {
        System.out.println("Error updating message: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);
        return ResponseEntity.ok(messages);
    }

}
