/* point 테이블 */

CREATE TABLE point (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    labeler_id VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    point_value BIGINT
);

/* 임시포인트 테이블 */
CREATE TABLE temp_point (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    labeling_uuid VARCHAR(255),
    point_value BIGINT,
    point_id BIGINT,
    status VARCHAR(15),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY(point_id) REFERENCES point(id)
);

/* Stamp 테이블 */
CREATE TABLE stamp (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    labeler_id VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    stamp_value INT
);

/* Gift 테이블 */
CREATE TABLE gift (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    admin_id VARCHAR(255),
    title VARCHAR(63),
    expected_draw_date DATE,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    gift_type VARCHAR(63)
);

/* Gifticon 테이블 */
CREATE TABLE gifticon (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(63),
    code VARCHAR(63),
    expired_date DATE,
    gift_id BIGINT NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY(gift_id) REFERENCES gift(id)
);

/* apply gift 테이블 */
CREATE TABLE apply_gift (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    gift_id BIGINT NOT NULL,
    labeler_id VARCHAR(255),
    apply_status VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY(gift_id) REFERENCES gift(id)
);