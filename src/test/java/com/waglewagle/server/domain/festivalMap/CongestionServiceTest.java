package com.waglewagle.server.domain.festivalMap;

import com.waglewagle.server.domain.festivalMap.dto.CongestionDTO;
import com.waglewagle.server.domain.festivalMap.entity.FestivalMap;
import com.waglewagle.server.domain.festivalMap.repository.FestivalMapRepository;
import com.waglewagle.server.domain.festivalMap.service.CongestionService;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import com.waglewagle.server.global.redis.repository.LocationRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CongestionServiceTest {

    @InjectMocks
    private CongestionService congestionService;

    @Mock
    private FestivalMapRepository festivalMapRepository;

    @Mock
    private LocationRedisRepository locationRedisRepository;

    private FestivalMap festivalMap;

    @BeforeEach
    void setUp() {
        festivalMap = FestivalMap.builder()
                .id(7L)
                .mapImageUrl("https://example.com/map.png")
                .sequence(1)
                .southWestLat(35.8300)
                .southWestLon(128.7550)
                .northEastLat(35.8350)
                .northEastLon(128.7600)
                .build();
    }

    @Test
    @DisplayName("정상 혼잡도 조회 - 셀별 레벨 분류 및 totalCount 합산 확인")
    void getCongestion_success() {
        // given
        given(festivalMapRepository.findById(7L))
                .willReturn(Optional.of(festivalMap));
        given(locationRedisRepository.getCellCounts(7L))
                .willReturn(Map.of(
                        "cell_A", 1,   // level 1 (< 2명)
                        "cell_B", 2,   // level 2 (2명)
                        "cell_C", 3,   // level 3 (>= 3명)
                        "cell_D", 5    // level 3 (>= 3명)
                ));

        // when
        CongestionDTO.CongestionResponse response = congestionService.getCongestion(7L);

        // then
        assertThat(response.totalCount()).isEqualTo(11);
        assertThat(response.zones()).hasSize(4);

        Map<String, Integer> levelMap = response.zones().stream()
                .collect(Collectors.toMap(
                        CongestionDTO.ZoneInfo::h3Index,
                        CongestionDTO.ZoneInfo::level
                ));

        assertThat(levelMap.get("cell_A")).isEqualTo(1);
        assertThat(levelMap.get("cell_B")).isEqualTo(2);
        assertThat(levelMap.get("cell_C")).isEqualTo(3);
        assertThat(levelMap.get("cell_D")).isEqualTo(3);
    }

    @Test
    @DisplayName("방문자가 없을 경우 zones 빈 리스트, totalCount=0 반환")
    void getCongestion_empty() {
        // given
        given(festivalMapRepository.findById(7L))
                .willReturn(Optional.of(festivalMap));
        given(locationRedisRepository.getCellCounts(7L))
                .willReturn(Map.of());

        // when
        CongestionDTO.CongestionResponse response = congestionService.getCongestion(7L);

        // then
        assertThat(response.totalCount()).isEqualTo(0);
        assertThat(response.zones()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 mapId 조회 시 예외 발생")
    void getCongestion_mapNotFound() {
        // given
        given(festivalMapRepository.findById(999L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> congestionService.getCongestion(999L))
                .isInstanceOf(GeneralException.class);
    }

    @Test
    @DisplayName("혼잡도 레벨 경계값 확인 - 0명/1명/2명/3명")
    void getCongestion_levelBoundary() {
        // given
        given(festivalMapRepository.findById(7L))
                .willReturn(Optional.of(festivalMap));
        given(locationRedisRepository.getCellCounts(7L))
                .willReturn(Map.of(
                        "cell_1", 0,   // level 1
                        "cell_2", 1,   // level 1
                        "cell_3", 2,   // level 2
                        "cell_4", 3,   // level 3
                        "cell_5", 10   // level 3
                ));

        // when
        CongestionDTO.CongestionResponse response = congestionService.getCongestion(7L);

        // then
        Map<String, Integer> levelMap = response.zones().stream()
                .collect(Collectors.toMap(
                        CongestionDTO.ZoneInfo::h3Index,
                        CongestionDTO.ZoneInfo::level
                ));

        assertThat(levelMap.get("cell_1")).isEqualTo(1);
        assertThat(levelMap.get("cell_2")).isEqualTo(1);
        assertThat(levelMap.get("cell_3")).isEqualTo(2);
        assertThat(levelMap.get("cell_4")).isEqualTo(3);
        assertThat(levelMap.get("cell_5")).isEqualTo(3);
    }
}