/* point 테이블 */

CREATE TABLE point (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    labeler_id VARCHAR(255),
    is_tmp_point BOOLEAN DEFAULT true,
    point_value BIGINT
);