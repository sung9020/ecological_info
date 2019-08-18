package com.sung.local.service.impl;

import com.sung.local.dto.LocalGovernmentDto;
import com.sung.local.entity.LocalGovernment;
import com.sung.local.enums.FileFormat;
import com.sung.local.repository.LocalGovernmentRepository;
import com.sung.local.service.LocalGovernmentInterface;
import com.sung.local.utils.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

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

    @PostConstruct
    public void setUp(){
        File file = new File("local_government_support_info.csv");
        List<List<String>> data = FileUtils.readCsv(file);
        int rowSize = data.size();

        for(int i = 1; i < rowSize; i++){
            List<String> row = data.get(i);
            String localGovernmentCode = "LGM" + String.format("%03d", i);

            LocalGovernmentDto localGovernmentDto = new LocalGovernmentDto();
            localGovernmentDto.setRegion(row.get(FileFormat.LOCAL_GOVERNMENT.getCol()));
            localGovernmentDto.setRegionCode(localGovernmentCode);
            localGovernmentRepository.save(localGovernmentDto.toEntity());
        }
    }


    @Override
    public LocalGovernmentDto getLocalGovernment(String region) {
        LocalGovernment localGovernment = localGovernmentRepository.findByRegion(region);
        LocalGovernmentDto localGovernmentDto = modelMapper.map(localGovernment, LocalGovernmentDto.class);
        return localGovernmentDto;
    }
}
