/* point 테이블 */

CREATE TABLE point (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    labeler_id VARCHAR(255),
    point_value BIGINT
);

/* 임시포인트 테이블 */
CREATE TABLE temp_point (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    labeler_id VARCHAR(255),
    labeling_uuid VARCHAR(255),
    point_value BIGINT
);