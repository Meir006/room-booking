package kz.sdu.roombooking.booking.dto;

import java.util.Optional;

import org.springframework.data.domain.Page;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.RoomSlot;

public record BookingPageData(Optional<BookingRequest> activeRequest, Page<RoomSlot> rooms) {}
