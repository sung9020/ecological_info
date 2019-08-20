package com.sung.local.service.impl;

import com.sung.local.dto.LocalGovernmentDto;
import com.sung.local.entity.LocalGovernment;
import com.sung.local.enums.ErrorFormat;
import com.sung.local.enums.FileFormat;
import com.sung.local.repository.LocalGovernmentRepository;
import com.sung.local.service.LocalGovernmentInterface;
import com.sung.local.utils.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Service
public class LocalGovernmentService implements LocalGovernmentInterface {

    @Autowired
    LocalGovernmentRepository localGovernmentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public LocalGovernmentDto getLocalGovernment(String region) {
        LocalGovernment localGovernment = Optional.ofNullable(localGovernmentRepository.findByRegion(region))
                .orElseThrow(() -> new IllegalArgumentException(ErrorFormat.NOT_FOUND_REGION_ERROR.getMsg()));

        LocalGovernmentDto localGovernmentDto = modelMapper.map(localGovernment, LocalGovernmentDto.class);

        return localGovernmentDto;
    }

    @Override
    public List<LocalGovernmentDto> getLocalGovernmentList() {
        List<LocalGovernmentDto> localGovernmentDtoList = localGovernmentRepository.findAll().stream()
                .map(LocalGovernmentDto::new)
                .collect(Collectors.toList());

        return localGovernmentDtoList;
    }
}
