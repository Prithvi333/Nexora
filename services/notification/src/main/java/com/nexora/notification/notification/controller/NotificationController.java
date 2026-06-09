package com.nexora.notification.notification.controller;

import com.nexora.notification.notification.service.NotificationService;
import com.nexora.notification.response.notification.NotificationResponse;
import com.nexora.notification.utils.constants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(IUrls.NOTIFICATION)
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    ResponseEntity<List<NotificationResponse>> getAllNotification(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(notificationService.getAllNotification(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

}
