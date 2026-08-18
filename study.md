# 📓 Study Log — Game Shop

게임샵(TOY) 프로젝트를 진행하며 겪은 문제와 도입 과정을, 실제 개발 순서에 따라 정리한다.

## 목차

| # | 단계 | 핵심 커밋 |
|---|---|---|
| 1 | 📋 기본 요구사항 설계 | — |
| 2 | 🧱 기본 엔티티 설계 (4개) | `13be72b` `8ccb728` |
| 3 | 🔧 DB 구조 문제 발견 & 개선 (6개로 확장) | `30b7ab2` |
| 4 | ⚙️ 서비스/컨트롤러 구현 | `41936d2` `8b859cb` `65fc364` |
| 5 | ✅ 레포/서비스 테스트 구현 | `838c2ae` |
| 6 | 💰 Purchase 흐름 추가 (1차 완성) | `c80d9fd` `6ff0805` |
| 7 | 📦 DTO 도입 + 예외 처리/검증 강화 | `0e8b5c0` `89d6cba` `97dc610` |
| 8 | ⚡ N+1 문제와 fetch join (다대일) | `565bcba` |
| 9 | 🔗 컬렉션 join용 DTO 도입 (일대다) | `b6e90c4` |
| 10 | ✨ 기능 확장: maxStack, position | `1e33ecf` `741885b` |
| 11 | 🪞 DTO 평탄화 작업 | `0765f1d` `dfbb12c` `4ee61a9` `7d3a4e4` `0fc37f8` |
| 12 | 🌱 시드 데이터 자동 삽입 | `db038a2` |
| 13 | 🖥️ 데모 웹 추가 *(AI 결과물)* | `1676725` |

---

## 1. 📋 기본 요구사항 설계

- 목표: "플레이어가 상점에서 아이템을 사고, 인벤토리에 쌓고, 거래 기록이 남는" 기본적인 게임 상점 도메인을 구현하는 토이 프로젝트
- 초기 구상 단계라 커밋으로 남아있진 않지만, 이후 만들어진 엔티티(Player/Item/Inventory/Transaction)와 API 구조를 보면 "누가(Player) 무엇을(Item) 얼마나(quantity) 가지고 있고, 언제 거래했는지(Transaction)"를 기록하는 게 핵심 요구사항이었음

---

## 2. 🧱 기본 엔티티 설계 (기존 4개)

- `Item`/`Player`/`Inventory`/`Transaction` 4개 엔티티 정의 (`13be72b`)
  - 필수 필드에 `@NotNull`, 음수 방지에 `@Min`, FK는 `nullable = false`
  - `Player.nickname`엔 `unique` 제약을 처음부터 걸어둠 (나중에 응답 DTO 평탄화에서 "이름만으로 식별 가능"의 근거가 됨)
- Spring Data JPA 레포지토리 4종 추가 (`findByName`, `findByNickname`, `findByPlayerAndItem` 등 조회 메서드) (`8ccb728`)

---

## 3. 🔧 기존 DB 구조의 문제점 발견 & 구조 개선 (엔티티 6개로 확장)

- **구조 개선** (`30b7ab2`)
  - **문제**: `Item`에 `price`/`stock`이 같이 있어서, "아이템 정의"와 "이 상점에서 파는 가격/재고"라는 서로 다른 개념이 한 엔티티에 섞여 있었음 (상점이 여러 개면 아이템마다 가격이 하나로 고정되는 구조적 한계)
  - **해결**: `price`/`stock`을 `Shop`/`ShopStock`으로 분리 → 엔티티가 4개(Item/Player/Inventory/Transaction)에서 6개(+ Shop, ShopStock)로 확장
  - 덤으로 `Inventory`(현재의 `InventorySlot`)의 unique 제약을 제거해서, 한 아이템이 여러 슬롯에 나뉘어 담길 수 있게 구조를 열어둠 (나중에 슬롯 분할/병합 기능의 전제 조건)
  - `Transaction`에 `shop` 참조 추가 (어느 상점에서 거래했는지 기록)

---

