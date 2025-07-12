package com.trainmanagement.service.impl;

import com.trainmanagement.domain.Booking;
import com.trainmanagement.repository.BookingRepository;
import com.trainmanagement.service.BookingService;
import com.trainmanagement.service.dto.BookingDTO;
import com.trainmanagement.service.mapper.BookingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trainmanagement.domain.Booking}.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDTO save(BookingDTO bookingDTO) {
        LOG.debug("Request to save Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDTO update(BookingDTO bookingDTO) {
        LOG.debug("Request to update Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public Optional<BookingDTO> partialUpdate(BookingDTO bookingDTO) {
        LOG.debug("Request to partially update Booking : {}", bookingDTO);

        return bookingRepository
            .findById(bookingDTO.getId())
            .map(existingBooking -> {
                bookingMapper.partialUpdate(existingBooking, bookingDTO);

                return existingBooking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        LOG.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
    }
}
