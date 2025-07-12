package com.trainmanagement.service.mapper;

import com.trainmanagement.domain.Invoice;
import com.trainmanagement.domain.Payment;
import com.trainmanagement.service.dto.InvoiceDTO;
import com.trainmanagement.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    InvoiceDTO toDto(Invoice s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
