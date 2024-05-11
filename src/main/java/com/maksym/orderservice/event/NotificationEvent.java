package com.maksym.orderservice.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NotificationEvent {
    private String title;
    private String description;
}
