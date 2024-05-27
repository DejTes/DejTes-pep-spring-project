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


    // Creates new message after validating the message text and the account ID of the user
    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isEmpty() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Invalid message text.");
        }
        if (accountService.getAccountById(message.getPostedBy()) == null) {
            throw new IllegalArgumentException("Invalid Account"); 
        }
        return messageRepository.save(message);
    }

    //Retrieves all messages form the repository
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

        // Retrieves a message by its ID
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    // Deletes a message by its ID and returns the number of rows deleted.
    public int deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }


    // Updaates the text of a message after validating the new message text
    public Message updateMessage(Integer messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            throw new IllegalArgumentException("Invalid message text.");
        }
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("Message not found."));
        message.setMessageText(newMessageText);
        return messageRepository.save(message);
    }
    
// Retrieves all messages posted by a specific user
    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