## 4. ⚙️ 6개 엔티티에 대한 서비스와 컨트롤러 구현

- `Item`/`Player` CRUD API 구현 (Repository/Service/Controller/PatchRequest) (`41936d2`)
  - `Item.type`이 null이면 `NONE` 기본값으로 채우는 로직이 이 시점에 이미 도입됨 (이후 `maxStack` 기본값 처리도 같은 패턴을 따라감)
- `InventoryService` 구현: 여러 슬롯 분할 저장, 오버플로우 자동 생성, 슬롯 병합/분할, 칸 수 제한 (`8b859cb`)
- `Shop`/`ShopStock` CRUD + 재고 증감/patch/삭제, 모든 재고 조작에 `shopId` 기반 소유권 검증 적용 (`65fc364`)

---

## 5. ✅ 각 레포와 서비스의 테스트 구현

- `Item`/`Player` Repository/Service 테스트 작성 (정상/미존재/patch 케이스) (`838c2ae`)
- `8b859cb`/`65fc364`에도 서비스 구현과 함께 테스트가 같이 붙어서 진행됨
- 이 시점부터 "정상 케이스 + 미존재 id + 잘못된 입력(예외)" 3종 세트로 테스트를 쓰는 패턴이 자리잡음 — 이후 세션(구매, 위치 이동 등)에서도 계속 이 틀을 따라감

---

## 6. 💰 Purchase 서비스 추가와 컨트롤러 추가 (초기 기초 구현 완료 시점)

- 구매(Purchase) 흐름 구현 (`c80d9fd`)
  - `PurchaseService.purchase()`가 골드 차감 · 재고 차감 · 인벤토리 증가 · 거래 기록을 **하나의 트랜잭션**으로 묶음 (중간에 하나라도 실패하면 전부 롤백)
  - 조회 전용인 `TransactionService`/`Controller` 추가
- `Purchase`/`Transaction` 테스트, 수량/재고/골드 부족 등 예외 케이스까지 커버 (`6ff0805`)
- 여기까지가 "기본 기능은 다 있다"고 볼 수 있는 1차 완성 시점

---

## 7. 📦 가독성 강화 및 코드 수정 방지를 위한 DTO의 개념 이해와 도입

- **DTO 계층 전면 도입** (`0e8b5c0`)
  - **문제**: 그동안 컨트롤러가 엔티티(`Item`, `Player` 등)를 응답으로 그대로 반환하고 있었음 → 엔티티 필드를 바꾸면 API 응답도 같이 바뀌어버리는 결합, 그리고 응답에 안 보여줘도 될 내부 필드까지 노출되는 문제
  - **해결**: 모든 컨트롤러의 엔티티 직접 노출을 없애고 Request(Create/Patch)/Response DTO로 분리, `dto` 패키지를 도메인별 서브패키지로 재구성
- **전역 예외 처리 및 요청/엔티티 검증 강화** (DTO 도입 바로 다음 단계로 자연스럽게 이어짐) (`89d6cba`)
  - **문제**: 컨트롤러/서비스가 던지는 예외(`NoSuchElementException` 등)가 그대로 위로 새서 클라이언트에게 항상 스프링 기본 500 에러가 나갔고, 요청 DTO엔 검증 어노테이션이 하나도 없어서 잘못된 값이 그냥 통과됐음
  - **해결**: `@RestControllerAdvice` + `@ExceptionHandler`로 `GlobalExceptionHandler` 작성해 예외 타입별 상태 코드 매핑, 엔티티에 있던 검증을 요청 DTO에도 미러링, `PatchRequest`는 `@NotNull`류는 빼고 `@Size`/`@Pattern`/`@Min`(값 있을 때만 검사)만 적용해서 "필드 생략 = 안 건드림"이라는 Patch 의미를 지킴
- `ItemCreateRequest.type`에 잘못 붙어있던 `@NotNull` 제거 (컨트롤러에서 이미 null이면 `NONE`으로 채워주는데, DTO 검증이 그 전에 막아버리는 모순을 뒤늦게 발견하고 수정) (`97dc610`)

---

