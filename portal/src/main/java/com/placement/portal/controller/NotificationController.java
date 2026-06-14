// controller/NotificationController.java
package com.placement.portal.controller;

import com.placement.portal.config.NotificationManager;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @GetMapping
    public List<String> getAllNotifications() {
        return NotificationManager.getInstance().getAllNotifications();
    }
}