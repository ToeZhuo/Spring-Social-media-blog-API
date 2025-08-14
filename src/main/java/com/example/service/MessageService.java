package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    // Add a new message
    public Message addMessage(Message message) {
        if (message.getMessageText() == "" ||
                message.getMessageText().length() > 255 ||
                accountRepository.findById(message.getPostedBy()).isEmpty()) {
            return null;
        }
        return messageRepository.save(message);
    }

    public List<Message> findAllMessage() {
        return messageRepository.findAll();
    }

    public Message findMessageById(int messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isEmpty()) {
            return null;
        }
        return message.get();
    }

    public int deleteMessageById(int messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isEmpty()) {
            return -1;
        }
        messageRepository.deleteById(messageId);
        return 1;
    }

    public int updateMessageById(int messageId, String newText) {
        if (newText == "" || newText.length() > 255) {
            return -1;
        }

        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty()) {
            return -1;
        }

        Message message = optionalMessage.get();
        message.setMessageText(newText);
        messageRepository.save(message);

        return 1;
    }

    public List<Message> findAllMessagesByAccountId(int accountId) {
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}
