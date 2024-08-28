package com.examly.springapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.examly.springapp.controller.EventController;
import com.examly.springapp.model.Event;
import com.examly.springapp.service.EventService;

public class SpringappApplicationTests {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void backend_testEventControllerFileExists() {
        String filePath = "src/main/java/com/examly/springapp/controller/EventController.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile());
    }

    @Test
    void backend_testEventModelFileExists() {
        String filePath = "src/main/java/com/examly/springapp/model/Event.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile());
    }

    @Test
    void backend_testEventRepositoryFileExists() {
        String filePath = "src/main/java/com/examly/springapp/repository/EventRepository.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile());
    }

    @Test
    void backend_testEventServiceFileExists() {
        String filePath = "src/main/java/com/examly/springapp/service/EventService.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile());
    }

    @Test
    public void backend_testGetEventByIdNotFound() throws Exception {
        when(eventService.getEventById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.getEventById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void backend_testUpdateEventNotFound() throws Exception {
        Event eventToUpdate = new Event();
        eventToUpdate.setId(1L);
        eventToUpdate.setName("Updated Event");

        when(eventService.getEventById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.updateEvent(1L, eventToUpdate);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void backend_testDeleteEventNotFound() throws Exception {
        when(eventService.getEventById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void backend_testCreateEventSuccess() throws Exception {
        Event newEvent = new Event();
        newEvent.setName("New Event");
        newEvent.setDescription("Event Description");
        newEvent.setLocation("Event Location");
        newEvent.setDate("2024-08-15");

        when(eventService.createEvent(any(Event.class))).thenReturn(newEvent);

        ResponseEntity<Event> response = eventController.createEvent(newEvent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New Event", response.getBody().getName());
        verify(eventService, times(1)).createEvent(any(Event.class));
    }

    @Test
    public void backend_testUpdateEventSuccess() throws Exception {
        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setName("Existing Event");
        existingEvent.setDescription("Existing Description");
        existingEvent.setLocation("Existing Location");
        existingEvent.setDate("2024-08-10");

        Event updatedEventDetails = new Event();
        updatedEventDetails.setName("Updated Event");
        updatedEventDetails.setDescription("Updated Description");
        updatedEventDetails.setLocation("Updated Location");
        updatedEventDetails.setDate("2024-08-20");

        when(eventService.getEventById(anyLong())).thenReturn(Optional.of(existingEvent));
        when(eventService.updateEvent(anyLong(), any(Event.class))).thenReturn(existingEvent);

        ResponseEntity<Event> response = eventController.updateEvent(1L, updatedEventDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Existing Event", response.getBody().getName());
        verify(eventService, times(1)).updateEvent(anyLong(), any(Event.class));
    }

    @Test
    public void backend_testDeleteEventSuccess() throws Exception {
        when(eventService.deleteEvent(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventService, times(1)).deleteEvent(anyLong());
    }

    @Test
    public void backend_testGetAllEvents() throws Exception {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("Event 2");

        when(eventService.getAllEvents()).thenReturn(List.of(event1, event2));

        List<Event> events = eventController.getAllEvents();

        assertEquals(2, events.size());
        verify(eventService, times(1)).getAllEvents();
    }

}