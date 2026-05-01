package com.waglewagle.server.domain.festival.service;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.domain.festival.entity.Festival;
import com.waglewagle.server.domain.festival.repository.FestivalRepository;
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public List<FestivalDTO.FestivalSummary> getRecommendedFestivals() {

        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 5);

        // 1. 진행 중인 축제 확인
        List<Festival> ongoing = festivalRepository.findOngoing(now, pageable);
        if (!ongoing.isEmpty()) {
            return ongoing.stream().map(FestivalDTO.FestivalSummary::from).toList();
        }

        // 2. 예정된 축제 확인
        List<Festival> upcoming = festivalRepository.findUpcoming(now, pageable);
        if (!upcoming.isEmpty()) {
            return upcoming.stream().map(FestivalDTO.FestivalSummary::from).toList();
        }

        // 3. 지난 축제 확인
        List<Festival> end = festivalRepository.findEnd(now, pageable);
        if (!end.isEmpty()) {
            return end.stream().map(FestivalDTO.FestivalSummary::from).toList();
        }

        throw new GeneralException(GeneralErrorCode.NOT_FOUND);

        //개최중인 것 중에 시작 날짜가 제일 최신인 거
        //혹시 개최 중인 축제가 없으면 개최 준비 중 중에 시작 날짜가 제일 가까운 거
        //이것도 없으면 개최 끝난 축제 중에라도 끝난 날짜가 제일 가까운 거

        //entity에 status를 추가하면 스케줄러를 이용해서 자동 DB 업데이트 필요
        // -> 이런 경우는 데이터 수가 많고 사용자가 많을 시 사용하면 유리

        //그냥 entity 수정 없이 쿼리로 status 계산
        // -> 데이터 수가 적고 사용자가 적을 시 사용하면 유리
    }

    public Page<FestivalDTO.FestivalSummary> getFastivals(
            String keyword, int page, int size) {

        //slice도 고려(pageResponseDTO 변경 해야 함, 프론트와 백과 의논) -> 데이터가 많을 시 유리

        //페이징 정보를 담기 사용(page 사용을 위해 필수)
        Pageable pageable = PageRequest.of(page, size);

        //keyword가 null일 경우 어떻게 하는가?(백, 프론트와 의논)

        //데이터를 page 형식으로 찾음, keyword가 축제 이름에 들어가는 걸 찾음
        Page<Festival> festivalPage = festivalRepository.findByNameContaining(keyword, pageable);

        //데이터가 없을 시 exception을 발생 시킴
        if (festivalPage.isEmpty()) {
            throw new GeneralException(GeneralErrorCode.NOT_FOUND);
        }

        //DTO로 변환
        return festivalPage.map(FestivalDTO.FestivalSummary::from);
    }


    public FestivalDTO.FestivalDetail getFestivalDetail(Long festivalId) {

        //festivalId에 맞는 festival을 찾음
        Optional<Festival> festivalOptional = festivalRepository.findById(festivalId);

        //값이 없을 시 Exception발생 시키고 값이 있으면 Optional 해제
        Festival festival = festivalOptional.orElseThrow(() ->
                new GeneralException(GeneralErrorCode.NOT_FOUND));

        //DTO로 변환시키고 반환
        return FestivalDTO.FestivalDetail.from(festival);
    }
}
