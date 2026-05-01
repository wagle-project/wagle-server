package com.waglewagle.server.domain.visitor;

import com.waglewagle.server.domain.festival.entity.Festival;
import com.waglewagle.server.domain.festivalMap.entity.FestivalMap;
import com.waglewagle.server.domain.festivalMap.repository.FestivalMapRepository;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import com.waglewagle.server.domain.visitor.repository.VisitorLocationService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

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
                .description("테스트 축제 설명")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .placeName("테스트 장소")
                .address("대구광역시 수성구")
                .build();

        // 대구 수성못 근처 직사각형 구역으로 가정
        festivalMap = FestivalMap.builder()
                .id(7L)
                .festival(festival)
                .imageUrl("https://example.com/map.png")
                .sequence(1)
                .southWestLat(35.8300)
                .southWestLon(128.7550)
                .northEastLat(35.8350)
                .northEastLon(128.7600)
                .build();
    }

    @Test
    @DisplayName("구역 내부 좌표 전송 시 isInside=true, currentMapId 반환")
    void updateLocation_insideMap() {
        // given
        given(festivalMapRepository.findByFestivalId(1L))
                .willReturn(List.of(festivalMap));
        given(locationRedisRepository.getVisitorLocation("test-uuid"))
                .willReturn(Optional.empty());

        VisitorLocationDTO.LocationUpdateRequest request =
                new VisitorLocationDTO.LocationUpdateRequest(35.8322, 128.7576);

        // when
        VisitorLocationDTO.LocationUpdateResponse response =
                visitorLocationService.updateLocation(1L, "test-uuid", request);

        // then
        assertThat(response.isInside()).isTrue();
        assertThat(response.currentMapId()).isEqualTo(7L);
        assertThat(response.locationUpdateInterval()).isEqualTo(3000L);

        // Redis 저장 및 카운트 증가 호출 확인
        then(locationRedisRepository).should()
                .saveVisitorLocation(eq("test-uuid"), eq(7L), anyString());

        then(locationRedisRepository).should()
                .incrementCell(eq(7L), anyString());
    }

    @Test
    @DisplayName("구역 외부 좌표 전송 시 isInside=false, currentMapId=null 반환")
    void updateLocation_outsideMap() {
        // given
        given(festivalMapRepository.findByFestivalId(1L))
                .willReturn(List.of(festivalMap));
        given(locationRedisRepository.getVisitorLocation("test-uuid"))
                .willReturn(Optional.empty());

        // 구역 밖 좌표
        VisitorLocationDTO.LocationUpdateRequest request =
                new VisitorLocationDTO.LocationUpdateRequest(37.5665, 126.9780);

        // when
        VisitorLocationDTO.LocationUpdateResponse response =
                visitorLocationService.updateLocation(1L, "test-uuid", request);

        // then
        assertThat(response.isInside()).isFalse();
        assertThat(response.currentMapId()).isNull();

        then(locationRedisRepository).should().deleteVisitorLocation("test-uuid");
        then(locationRedisRepository).should(never()).incrementCell(anyLong(), anyString());
    }

    @Test
    @DisplayName("이전 위치가 있을 경우 이전 셀 카운트 감소 후 새 위치 저장")
    void updateLocation_decrementPreviousCell() {
        // given
        given(festivalMapRepository.findByFestivalId(1L))
                .willReturn(List.of(festivalMap));
        // 이전 위치: 같은 맵의 다른 셀
        given(locationRedisRepository.getVisitorLocation("test-uuid"))
                .willReturn(Optional.of("7:8928308280fffff"));

        VisitorLocationDTO.LocationUpdateRequest request =
                new VisitorLocationDTO.LocationUpdateRequest(35.8322, 128.7576);

        // when
        visitorLocationService.updateLocation(1L, "test-uuid", request);

        // then: 이전 셀 감소 확인
        then(locationRedisRepository).should().decrementCell(7L, "8928308280fffff");
        // 새 셀 증가 확인
        then(locationRedisRepository).should().incrementCell(eq(7L), anyString());
    }

    @Test
    @DisplayName("구역 내부에 있다가 외부로 나가면 이전 셀 카운트 감소")
    void updateLocation_insideToOutside() {
        // given
        given(festivalMapRepository.findByFestivalId(1L))
                .willReturn(List.of(festivalMap));
        given(locationRedisRepository.getVisitorLocation("test-uuid"))
                .willReturn(Optional.of("7:8928308280fffff"));

        // 구역 밖 좌표
        VisitorLocationDTO.LocationUpdateRequest request =
                new VisitorLocationDTO.LocationUpdateRequest(37.5665, 126.9780);

        // when
        visitorLocationService.updateLocation(1L, "test-uuid", request);

        // then
        then(locationRedisRepository).should().decrementCell(7L, "8928308280fffff");
        then(locationRedisRepository).should().deleteVisitorLocation("test-uuid");
        then(locationRedisRepository).should(never()).incrementCell(anyLong(), anyString());
    }
}