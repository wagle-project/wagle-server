package com.waglewagle.server.domain.festivalMap.entity;

import com.waglewagle.server.domain.festival.entity.Festival;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "festival_map")
public class FestivalMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer sequence; // 노출 순서

    // --- Ground Overlay 좌표 (이미지 매핑용) ---
    @Column(name = "south_west_lat")
    private Double southWestLat;

    @Column(name = "south_west_lon")
    private Double southWestLon;

    @Column(name = "north_east_lat")
    private Double northEastLat;

    @Column(name = "north_east_lon")
    private Double northEastLon;
}