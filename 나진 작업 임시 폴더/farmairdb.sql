-- 기존 db 목록 조회
show databases;

-- db 생성
CREATE DATABASE farmairdb;
USE farmairdb;

-- db 생성 확인
show databases;

-- db 사용 연결
USE farmairdb;

-- 테이블 생성
CREATE TABLE env(	
	id INT NOT NULL AUTO_INCREMENT,
	mtime DATETIME NOT NULL,
	temp FLOAT,
	hum INT,
	wtemp INT,
	light INT,
	PRIMARY KEY(id)
);

-- 테스트 레코드 삽입
INSERT INTO env (id,mtime,temp,hum,wtemp,light)
VALUES(1,NOW(),36.5,50,36.0,100); 

-- 레코드 삽입 확인(조회)
SELECT * FROM env; 