## 8. ⚡ 최적화를 위한 fetch join 이해 및 도입

- **N+1 방지를 위한 fetch join 적용** (`565bcba`)
  - **문제**: `InventorySlot`/`ShopStock`/`Transaction`이 참조하는 `@ManyToOne(LAZY)` 연관관계들이, 리스트 조회 후 응답 DTO를 만들 때(`item.getName()` 등)마다 슬롯 개수만큼 추가 쿼리가 발생 (N+1). 특히 `TransactionRepository.findAll()`은 필터도 없고 계속 쌓이기만 하는 테이블이라 가장 위험했음
  - **해결**: Repository 커스텀 메서드에 `@Query(...JOIN FETCH...)`를 붙여 LAZY 연관관계를 한 번의 SQL JOIN으로 로딩. 기본 제공 메서드(`findById`, `findAll`)는 `@Query`를 못 붙이므로 `findAllWithDetails()` 같은 커스텀 메서드로 대체
  - **적용 기준**: "Repository의 어느 메서드냐"가 아니라 **"이 결과로 코드가 실제 `.getXxx()`(진짜 데이터)를 호출하는가"** — `.getPlayer().getId()`처럼 id만 쓰면 애초에 쿼리가 안 나가고(FK 컬럼이 이미 로딩돼있음), `.getName()`처럼 진짜 데이터를 요구할 때만 추가 쿼리가 발생한다는 걸 확인

---

## 9. 🔗 컬렉션 join을 이해하기 위한 DTO 추가 도입

- **Shop 상세 조회에 재고/아이템 목록 포함 (일대다 fetch join)** (`b6e90c4`)
  - 8번의 `JOIN FETCH`가 다대일(`ManyToOne`)이었다면, 이번엔 "상점 하나 : 재고 여러 개"라는 **일대다** 관계를 한 번에 가져오는 케이스
  - `ShopDetailResponse` + `ShopStockSummary` 도입: 상점 상세 조회 시 그 상점의 재고 목록(아이템 id/name/type/description/maxStack/stock/price)까지 한 응답에 평탄하게 포함
  - `ShopStockSummary`는 상점이 이미 정해진 컨텍스트(상세 조회 안에 중첩)라서 `shop` 참조를 아예 안 넣고 item 필드만 풀어서 담음 — 이 패턴이 나중에(11번) 오늘 세션의 DTO 평탄화 작업에서 그대로 참고됨

---

## 10. ✨ 추가적 기능 기획 및 도입 과정 (maxStack, position)

- **Item별 maxStack(최대 스택 수) 필드 도입** (`1e33ecf`)
  - **문제**: `InventoryService`에 `MAX_STACK = 99`가 전역 고정값으로 하드코딩돼서, 물약이든 무기든 전부 99개까지 무조건 쌓임
  - **해결**: `maxStack`을 슬롯(인스턴스)이 아니라 `Item`(정의/템플릿)에 추가. `@Builder.Default`로 기본값 99 유지하되, `.maxStack(null)`처럼 명시적으로 null을 넣으면 기본값이 무시되고 null이 그대로 들어가는 Lombok 함정이 있어서 `ItemService`에서 별도 null 체크로 방어
- **인벤토리 슬롯 위치(row/col) 도입 및 이동 기능** *(오늘 세션)* (`741885b`)
  - `InventorySlot`에 row/col 필드 + `(player,row,col)` unique 제약 추가, `BitSet`으로 6x10 그리드의 점유 상태를 표현해 빈 칸 자동 배치
  - 테스트를 짜다가 발견한 버그: 슬롯을 "자기 자신의 현재 위치"로 옮기려 하면 점유 목록에 자기 자신도 포함돼 있어서 예외가 터짐 → 자기 위치 이동은 `IllegalArgumentException`으로 먼저 걸러내고, 진짜 이동 검증 시엔 자기 칸을 점유 목록에서 제외하도록 수정

---

## 11. 🪞 기존 DTO의 가독성 문제 및 이를 위한 평탄화 작업 과정 *(오늘 세션)*

