import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    @Test
    void checkBloodPressureTest() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        File repoFile = new File("patients.txt");
        PatientInfoRepository pIR;
        PatientInfoRepository patientInfoRepository = new PatientInfoFileRepository(repoFile, mapper);
        pIR = Mockito.spy(patientInfoRepository);
        String id1 = pIR.add(
                new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
        );
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure(id1, currentPressure);
        Mockito.verify(alertService,Mockito.times(1))
                .send("Warning, patient with id: " + id1 + ", need help");
    }

    @Test
    void checkTemperatureTest() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        File repoFile = new File("patients.txt");
        PatientInfoRepository pIR;
        PatientInfoRepository patientInfoRepository = new PatientInfoFileRepository(repoFile, mapper);
        pIR = Mockito.spy(patientInfoRepository);
        String id1 = pIR.add(
                new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
        );
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        BigDecimal currentTemperature = new BigDecimal("37.9");
        medicalService.checkTemperature(id1, currentTemperature);
        Mockito.verify(alertService,Mockito.times(1))
                .send("Warning, patient with id: " + id1 + ", need help");
    }

    @Test
    void checkIndicatorsNormTest() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        File repoFile = new File("patients.txt");
        PatientInfoRepository pIR;
        PatientInfoRepository patientInfoRepository = new PatientInfoFileRepository(repoFile, mapper);
        pIR = Mockito.spy(patientInfoRepository);
        String id1 = pIR.add(
                new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
        );
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        BloodPressure currentPressure = new BloodPressure(120, 80);
        medicalService.checkBloodPressure(id1, currentPressure);
        Mockito.verify(alertService,Mockito.times(0))
                .send("Warning, patient with id: " + id1 + ", need help");
        BigDecimal currentTemperature = new BigDecimal("36.65");
        medicalService.checkTemperature(id1, currentTemperature);
        Mockito.verify(alertService,Mockito.times(0))
                .send("Warning, patient with id: " + id1 + ", need help");
    }
}
