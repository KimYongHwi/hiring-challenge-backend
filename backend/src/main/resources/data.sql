INSERT INTO products (
    product_id,
    product_name,
    unit,
    unit_price,
    stock_qty,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    UUID '11111111-1111-1111-1111-111111111111',
    '콜라',
    '캔',
    1500,
    100,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM products
    WHERE product_id = UUID '11111111-1111-1111-1111-111111111111'
);

INSERT INTO products (
    product_id,
    product_name,
    unit,
    unit_price,
    stock_qty,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    UUID '22222222-2222-2222-2222-222222222222',
    '사이다',
    '캔',
    1500,
    100,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM products
    WHERE product_id = UUID '22222222-2222-2222-2222-222222222222'
);

INSERT INTO products (
    product_id,
    product_name,
    unit,
    unit_price,
    stock_qty,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    UUID '33333333-3333-3333-3333-333333333333',
    '생수',
    '병',
    900,
    200,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM products
    WHERE product_id = UUID '33333333-3333-3333-3333-333333333333'
);

INSERT INTO products (
    product_id,
    product_name,
    unit,
    unit_price,
    stock_qty,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    UUID '44444444-4444-4444-4444-444444444444',
    '오렌지주스',
    '팩',
    2500,
    50,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM products
    WHERE product_id = UUID '44444444-4444-4444-4444-444444444444'
);
