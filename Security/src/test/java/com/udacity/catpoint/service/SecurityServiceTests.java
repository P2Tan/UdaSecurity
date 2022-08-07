package com.udacity.catpoint.service;

import com.udacity.catpoint.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTests {

    private final String random = UUID.randomUUID().toString();
    @Mock
    Sensor sensor;
    @Mock
    SecurityService securityService;
    @Mock
    SecurityRepository securityRepository;
    @Mock
    FakeImageService imageService;

    private Set<Sensor> getSensors(boolean active, int count) {
        Set<Sensor> sensors = new HashSet<>();
        for (int i = 0; i <= count; i++) {
            sensors.add(new Sensor(random, SensorType.DOOR));
        }
        sensors.forEach(it -> it.setActive(active));
        return sensors;
    }

    @BeforeEach
    public void setUp() {
        securityService = new SecurityService(securityRepository, imageService);
    }

    @ParameterizedTest
    @EnumSource(value = ArmingStatus.class, names = {"ARMED_HOME", "ARMED_AWAY"})
    public void alarmArmedAndSensorActivated_pendingAlarmStatus(ArmingStatus armingStatus) {
        when(securityRepository.getSensors()).thenReturn(getSensors(true, 2));
        when(securityService.getArmingStatus()).thenReturn(armingStatus);
        when(securityService.getAlarmStatus()).thenReturn(AlarmStatus.NO_ALARM);
        securityService.changeSensorActivationStatus(sensor, true);
        ArgumentCaptor<AlarmStatus> captor = ArgumentCaptor.forClass(AlarmStatus.class);
        verify(securityRepository, Mockito.atMostOnce()).setAlarmStatus(captor.capture());
        assertEquals(captor.getValue(), AlarmStatus.PENDING_ALARM);
    }
}