- **계기**: Postman에서 `maxStock`(오타)을 보내다가, 응답 DTO들이 player/item/shop 같은 참조 엔티티를 전체 객체로 중첩시켜 넣고 있다는 걸 다시 보게 됨
- 인벤토리 전체 조회 응답을 player 기준으로 그룹화 (`SlotOrderByPlayerResponse`): URL에 이미 있는 playerId를 슬롯마다 또 중첩시키지 않고 최상단으로 한 번만 뺌 (`0765f1d`)
- Transaction/ShopStock 응답 평탄화: player/item/shop 전체 객체 대신 id+name만 남김 (`dfbb12c`)
- `Player` 생성 시 `gold` 미입력 허용 (`@NotNull` 제거, 서비스에서 이미 null이면 0으로 채워주고 있어서 DTO 검증이 불필요하게 막고 있었음) (`4ee61a9`)
- 평탄화 결과 완전히 같은 모양이 된 `InventorySlotResponse`/`InventorySlotEntryResponse`를 하나로 통합 (`7d3a4e4`)
- 이 작업들과 함께 빠져있던 테스트(`InventoryService.moveSlot`, `PlayerService.spendGold`)도 보강 (`0fc37f8`)
- **핵심 판단 기준**: id를 무조건 다 빼거나 다 넣는 게 아니라, **참조 대상의 이름이 나중에 바뀔 수 있는가**를 기준으로 결정 — `Player.nickname`/`Item.name`/`Shop.name`이 지금은 `unique`라 이름만으로 충분하지만, 나중에 "닉네임 변경" 같은 기능이 생기면 id 없인 그 시점 데이터를 재조회할 방법이 없어지므로 id는 유지하고 표시용 이름을 같이 노출하는 절충으로 감

---

## 12. 🌱 기존의 지속적 데이터 삽입의 불편성과 이를 개선하기 위한 시드 데이터 도입 과정 *(오늘 세션)*

- **문제**: Postman으로 기능 하나 확인할 때마다 Player/Item/Shop/ShopStock을 수동으로 새로 만들어야 해서 반복 작업이 계속됨
- **앱 시작 시 기본 시드 데이터 자동 삽입** (`db038a2`)
  - `CommandLineRunner`를 구현한 `DataInitializer`가 앱 시작 시 자동 실행, `player count() > 0`이면 스킵해서 중복 삽입 방지
  - 지금 DB가 인메모리 H2라 재시작마다 통째로 비워지므로 이 가드만으로 충분히 안전
  - 다만 이런 시더가 나중에 실제 운영 DB에도 그대로 실행되면 위험하므로, `@Profile("!prod")`로 감싸서 지금은 그대로 동작하면서 나중에 `prod` 프로필을 도입하는 순간 자동으로 꺼지도록 방어

---

## 13. 🖥️ (클라, AI only) 기존 API의 정상 작동 확인 및 편의성을 확인하기 위한 데모 웹 추가

> 🤖 이 항목은 직접 코드를 짠 게 아니라 AI(Claude)에게 만들어달라고 요청해서 나온 결과물 — 학습용이라기보단 "API가 실제로 잘 동작하는지 눈으로 확인하는 도구"로 취급.

- API 수동 확인용 데모 웹 페이지 추가, `feature/demo-web` 브랜치에서 진행 (API 로직과 무관한 실험적 작업이라 `main`은 깨끗하게 유지) (`1676725`)
  - 정적 HTML+JS 단일 페이지를 `src/main/resources/static/`에 둬서 Spring Boot가 같은 오리진으로 서빙 (CORS 설정 불필요, 빌드 도구도 불필요)
  - Player 선택/생성, Shop 아이템 카드로 구매, 6x10 인벤토리 그리드 표시
  - 인벤토리 슬롯을 드래그해서 이동/합치기(merge), `Shift`+클릭으로 분할(split)
  - 실제 서버에 붙여서 네트워크 요청 로그로 검증: `split`/`merge`/`position` 이동/(다른 아이템끼리 merge 시도 시) 409 에러까지 의도한 API 그대로 호출되는 것 확인
