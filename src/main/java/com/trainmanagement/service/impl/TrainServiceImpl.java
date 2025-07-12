package com.trainmanagement.service.impl;

import com.trainmanagement.domain.Train;
import com.trainmanagement.repository.TrainRepository;
import com.trainmanagement.service.TrainService;
import com.trainmanagement.service.dto.TrainDTO;
import com.trainmanagement.service.mapper.TrainMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trainmanagement.domain.Train}.
 */
@Service
@Transactional
public class TrainServiceImpl implements TrainService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainServiceImpl.class);

    private final TrainRepository trainRepository;

    private final TrainMapper trainMapper;

    public TrainServiceImpl(TrainRepository trainRepository, TrainMapper trainMapper) {
        this.trainRepository = trainRepository;
        this.trainMapper = trainMapper;
    }

    @Override
    public TrainDTO save(TrainDTO trainDTO) {
        LOG.debug("Request to save Train : {}", trainDTO);
        Train train = trainMapper.toEntity(trainDTO);
        train = trainRepository.save(train);
        return trainMapper.toDto(train);
    }

    @Override
    public TrainDTO update(TrainDTO trainDTO) {
        LOG.debug("Request to update Train : {}", trainDTO);
        Train train = trainMapper.toEntity(trainDTO);
        train = trainRepository.save(train);
        return trainMapper.toDto(train);
    }

    @Override
    public Optional<TrainDTO> partialUpdate(TrainDTO trainDTO) {
        LOG.debug("Request to partially update Train : {}", trainDTO);

        return trainRepository
            .findById(trainDTO.getId())
            .map(existingTrain -> {
                trainMapper.partialUpdate(existingTrain, trainDTO);

                return existingTrain;
            })
            .map(trainRepository::save)
            .map(trainMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainDTO> findOne(Long id) {
        LOG.debug("Request to get Train : {}", id);
        return trainRepository.findById(id).map(trainMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Train : {}", id);
        trainRepository.deleteById(id);
    }
}
