-- Thêm câu hỏi theo thứ tự mới

INSERT INTO question (id_question, question_key, question, question_type, unit_label) VALUES
(1, NULL, 'Bạn muốn mình gọi bạn là ....?', 'inputText', NULL),
(2, 'GENDER', 'Giới tính của bạn là gì?', 'selection', NULL),
(3, NULL, 'Tuổi của bạn?', 'inputNumber', 'tuổi'),
(4, NULL, 'Bạn cao bao nhiêu?', 'slider', 'cm'),
(5, 'GOAL', 'Mục tiêu bạn muốn hướng đến?', 'selection', NULL),
(6, NULL, 'Cân nặng hiện tại của bạn là?', 'inputNumber', 'kg'),
(7, NULL, 'Cân nặng mong muốn của bạn là?', 'inputNumber', 'kg'),
(8, 'ACTIVITY', 'Bạn cảm thấy mình hoạt động ở mức nào?', 'selection', NULL);

-- answer
INSERT INTO answer (id_answer, id_question, answer)
VALUES
(1, 2, 'MALE'),
(2, 2, 'FEMALE'),
(3, 2, 'OTHER');

INSERT INTO answer (id_answer, id_question, answer)
VALUES
(4, 5, 'LOSE_WEIGHT'),
(5, 5, 'KEEP_FIT'),
(6, 5, 'GAIN_WEIGHT');

INSERT INTO answer (id_answer, id_question, answer)
VALUES
(7, 8, 'SEDENTARY'),
(8, 8, 'LIGHT_ACTIVE'),
(9, 8, 'MODERATE_ACTIVE'),
(10, 8, 'HEAVY_ACTIVE'),
(11, 8, 'VERY_HEAVY_ACTIVE');
-- ----------------------------------------------------------------------------------------- --
INSERT INTO workout_exercise (name, met_value) VALUES 
-- --- Nhóm Đi bộ & Chạy ---
('Đi bộ nhẹ nhàng (đi dạo)', 2.5),
('Đi bộ nhanh (tập thể dục)', 3.8),
('Chạy bộ chậm (Jogging)', 7.0),
('Chạy nhanh (Tốc độ cao)', 11.5),
('Leo cầu thang', 8.0),

-- --- Nhóm Gym & Thể hình ---
('Tập tạ (Cường độ thường)', 3.5),
('Tập tạ (Cường độ cao/Powerlifting)', 6.0),
('Calisthenics (Hít đất, Gập bụng)', 3.8),
('HIIT (Cardio cường độ cao)', 8.0),
('Squat (Không tạ)', 5.0),

-- --- Nhóm Thể thao ---
('Đạp xe (Thư giãn < 16km/h)', 4.0),
('Đạp xe (Tập luyện > 20km/h)', 8.0),
('Bơi lội (Tự do, tốc độ vừa)', 5.8),
('Bơi lội (Tốc độ cao)', 9.8),
('Yoga (Hatha/Stretching)', 2.5),
('Pilates', 3.0),
('Nhảy dây (Tốc độ vừa)', 10.0),
('Đấm bốc (Boxing - Tập với bao cát)', 5.5),

-- --- Nhóm Thể thao đối kháng/Đồng đội ---
('Bóng đá (Thi đấu)', 10.0),
('Bóng rổ (Chơi thường)', 6.5),
('Cầu lông (Chơi giải trí)', 4.5),
('Tennis (Đánh đơn)', 7.0);


-- Insert dữ liệu vào bảng foods
INSERT INTO foods (food_name, calories_per_100g, protein_per_100g, carbs_per_100g, fat_per_100g, image_url) VALUES 
-- --- Nhóm Tinh bột (Carbs) ---
('Cơm trắng (Nấu chín)', 130, 2.7, 28.0, 0.3, 'https://placehold.co/600x400?text=Com+Trang'),
('Khoai lang luộc', 86, 1.6, 20.1, 0.1, 'https://placehold.co/600x400?text=Khoai+Lang'),
('Bánh mì (Việt Nam)', 264, 8.5, 52.0, 3.2, 'https://placehold.co/600x400?text=Banh+Mi'),
('Yến mạch (Đã nấu)', 68, 2.4, 12.0, 1.4, 'https://placehold.co/600x400?text=Yen+Mach'),

-- --- Nhóm Đạm (Protein) ---
('Ức gà (Luộc/Hấp)', 165, 31.0, 0.0, 3.6, 'https://placehold.co/600x400?text=Uc+Ga'),
('Thịt bò (Thăn, nạc)', 250, 26.0, 0.0, 15.0, 'https://placehold.co/600x400?text=Thit+Bo'),
('Trứng gà (Luộc)', 155, 13.0, 1.1, 11.0, 'https://placehold.co/600x400?text=Trung+Ga'),
('Cá hồi (Áp chảo)', 208, 20.0, 0.0, 13.0, 'https://placehold.co/600x400?text=Ca+Hoi'),
('Đậu phụ (Luộc)', 76, 8.0, 1.9, 4.8, 'https://placehold.co/600x400?text=Dau+Phu'),

-- --- Nhóm Rau củ & Trái cây (Fiber/Vitamins) ---
('Súp lơ xanh (Bông cải)', 34, 2.8, 7.0, 0.4, 'https://placehold.co/600x400?text=Sup+Lo'),
('Chuối', 89, 1.1, 22.8, 0.3, 'https://placehold.co/600x400?text=Chuoi'),
('Táo (Cả vỏ)', 52, 0.3, 14.0, 0.2, 'https://placehold.co/600x400?text=Tao'),
('Dưa hấu', 30, 0.6, 7.6, 0.2, 'https://placehold.co/600x400?text=Dua+Hau'),
('Bơ (Quả bơ)', 160, 2.0, 8.5, 14.7, 'https://placehold.co/600x400?text=Qua+Bo'),

-- --- Món ăn Việt Nam phổ biến (Ước lượng trung bình trên 100g hỗn hợp) ---
('Phở Bò', 110, 6.0, 15.0, 3.0, 'https://placehold.co/600x400?text=Pho+Bo'),
('Bún Bò Huế', 120, 7.0, 14.0, 4.5, 'https://placehold.co/600x400?text=Bun+Bo'),
('Cơm Tấm Sườn', 180, 8.5, 25.0, 6.0, 'https://placehold.co/600x400?text=Com+Tam'),
('Gỏi Cuốn (Tôm thịt)', 150, 8.0, 20.0, 4.0, 'https://placehold.co/600x400?text=Goi+Cuon');