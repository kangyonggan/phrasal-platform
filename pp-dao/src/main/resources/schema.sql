DROP DATABASE IF EXISTS pp;

CREATE DATABASE pp
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE pp;

-- ----------------------------
--  Table structure for tb_phrasal
-- ----------------------------
DROP TABLE
IF EXISTS tb_phrasal;

CREATE TABLE tb_phrasal
(
  id           BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
  COMMENT '主键, 自增',
  name         VARCHAR(13)                           NOT NULL
  COMMENT '成语名称',
  word_len     INT(11)                               NOT NULL
  COMMENT '字数',
  type         VARCHAR(20)                           NOT NULL                    DEFAULT ''
  COMMENT '类型',
  capital_word CHAR(1)                               NOT NULL
  COMMENT '首字母',
  spelling     VARCHAR(100)                          NOT NULL
  COMMENT '拼音',
  definition   VARCHAR(1024)                         NOT NULL                    DEFAULT ''
  COMMENT '释义',
  source       VARCHAR(256)                          NOT NULL                    DEFAULT ''
  COMMENT '出处',
  example      VARCHAR(1024)                         NOT NULL                    DEFAULT ''
  COMMENT '示例',
  status       TINYINT                               NOT NULL                    DEFAULT 0
  COMMENT '状态:{0:可用, 1:禁用}',
  created_time TIMESTAMP                             NOT NULL                    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  updated_time TIMESTAMP                             NOT NULL                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间'
)
  COMMENT '成语表';
CREATE INDEX ix_word_len
  ON tb_phrasal (word_len);
CREATE INDEX ix_type
  ON tb_phrasal (type);
CREATE INDEX ix_capital_word
  ON tb_phrasal (capital_word);