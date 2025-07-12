package com.trainmanagement.service.impl;

import com.trainmanagement.domain.CoachType;
import com.trainmanagement.repository.CoachTypeRepository;
import com.trainmanagement.service.CoachTypeService;
import com.trainmanagement.service.dto.CoachTypeDTO;
import com.trainmanagement.service.mapper.CoachTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trainmanagement.domain.CoachType}.
 */
@Service
@Transactional
public class CoachTypeServiceImpl implements CoachTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CoachTypeServiceImpl.class);

    private final CoachTypeRepository coachTypeRepository;

    private final CoachTypeMapper coachTypeMapper;

    public CoachTypeServiceImpl(CoachTypeRepository coachTypeRepository, CoachTypeMapper coachTypeMapper) {
        this.coachTypeRepository = coachTypeRepository;
        this.coachTypeMapper = coachTypeMapper;
    }

    @Override
    public CoachTypeDTO save(CoachTypeDTO coachTypeDTO) {
        LOG.debug("Request to save CoachType : {}", coachTypeDTO);
        CoachType coachType = coachTypeMapper.toEntity(coachTypeDTO);
        coachType = coachTypeRepository.save(coachType);
        return coachTypeMapper.toDto(coachType);
    }

    @Override
    public CoachTypeDTO update(CoachTypeDTO coachTypeDTO) {
        LOG.debug("Request to update CoachType : {}", coachTypeDTO);
        CoachType coachType = coachTypeMapper.toEntity(coachTypeDTO);
        coachType = coachTypeRepository.save(coachType);
        return coachTypeMapper.toDto(coachType);
    }

    @Override
    public Optional<CoachTypeDTO> partialUpdate(CoachTypeDTO coachTypeDTO) {
        LOG.debug("Request to partially update CoachType : {}", coachTypeDTO);

        return coachTypeRepository
            .findById(coachTypeDTO.getId())
            .map(existingCoachType -> {
                coachTypeMapper.partialUpdate(existingCoachType, coachTypeDTO);

                return existingCoachType;
            })
            .map(coachTypeRepository::save)
            .map(coachTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachTypeDTO> findAll() {
        LOG.debug("Request to get all CoachTypes");
        return coachTypeRepository.findAll().stream().map(coachTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CoachTypeDTO> findOne(Long id) {
        LOG.debug("Request to get CoachType : {}", id);
        return coachTypeRepository.findById(id).map(coachTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CoachType : {}", id);
        coachTypeRepository.deleteById(id);
    }
}
