package com.trainmanagement.service.mapper;

import com.trainmanagement.domain.Booking;
import com.trainmanagement.domain.Customer;
import com.trainmanagement.domain.Train;
import com.trainmanagement.service.dto.BookingDTO;
import com.trainmanagement.service.dto.CustomerDTO;
import com.trainmanagement.service.dto.TrainDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "train", source = "train", qualifiedByName = "trainId")
    BookingDTO toDto(Booking s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("trainId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrainDTO toDtoTrainId(Train train);
}
