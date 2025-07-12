package com.trainmanagement.service.impl;

import com.trainmanagement.domain.TrainCoach;
import com.trainmanagement.repository.TrainCoachRepository;
import com.trainmanagement.service.TrainCoachService;
import com.trainmanagement.service.dto.TrainCoachDTO;
import com.trainmanagement.service.mapper.TrainCoachMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trainmanagement.domain.TrainCoach}.
 */
@Service
@Transactional
public class TrainCoachServiceImpl implements TrainCoachService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainCoachServiceImpl.class);

    private final TrainCoachRepository trainCoachRepository;

    private final TrainCoachMapper trainCoachMapper;

    public TrainCoachServiceImpl(TrainCoachRepository trainCoachRepository, TrainCoachMapper trainCoachMapper) {
        this.trainCoachRepository = trainCoachRepository;
        this.trainCoachMapper = trainCoachMapper;
    }

    @Override
    public TrainCoachDTO save(TrainCoachDTO trainCoachDTO) {
        LOG.debug("Request to save TrainCoach : {}", trainCoachDTO);
        TrainCoach trainCoach = trainCoachMapper.toEntity(trainCoachDTO);
        trainCoach = trainCoachRepository.save(trainCoach);
        return trainCoachMapper.toDto(trainCoach);
    }

    @Override
    public TrainCoachDTO update(TrainCoachDTO trainCoachDTO) {
        LOG.debug("Request to update TrainCoach : {}", trainCoachDTO);
        TrainCoach trainCoach = trainCoachMapper.toEntity(trainCoachDTO);
        trainCoach = trainCoachRepository.save(trainCoach);
        return trainCoachMapper.toDto(trainCoach);
    }

    @Override
    public Optional<TrainCoachDTO> partialUpdate(TrainCoachDTO trainCoachDTO) {
        LOG.debug("Request to partially update TrainCoach : {}", trainCoachDTO);

        return trainCoachRepository
            .findById(trainCoachDTO.getId())
            .map(existingTrainCoach -> {
                trainCoachMapper.partialUpdate(existingTrainCoach, trainCoachDTO);

                return existingTrainCoach;
            })
            .map(trainCoachRepository::save)
            .map(trainCoachMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainCoachDTO> findAll() {
        LOG.debug("Request to get all TrainCoaches");
        return trainCoachRepository.findAll().stream().map(trainCoachMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainCoachDTO> findOne(Long id) {
        LOG.debug("Request to get TrainCoach : {}", id);
        return trainCoachRepository.findById(id).map(trainCoachMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TrainCoach : {}", id);
        trainCoachRepository.deleteById(id);
    }
}
