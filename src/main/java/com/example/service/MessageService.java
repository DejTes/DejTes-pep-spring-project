package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if ( message.getMessageText() == null || message.getMessageText().isEmpty() || message.getMessageText().length() > 225 || !accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("Invalid message or does not exist");
        }
        return messageRepository.save(message);
    }


    //Retreave all messages from the database
    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    //Retrieve a message by its ID
    public Optional<Message> getMessageId(Integer messageId) {
        return messageRepository.findById(messageId);
    }

        // Delete message by id
    public void deleteMessageById(Integer messageId){
        messageRepository.deleteById(messageId);
    }

    public Message updateMessage(Integer messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isEmpty() || newMessageText.length() > 255) {
            throw new IllegalArgumentException("Invalid message text.");
        }
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("Message not found."));
        message.setMessageText(newMessageText);
        return messageRepository.save(message);
    }


    // Retrieves all messages posted by a specific user.
    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }


}
