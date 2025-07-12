package com.trainmanagement.service.mapper;

import com.trainmanagement.domain.Booking;
import com.trainmanagement.domain.Passenger;
import com.trainmanagement.service.dto.BookingDTO;
import com.trainmanagement.service.dto.PassengerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Passenger} and its DTO {@link PassengerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PassengerMapper extends EntityMapper<PassengerDTO, Passenger> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingId")
    PassengerDTO toDto(Passenger s);

    @Named("bookingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookingDTO toDtoBookingId(Booking booking);
}
