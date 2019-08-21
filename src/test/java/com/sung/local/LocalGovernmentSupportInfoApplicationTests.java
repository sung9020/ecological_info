package com.sung.local;

import com.sung.local.dto.LocalGovernmentDto;
import com.sung.local.dto.SupportInfoDto;
import com.sung.local.entity.LocalGovernment;
import com.sung.local.entity.SupportInfo;
import com.sung.local.enums.FileFormat;
import com.sung.local.repository.LocalGovernmentRepository;
import com.sung.local.repository.SupportInfoRepository;
import com.sung.local.utils.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = "test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class LocalGovernmentSupportInfoApplicationTests {


	@Autowired
	LocalGovernmentRepository localGovernmentRepository;

	@Autowired
	SupportInfoRepository supportInfoRepository;


	@Test
	public void CSV_파일_읽기(){
		File file = new File("local_government_support_info.csv");
		List<List<String>> data = FileUtils.readCsv(file);

		assertThat(data, Matchers.notNullValue());
		assertThat(data.size(), Matchers.equalTo(99));
	}

	@Test
	public void 기관코드_DB저장(){
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

		List<LocalGovernment> localGovernmentList = localGovernmentRepository.findAll();

		assertThat(localGovernmentList, Matchers.notNullValue());
		assertThat(localGovernmentList.size(), Matchers.equalTo(98));
	}

	@Test
	public void 지자체협약지원정보_DB저장(){
		File file = new File("local_government_support_info.csv");
		List<List<String>> data = FileUtils.readCsv(file);

		int rowSize = data.size();

		for(int i = 1; i < rowSize; i++){
			List<String> row = data.get(i);
			String localGovernmentCode = "LGM" + String.format("%03d", i);

			SupportInfoDto supportInfoDto = new SupportInfoDto();
			supportInfoDto.setRegionCode(localGovernmentCode);
			supportInfoDto.setSupportTarget(row.get(FileFormat.SUPPORT_TARGET.getCol()));
			supportInfoDto.setUsage(row.get(FileFormat.USAGE.getCol()));
			supportInfoDto.setSupportLimit(row.get(FileFormat.SUPPORT_LIMIT.getCol()));
			supportInfoDto.setRate(row.get(FileFormat.INTEREST_SUBSIDY.getCol()));
			supportInfoDto.setInstitutions(row.get(FileFormat.INSTITUTIONS.getCol()));
			supportInfoDto.setManagement(row.get(FileFormat.MANAGEMENT.getCol()));
			supportInfoDto.setReception(row.get(FileFormat.DEALER.getCol()));

			supportInfoRepository.save(supportInfoDto.toEntity());
		}
		List<SupportInfo> supportInfoList = supportInfoRepository.findAll();
		assertThat(supportInfoList, Matchers.notNullValue());
		assertThat(supportInfoList.size(), Matchers.equalTo(98));
	}

	@Test
	public void contextLoads() {
	}

}
