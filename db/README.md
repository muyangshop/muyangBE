# MuYang DB (PostgreSQL) 셋업

DBeaver로 스키마를 만들고, Spring Boot를 `postgres` 프로파일로 연결하는 순서입니다.

## 1. PostgreSQL 준비 (docker compose — 권장)
프로젝트 루트(`muyang-server/`)에서:

```powershell
docker compose up -d      # DB 켜기 (첫 기동 시 schema.sql 자동 실행)
docker compose down       # 끄기 (데이터 유지)
docker compose down -v    # 데이터까지 완전 초기화
```

- 데이터는 `muyang-pgdata` 볼륨에 영구 저장돼, 컨테이너를 지워도 유지됩니다.
- 접속 정보를 바꾸려면 같은 폴더에 `.env`로 `DB_PORT`, `DB_PASSWORD` 등을 지정하세요.

> 직접 띄우려면: `docker run --name muyang-pg -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=muyang -p 5432:5432 -d postgres:16`

## 2. DBeaver 연결
- 새 연결 → **PostgreSQL**
- Host `localhost` · Port `5432` · Database `muyang` · User `postgres` · Password `postgres`
- (드라이버 다운로드 메시지가 뜨면 그대로 다운로드)

## 3. 스키마 생성
DBeaver에서 `muyang` 연결을 열고, SQL 에디터에 [`schema.sql`](schema.sql) 내용을 붙여넣고 **전체 실행**(Alt+X).
→ 좌측 트리에서 테이블 22개가 생성된 걸 확인하고, ER 다이어그램(데이터베이스 우클릭 → View Diagram)으로 관계도 볼 수 있어요.

## 4. 앱을 Postgres로 실행
```powershell
.\gradlew.bat bootRun --args="--spring.profiles.active=postgres"
```
- `ddl-auto=validate` 라서 엔티티와 스키마가 맞지 않으면 시작 시 에러로 알려줍니다.
- 빈 스키마면 `DataSeeder`가 데모 데이터(상품·데모계정·펫·쿠폰)를 한 번 채웁니다.
- 접속 정보를 바꾸려면 환경변수 `DB_URL` / `DB_USER` / `DB_PASSWORD` 를 주세요.

## 참고
- 기본(프로파일 미지정) 실행은 기존처럼 **인메모리 H2** 입니다 — 빠른 개발용.
- 운영 전환 시: DB 비밀번호·JWT 시크릿·PG 키를 모두 환경변수로 주입하세요.
