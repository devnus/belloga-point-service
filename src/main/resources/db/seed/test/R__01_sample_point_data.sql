/* point */
INSERT INTO point(labeler_id, point_value) VALUES('gildong', 1000000);
INSERT INTO point(labeler_id, point_value) VALUES('dusik', 2000000);
/* temp point */
INSERT INTO temp_point(point_id, point_value, status) VALUES(1, 100, 'NOT_CHANGED');
INSERT INTO temp_point(point_id, point_value, status) VALUES(1, 200, 'NOT_CHANGED');
INSERT INTO temp_point(point_id, point_value, status) VALUES(1, 1000000, 'CHANGED');
INSERT INTO temp_point(point_id, point_value, status) VALUES(2, 200, 'NOT_CHANGED'); /* 두식이의 임시포인트 */
