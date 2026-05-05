CREATE TABLE festival (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(100)    NOT NULL,
    description     VARCHAR(500),
    poster_image_url VARCHAR(255),
    start_at        DATETIME        NOT NULL,
    end_at          DATETIME        NOT NULL,
    place_name      VARCHAR(255)    NOT NULL,
    address         VARCHAR(255)    NOT NULL,
    south_west_lat  DOUBLE,
    south_west_lon  DOUBLE,
    north_east_lat  DOUBLE,
    north_east_lon  DOUBLE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE festival_map (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    festival_id     BIGINT          NOT NULL,
    name            VARCHAR(100)    NOT NULL,
    map_image_url   VARCHAR(255)    NOT NULL,
    sequence        INT             NOT NULL,
    south_west_lat  DOUBLE,
    south_west_lon  DOUBLE,
    north_east_lat  DOUBLE,
    north_east_lon  DOUBLE,
    PRIMARY KEY (id),
    FOREIGN KEY (festival_id) REFERENCES festival(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE time_table (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    festival_id     BIGINT          NOT NULL,
    name            VARCHAR(100)    NOT NULL,
    sequence        INT             NOT NULL,
    image_url       VARCHAR(255)    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (festival_id) REFERENCES festival(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE visitor (
    uuid            VARCHAR(36)     NOT NULL,
    festival_id     BIGINT,
    is_terms_agreed TINYINT(1)      NOT NULL,
    created_at      DATETIME,
    last_active_at  DATETIME,
    PRIMARY KEY (uuid),
    FOREIGN KEY (festival_id) REFERENCES festival(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
