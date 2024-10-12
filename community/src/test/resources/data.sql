CREATE TABLE IF NOT EXISTS account (
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(60) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(16) NOT NULL,
    university VARCHAR(20) NOT NULL,
    student_id VARCHAR(20) NOT NULL,
    status VARCHAR(255) NOT NULL
);

INSERT INTO account (email, password, nickname, university, student_id, status) VALUES
('test@example.com', 'encodedPassword123!', 'TestNickname', 'University','1111111', 'REGISTERED');