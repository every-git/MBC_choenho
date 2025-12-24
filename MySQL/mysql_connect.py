#!/usr/bin/env python3
"""
MySQL 서버 연결 도구
설정된 연결 정보를 사용하여 MySQL 서버에 연결합니다.
"""

import mysql.connector
from mysql.connector import Error
import sys
import json

# 연결 설정
DB_CONFIGS = {
    "local_3306": {
        "host": "localhost",
        "port": 3306,
        "user": "root",
        "password": "",  # 실제 비밀번호 입력 필요
        "database": "dcproject"
    },
    "jdbctest": {
        "host": "127.0.0.1",
        "port": 3306,
        "user": "jdbctest",
        "password": "",  # 실제 비밀번호 입력 필요
        "database": "dcproject",
        "ssl_disabled": False
    },
    "nas_db": {
        "host": "192.168.0.7",
        "port": 3307,
        "user": "jdbctest",
        "password": "",  # 실제 비밀번호 입력 필요
        "database": "dcproject",
        "ssl_disabled": False
    },
    "springdb": {
        "host": "127.0.0.1",
        "port": 3306,
        "user": "root",
        "password": "",  # 실제 비밀번호 입력 필요
        "database": "springdb",
        "ssl_disabled": False
    }
}


def connect_to_mysql(config_name: str):
    """MySQL 서버에 연결합니다."""
    if config_name not in DB_CONFIGS:
        print(f"오류: '{config_name}' 연결을 찾을 수 없습니다.")
        print_available_connections()
        return None
    
    config = DB_CONFIGS[config_name].copy()
    
    # 비밀번호가 없으면 입력받기
    if not config.get("password"):
        import getpass
        config["password"] = getpass.getpass(f"{config_name} 비밀번호 입력: ")
    
    try:
        connection = mysql.connector.connect(**config)
        if connection.is_connected():
            db_info = connection.get_server_info()
            print(f"✓ MySQL 서버에 연결되었습니다! (버전: {db_info})")
            return connection
    except Error as e:
        print(f"✗ 연결 오류: {e}")
        return None


def print_available_connections():
    """사용 가능한 연결 목록을 출력합니다."""
    print("\n사용 가능한 연결:")
    for i, (name, config) in enumerate(DB_CONFIGS.items(), 1):
        print(f"  {i}. {name}")
        print(f"     Host: {config['host']}:{config['port']}")
        print(f"     User: {config['user']}")
        print(f"     Database: {config['database']}")
        print()


def test_connection(config_name: str):
    """연결을 테스트하고 간단한 쿼리를 실행합니다."""
    connection = connect_to_mysql(config_name)
    if connection:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT VERSION();")
            version = cursor.fetchone()
            print(f"MySQL 버전: {version[0]}")
            
            cursor.execute("SELECT DATABASE();")
            database = cursor.fetchone()
            print(f"현재 데이터베이스: {database[0]}")
            
        except Error as e:
            print(f"쿼리 실행 오류: {e}")
        finally:
            if connection.is_connected():
                cursor.close()
                connection.close()
                print("MySQL 연결이 종료되었습니다.")


if __name__ == "__main__":
    if len(sys.argv) > 1:
        config_name = sys.argv[1]
        test_connection(config_name)
    else:
        print_available_connections()
        print("사용법: python mysql_connect.py [connection_name]")
        print("예: python mysql_connect.py local_3306")







