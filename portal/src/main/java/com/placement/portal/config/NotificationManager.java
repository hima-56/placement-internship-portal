// config/NotificationManager.java
package com.placement.portal.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton Design Pattern implementation.
 * Ensures only ONE instance of NotificationManager exists across the application,
 * used to log/track all placement-related notifications centrally.
 */
public class NotificationManager {

    // Single static instance
    private static NotificationManager instance;

    // Stores all notifications generated
    private final List<String> notificationLog;

    // Private constructor prevents external instantiation
    private NotificationManager() {
        notificationLog = new ArrayList<>();
    }

    // Thread-safe lazy initialization
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void notify(String recipientType, String message) {
        String entry = String.format("[%s] To: %s -> %s",
                LocalDateTime.now(), recipientType, message);
        notificationLog.add(entry);
        System.out.println("NOTIFICATION " + entry);
    }

    public List<String> getAllNotifications() {
        return Collections.unmodifiableList(notificationLog);
    }
}