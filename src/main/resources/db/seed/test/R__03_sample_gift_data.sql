/* gift */
INSERT INTO gift(admin_id, title, gift_type, expected_draw_date) VALUES('dusik', '바나나 기프티콘 이벤트', 'GIFTICON', '2022-11-11');
INSERT INTO gift(admin_id, title, gift_type, expected_draw_date) VALUES('dusik', '초콜릿 기프티콘 이벤트', 'GIFTICON', '2022-12-12');


/* gifticon */
INSERT INTO gifticon(title, code, expired_date, gift_id) VALUES('바나나1', 'asdas-fdsfs-asdasd', '2025-11-11', 1);
INSERT INTO gifticon(title, code, expired_date, gift_id) VALUES('초콜릿1', 'asdas-fdsfs-asdasd', '2025-11-11', 2);

INSERT INTO apply_gift(gift_id, labeler_id) VALUES(1, 'gildong');
INSERT INTO apply_gift(gift_id, labeler_id) VALUES(2, 'gildong');
INSERT INTO apply_gift(gift_id, labeler_id) VALUES(2, 'gildong');
INSERT INTO apply_gift(gift_id, labeler_id) VALUES(2, 'gildong');
INSERT INTO apply_gift(gift_id, labeler_id) VALUES(2, 'gildong');