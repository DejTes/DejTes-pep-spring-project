package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.example.entity.Message;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isEmpty() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Invalid message text.");
        }
        if (accountService.getAccountById(message.getPostedBy()) == null) {
            throw new IllegalArgumentException("Invalid Account Id"); 
        }
        return messageRepository.save(message);
    }



    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public void deleteMessageById(Integer messageId) {
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

    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
