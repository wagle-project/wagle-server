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

    @Column(name = "poster_image_url")
    private String posterImageUrl;

    @Column(name = "start_at", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime startDate;

    @Column(name = "end_at", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime endDate;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "south_west_lat")
    private Double southWestLat;

    @Column(name = "south_west_lon")
    private Double southWestLon;

    @Column(name = "north_east_lat")
    private Double northEastLat;

    @Column(name = "north_east_lon")
    private Double northEastLon;
}