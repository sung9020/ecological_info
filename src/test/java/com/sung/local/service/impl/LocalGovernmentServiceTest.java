package com.sung.local.service.impl;

import com.sung.local.dto.LocalGovernmentDto;
import com.sung.local.dto.SupportInfoDto;
import com.sung.local.entity.LocalGovernment;
import com.sung.local.entity.SupportInfo;
import com.sung.local.enums.FileFormat;
import com.sung.local.repository.LocalGovernmentRepository;
import com.sung.local.utils.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class LocalGovernmentServiceTest {


    @Autowired
    LocalGovernmentRepository localGovernmentRepository;

    @Test
    public void 지자체코드_조회(){
        LocalGovernment localGovernment = localGovernmentRepository.findByRegion("강릉시");
        assertThat(localGovernment.getRegionCode().length(), Matchers.greaterThan(0));
    }

    @Test
    public void 지자체_리스트_조회(){
        List<LocalGovernment> localGovernmentList = localGovernmentRepository.findAll();
        assertThat(localGovernmentList.size(), Matchers.greaterThan(0));
    }


    private void setCsvData(){
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
}