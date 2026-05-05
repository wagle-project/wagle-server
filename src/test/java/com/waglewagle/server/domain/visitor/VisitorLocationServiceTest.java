package com.waglewagle.server.domain.visitor;

import com.waglewagle.server.domain.festival.entity.Festival;
import com.waglewagle.server.domain.festivalMap.entity.FestivalMap;
import com.waglewagle.server.domain.festivalMap.repository.FestivalMapRepository;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import com.waglewagle.server.domain.visitor.service.VisitorLocationService;
import com.waglewagle.server.global.redis.repository.LocationRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VisitorLocationServiceTest {

    @InjectMocks
    private VisitorLocationService visitorLocationService;

    @Mock
    private FestivalMapRepository festivalMapRepository;

    @Mock
    private LocationRedisRepository locationRedisRepository;

    private Festival festival;
    private FestivalMap festivalMap;

    @BeforeEach
    void setUp() {
        festival = Festival.builder()
                .id(1L)
                .name("테스트 축제")
                .startAt(LocalDateTime.now().minusDays(1))
                .endAt(LocalDateTime.now().plusDays(1))
                .build();

        festivalMap = FestivalMap.builder()
                .id(7L)
                .festival(festival)
                .southWestLat(35.8300)
                .southWestLon(128.7550)
                .northEastLat(35.8350)
                .northEastLon(128.7600)
                .build();
    }

    @Test
    @DisplayName("구역 내부 좌표 전송 시 세션 저장 및 isInside=true 반환")
    void updateLocation_insideMap() {
        // given
        given(festivalMapRepository.findByFestivalId(1L))
                .willReturn(List.of(festivalMap));

        VisitorLocationDTO.LocationUpdateRequest request =
                new VisitorLocationDTO.LocationUpdateRequest(35.8322, 128.7576);

        // when
        VisitorLocationDTO.LocationUpdateResponse response =
                visitorLocationService.updateLocation(1L, "test-uuid", request);

        // then
        assertThat(response.isInside()).isTrue();
        assertThat(response.currentMapId()).isEqualTo(7L);

        // 핵심: 실시간 집계 방식에서는 saveVisitorLocation만 호출되면 됩니다.
        then(locationRedisRepository).should()
                .saveVisitorLocation(eq("test-uuid"), eq(7L), anyString());
    }

    @Test
    @DisplayName("구역 외부 좌표 전송 시 세션 삭제 및 isInside=false 반환")
    void updateLocation_outsideMap() {
        // given
        given(festivalMapRepository.findByFestivalId(1L))
                .willReturn(List.of(festivalMap));

        VisitorLocationDTO.LocationUpdateRequest request =
                new VisitorLocationDTO.LocationUpdateRequest(37.5665, 126.9780);

        // when
        VisitorLocationDTO.LocationUpdateResponse response =
                visitorLocationService.updateLocation(1L, "test-uuid", request);

        // then
        assertThat(response.isInside()).isFalse();
        assertThat(response.currentMapId()).isNull();

        // 구역 밖이면 기존 세션을 삭제해야 함
        then(locationRedisRepository).should().deleteVisitorLocation("test-uuid");
        // 더 이상 increment 같은 메서드는 호출되지 않음
    }

    @Test
    @DisplayName("이미 위치 정보가 있어도 단순히 덮어쓰기(갱신) 처리")
    void updateLocation_alreadyExists() {
        // given
        given(festivalMapRepository.findByFestivalId(1L))
                .willReturn(List.of(festivalMap));

        VisitorLocationDTO.LocationUpdateRequest request =
                new VisitorLocationDTO.LocationUpdateRequest(35.8322, 128.7576);

        // when
        visitorLocationService.updateLocation(1L, "test-uuid", request);

        // then
        // 실시간 집계 방식은 이전 위치가 무엇이었든 상관없이 현재 위치를 저장만 하면 끝입니다.
        then(locationRedisRepository).should()
                .saveVisitorLocation(eq("test-uuid"), eq(7L), anyString());
    }
}