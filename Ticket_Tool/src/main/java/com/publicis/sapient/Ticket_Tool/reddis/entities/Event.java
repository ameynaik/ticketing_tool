package com.publicis.sapient.Ticket_Tool.reddis.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.publicis.sapient.Ticket_Tool.EventType;

@RedisHash("Event")
public record Event(@Id Integer id,String name, @Indexed EventType type, @Indexed Integer cityId) {}
