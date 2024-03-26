package com.maksym.orderservice.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEvent {
    private String title;
    private String description;
}
