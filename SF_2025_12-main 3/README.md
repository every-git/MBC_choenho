# SF_2025_12

## MySQL Database & User 설정 및 table_board 테이블 생성

```sql
-- 📌 1) Database & User 설정
CREATE DATABASE springdb;

CREATE USER 'springdbuser'@'%' IDENTIFIED BY '1234';

GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'%';


-- 📌 2) table_board 테이블 생성
create table table_board(
    bno int auto_increment primary key,
    title varchar(500) not null,
    content varchar(2000) not null,
    writer varchar(50) not null,
    regdate timestamp default now(),
    updatedate timestamp default now(),
    delflag boolean default false
);

-- 📌 3) tbl_reply 테이블 생성
create table tbl_reply(
	rno int auto_increment primary key,
    replyText varchar(500) not null, -- 댓글 내용
    replyer varchar(50) not null,  -- 작성자
    replydate timestamp default now(),
    updatedate timestamp default now() on update now(),
    delflag boolean default false,
    bno int not  null  
);

-- 📌 4) 외래키 설정
alter table tbl_reply add constraint fk_reply_board foreign key(bno)
references tbl_board(bno);

-- 📌 5) 인텍스 설정
create index idx_reply_board on tbl_reply(bno desc, rno asc);

-- 📌 6) tbl_account 테이블 생성
create table tbl_account(
    uid varchar(50) primary key,
    upw varchar(200) not null,
    uname varchar(100) not null,
    email varchar(200) not null
);

-- 📌 7) tbl_account_roles 테이블 생성
create table tbl_account_roles(
    uid varchar(50) not null,
    role varchar(50) not null,
    primary key (uid, role),
    foreign key (uid) references tbl_account(uid) on delete cascade
);

