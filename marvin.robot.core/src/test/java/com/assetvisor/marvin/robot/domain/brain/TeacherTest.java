package com.assetvisor.marvin.robot.domain.brain;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeacherTest {
    @Mock
    private ForInvokingIntelligence forInvokingIntelligence;
    @Mock
    private ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;
    @Mock
    private ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;
    @InjectMocks
    private Teacher teacher;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTeachBrain() {
        // Given
        EnvironmentDescription environmentDescription1 = new EnvironmentDescription(UUID.randomUUID().toString(), "description1");
        EnvironmentDescription environmentDescription2 = new EnvironmentDescription(UUID.randomUUID().toString(), "description2");
        EnvironmentDescription environmentDescription3 = new EnvironmentDescription(UUID.randomUUID().toString(), "description3");
        List<EnvironmentDescription> environmentDescriptionList = List.of(environmentDescription1, environmentDescription2, environmentDescription3);

        given(forPersistingEnvironmentDescriptions.all()).willReturn(List.of(environmentDescription1, environmentDescription2));
        given(forGettingEnvironmentDescriptions.getEnvironmentDescriptions()).willReturn(List.of(environmentDescription3));

        // When
        ArgumentCaptor<List<EnvironmentDescription>> captor = ArgumentCaptor.forClass(List.class);
        teacher.teach();
        // Then
        verify(forInvokingIntelligence).teach(captor.capture());
        assertTrue(captor.getValue().containsAll(environmentDescriptionList));
        verify(forPersistingEnvironmentDescriptions).all();
        verify(forGettingEnvironmentDescriptions).getEnvironmentDescriptions();
        verifyNoMoreInteractions(forInvokingIntelligence, forPersistingEnvironmentDescriptions, forGettingEnvironmentDescriptions);
    }
}