package com.sung.local.service.impl;

import com.sung.local.dto.LocalGovernmentDto;
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
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles(profiles = "test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class LocalGovernmentServiceTest {


    @Autowired
    LocalGovernmentRepository localGovernmentRepository;


}