# hiring-challenge-backend (backend 과제 브랜치)

이 브랜치는 **백엔드 지원자**용 과제입니다. **`backend/`** 에서 아래 **API 계약**을 만족하도록 구현합니다.  
**`frontend/`** 는 출제 측이 제공하는 **동작하는 참고 UI**(동일 스펙에 맞춘 클라이언트)이며, **과제 범위에서 `frontend/` 코드를 수정할 필요는 없습니다.** (로컬에서 API 연동·수동 검증용으로만 사용합니다.)

이 저장소 **`backend/`** 의 보일러플레이트는 **Kotlin + Spring Boot**(Gradle) 기준으로 작성되어 있습니다.  
**Java** 로 구현하고 싶다면 언어·빌드·런타임에 맞는 **보일러플레이팅, 스캐폴딩, 도커라이징** 등은 지원자가 **직접** 맞추어 구성해야 합니다. 과제의 본질은 아래 **API 계약** 준수입니다.

스캐폴딩에는 **PostgreSQL** 도 포함되어 있습니다. (예: 저장소 루트의 **`docker-compose.yml`** 로 기동하는 DB, `backend` 의 JDBC·PostgreSQL 드라이버 의존성, `docker` Spring 프로파일을 통한 연결 설정 등.) **가능하면 이 PostgreSQL 구성을 있는 그대로 활용하는 것을 권장**합니다.

---

## 백엔드 API 스펙 (계약 — 구현 대상)

모든 REST 경로는 **`/api/v1`** 접두사를 둡니다.  
참고용 `frontend/` 의 Vite는 `vite.config.ts` 의 프록시로 동일 경로를 백엔드(기본 `http://127.0.0.1:8080`)에 넘깁니다.

### 공통: 페이지네이션 (Spring Data `Page`)

`GET ...?page=&size=` 형태의 목록 API는 JSON으로 **Spring `Page`** 형태를 반환해야 합니다. 클라이언트는 최소 다음 필드를 사용합니다.

| 필드 | 의미 |
|------|------|
| `content` | 현재 페이지의 항목 배열 |
| `totalElements` | 전체 건수 |
| `totalPages` | 전체 페이지 수 |
| `number` | 현재 페이지 번호(0 기반) |
| `size` | 페이지 크기 |
| `first` | 첫 페이지 여부 |
| `last` | 마지막 페이지 여부 |

### 공통: 오류 응답

4xx 등 오류 시 본문이 JSON이면 **`{ "code": string, "message": string }`** 형태로 내려주는 것을 권장합니다.  
(참고 UI는 `message` 표시·`code` 분기에 활용할 수 있습니다.)

---

### 품목 (`/api/v1/products`)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| `GET` | `/api/v1/products?page={n}&size={m}` | 품목 페이지 목록. 응답 `content` 항목은 **Product** 형태. |
| `GET` | `/api/v1/products/{productId}` | 품목 단건. |
| `POST` | `/api/v1/products` | 품목 등록. 본문: `productName`, `unit`, `unitPrice`, `stockQty`. 성공 시 **201** + Product. |
| `PUT` | `/api/v1/products/{id}` | 품목 수정. 본문은 등록과 동일 필드. |
| `DELETE` | `/api/v1/products/{id}` | 품목 삭제. 성공 시 **204**, 본문 없음. |

`{id}` / `{productId}` 는 UUID 문자열입니다.

---

### 주문 (`/api/v1/orders`)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| `GET` | `/api/v1/orders?page={n}&size={m}` | 주문 목록(요약). `content` 항목은 **OrderListItem** 형태. |
| `GET` | `/api/v1/orders/{orderId}` | 주문 상세(줄 포함). 응답은 **OrderDetail** 형태. |
| `POST` | `/api/v1/orders` | 주문 생성. 본문: `ordererName`, `address`, `phoneNo`, `orderLines[]` (`productId`, `qty`). 성공 시 **201** + OrderDetail. |
| `PUT` | `/api/v1/orders/{id}` | 주문 수정. 본문은 생성과 동일. |
| `DELETE` | `/api/v1/orders/{id}` | 주문 삭제. 성공 시 **204**. |

