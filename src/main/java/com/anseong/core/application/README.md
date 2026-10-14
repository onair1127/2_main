서비스 계층(Service Layer) 입니다.

각 Layer는 Facade 패턴을 적용하여 주로 Repository 작업과 나누어 트랜잭션 범위를 단축시켜 동시성 향상과 리소스 고갈을 최대한 방지하였습니다.
