-- 1. Festival
INSERT INTO festival (name, description, poster_url, start_date, end_date, place_name, address, min_lat, max_lat, min_lon, max_lon)
VALUES
    ('2026 영남대 천마대동제', '영남대학교 봄 축제', 'poster_url_1', '2026-05-20 10:00:00', '2026-05-22 23:00:00', '영남대학교', '경산시 대학로 280', 35.820, 35.840, 128.740, 128.760),
    ('대구 치맥 페스티벌', '전국 최대 규모 치맥 파티', 'poster_url_2', '2026-07-03 16:00:00', '2026-07-07 22:00:00', '두류공원', '대구 달서구 공원순환로 36', 35.845, 35.855, 128.550, 128.570),
    ('서울 밤도깨비 야시장', '한강 야경과 함께하는 푸드트럭', 'poster_url_3', '2026-08-14 18:00:00', '2026-08-16 23:59:59', '반포한강공원', '서울 서초구 신반포로11길 40', 37.505, 37.515, 126.995, 127.005),
    ('부산 불꽃축제', '광안리 해변의 화려한 밤하늘', 'poster_url_4', '2026-11-07 19:00:00', '2026-11-07 21:00:00', '광안리 해수욕장', '부산 수영구 광안해변로 219', 35.150, 35.160, 129.115, 129.125);

-- 2. Festival Map
INSERT INTO festival_map (festival_id, name, image_url, sequence, south_west_lat, south_west_lon, north_east_lat, north_east_lon)
VALUES
    (1, '2026 영남대 천마대동제', 'map_url_1_1', 1, 35.825, 128.745, 35.835, 128.755), -- 영남대
    (2, '대구 치맥 페스티벌', 'map_url_2_1', 1, 35.848, 128.555, 35.852, 128.565), -- 대구
    (3, '서울 밤도깨비 야시장', 'map_url_3_1', 1, 37.508, 126.998, 37.512, 127.002), -- 서울
    (4, '부산 불꽃축제', 'map_url_4_1', 1, 35.153, 129.118, 35.157, 129.122); -- 부산

-- 3. Time Table
INSERT INTO time_table (festival_id, name, image_url, sequence)
VALUES
    (1, '2026 영남대 천마대동제', 'timetable_url_1_1', 1),
    (1, '2026 영남대 천마대동제', 'timetable_url_1_2', 2),
    (2, '대구 치맥 페스티벌', 'timetable_url_2_1', 1),
    (3, '서울 밤도깨비 야시장', 'timetable_url_3_1', 1),
    (4, '부산 불꽃축제', 'timetable_url_4_1', 1);