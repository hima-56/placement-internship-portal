// config/PostingFactory.java
package com.placement.portal.config;

import com.placement.portal.model.*;
import org.springframework.stereotype.Component;

@Component
public class PostingFactory {

    public static final String TYPE_JOB = "JOB";
    public static final String TYPE_INTERNSHIP = "INTERNSHIP";

    public Posting createPosting(String type) {
        return switch (type.toUpperCase()) {
            case TYPE_JOB         -> new JobPosting();
            case TYPE_INTERNSHIP  -> new InternshipPosting();
            default -> throw new IllegalArgumentException("Unknown posting type: " + type);
        };
    }
}