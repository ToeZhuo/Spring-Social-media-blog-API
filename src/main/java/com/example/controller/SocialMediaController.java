package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<Account> register(@RequestBody Account account) {
        Account newAccount = accountService.addAccount(account);

        HttpStatus status = HttpStatus.OK;
        if (newAccount == null) {
            if (accountService.findAccountByUsername(account.getUsername()) != null) {
                status = HttpStatus.CONFLICT;
            } else {
                status = HttpStatus.BAD_REQUEST;
            }
        }
        return ResponseEntity.status(status)
                .body(newAccount);
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Account> login(@RequestBody Account account) {
        Account loginAccount = accountService.validateAccountInfo(account);

        return ResponseEntity.status(loginAccount == null ? HttpStatus.UNAUTHORIZED : HttpStatus.OK)
                .body(loginAccount);
    }

    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message newMessage = messageService.addMessage(message);

        return ResponseEntity.status(newMessage == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
                .body(newMessage);
    }

    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(messageService.findAllMessage());
    }

    @GetMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(messageService.findMessageById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int affectedRow = messageService.deleteMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(affectedRow == -1 ? null : affectedRow);
    }

    @PatchMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> updateMessageById(@PathVariable int messageId,
            @RequestBody Map<String, Object> body) {
        String newText = (String) body.get("messageText");
        int affectedRow = messageService.updateMessageById(messageId, newText);
        return ResponseEntity.status(affectedRow == -1 ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
                .body(affectedRow == -1 ? null : affectedRow);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessagesFromUser(@PathVariable int accountId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(messageService.findAllMessagesByAccountId(accountId));
    }
}