주문 생성·수정 시 각 `orderLines` 항목의 **`unitPrice`는 주문 시점 품목 단가 스냅샷**으로 저장되어, 이후 카탈로그 가격이 바뀌어도 과거 주문 금액이 흔들리지 않아야 합니다.

---

### 스캐폴드 헬스 (유지)

- `GET /api/v1/health`
- `GET /api/v1/challenge-summary`

---

## 과제: 구현해야 할 내용 (백엔드)

위 **API 스펙**과 아래 **도메인 타입**을 기준으로, **`backend/`** 에서 다음을 구현합니다.

1. **품목**: CRUD, UUID 식별자, 목록은 Spring `Page` 형태.
2. **주문**: CRUD, 목록은 요약(**OrderListItem**: 줄 개수·총액 등), 상세는 **OrderDetail** 및 줄 단가 스냅샷.
3. **공통**: 요청 검증, 적절한 HTTP 상태 코드, 위 오류 응답 형식(가능한 범위), 영속화 계층(JPA 등) 및 스키마 설계.
4. **비고**: 스캐폴드에 포함된 설정·의존성을 바탕으로 확장하면 됩니다. DB는 스캐폴드 기본값을 유지하거나, 과제 안내에 맞게 교체해도 됩니다.

구현이 완료되면 **`./gradlew bootRun`** 으로 서버를 띄운 뒤, 참고 **`frontend/`** 로 동작을 확인할 수 있습니다.

---

## 도메인 타입 (참고)

API JSON 필드명은 **camelCase** (`productId`, `orderLines`, `lineItemCount` 등)를 가정합니다.

### 품목 카탈로그

```ts
type Product = {
  productId: string;
  productName: string;
  unit: string;
  /** 저장·정산 시에는 정수(예: 원 단위) 권장. UI에서만 number로 다루어도 됨 */
  unitPrice: number;
  stockQty: number;
};
```

### 주문 목록 한 줄

```ts
type OrderListItem = {
  orderId: string;
  ordererName: string;
  address: string;
  phoneNo: string;
  lineItemCount: number;
  totalAmount: number;
};
```

### 주문 상세

```ts
type OrderLine = {
  productId: string;
  qty: number;
  /** 주문 시점 단가 스냅샷 */
  unitPrice: number;
};

type OrderDetail = {
  orderId: string;
  ordererName: string;
  address: string;
  phoneNo: string;
  orderLines: OrderLine[];
};
```

### 관계 (FK–PK 관점)

- **`OrderLine` → `Product`**: `orderLines[].productId`는 `product.productId`를 참조하는 **다대일(N:1)** 관계입니다.
- **`Order` → `OrderLine`**: 한 주문에 여러 줄이 붙는 **일대다(1:N)** 입니다.
- **`Order` ↔ `Product`**: 직접 N:1이 아니라, **`OrderLine`을 통한 다대다(M:N)** 에 해당합니다.

---

## 실행 방법

### 루트에서 프론트·백 동시 실행

최초 1회 `cd frontend && npm install` 후, 저장소 루트에서 다음을 실행합니다.

```bash
./dev.sh
```

실행 권한이 없으면 `bash dev.sh` 로 실행합니다. Vite 기본 포트 **5173**, 백엔드 **8080** 이 동시에 올라가며, 종료는 터미널에서 **Ctrl+C** 입니다. (`dev.sh` 는 JDK/Gradle 과 `frontend/node_modules` 존재 여부를 먼저 확인합니다.)

### Backend (과제 구현 대상)

```bash
cd backend
./gradlew bootRun
```

기본 포트는 Spring Boot 기본값(`8080`)입니다. 위 스펙의 엔드포인트가 동작해야 참고 UI와 연동됩니다.

### Frontend (참고 UI — 과제 범위 밖)

```bash
cd frontend
npm install
npm run dev
```

기본 포트는 Vite 기본값(`5173`)입니다. 백엔드를 먼저 띄운 뒤 브라우저에서 API 호출이 프록시를 통해 동작하는지 확인합니다.
