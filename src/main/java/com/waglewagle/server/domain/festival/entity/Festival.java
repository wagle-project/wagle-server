package com.waglewagle.server.domain.festival.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "festival")
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "place_name", nullable = false)
    private String placeName; // 축제 대표 장소명

    @Column(name = "address", nullable = false)
    private String address;   // 상세 주소

    // --- 축제 허용 범위 (Geographical Bounds) ---
    @Column(name = "min_lat")
    private Double minLat;

    @Column(name = "max_lat")
    private Double maxLat;

    @Column(name = "min_lon")
    private Double minLon;

    @Column(name = "max_lon")
    private Double maxLon;
}