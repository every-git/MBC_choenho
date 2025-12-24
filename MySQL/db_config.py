"""
MySQL 연결 설정 파일
MySQL Workbench의 connections.xml에서 추출한 정보를 기반으로 작성되었습니다.
"""

DB_CONFIGS = {
    "local_3306": {
        "host": "localhost",
        "port": 3306,
        "user": "root",
        "password": "",
        "database": "dcproject",
        "server_version": "8.0.39"
    },
    "jdbctest": {
        "host": "127.0.0.1",
        "port": 3306,
        "user": "jdbctest",
        "password": "",
        "database": "dcproject",
        "server_version": "8.0.39",
        "use_ssl": True
    },
    "nas_db": {
        "host": "192.168.0.7",
        "port": 3307,
        "user": "jdbctest",
        "password": "",
        "database": "dcproject",
        "server_version": "8.0.44",
        "use_ssl": True
    },
    "springdb": {
        "host": "127.0.0.1",
        "port": 3306,
        "user": "root",
        "password": "",
        "database": "springdb",
        "server_version": "8.0.39",
        "use_ssl": True
    }
}

def get_connection_string(config_name: str) -> str:
    """연결 문자열을 반환합니다."""
    config = DB_CONFIGS.get(config_name)
    if not config:
        raise ValueError(f"Unknown config: {config_name}")
    
    password_part = f":{config['password']}" if config.get('password') else ""
    return f"mysql://{config['user']}{password_part}@{config['host']}:{config['port']}/{config['database']}"

# 사용 예시
if __name__ == "__main__":
    for name, config in DB_CONFIGS.items():
        print(f"\n{name}:")
        print(f"  Host: {config['host']}")
        print(f"  Port: {config['port']}")
        print(f"  User: {config['user']}")
        print(f"  Database: {config['database']}")







