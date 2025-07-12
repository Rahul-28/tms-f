package com.trainmanagement.service.mapper;

import com.trainmanagement.domain.Booking;
import com.trainmanagement.domain.Payment;
import com.trainmanagement.service.dto.BookingDTO;
import com.trainmanagement.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingId")
    PaymentDTO toDto(Payment s);

    @Named("bookingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookingDTO toDtoBookingId(Booking booking);
}
