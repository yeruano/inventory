-- Geenrar Datos Aleatorios
INSERT INTO supplier (nit, company_name, supplier_name, description, cell_phone_number, address, email, web_page)
VALUES
    ('1234567890', 'Company A', 'Supplier A', 'Description A', '123-456-7890', 'Address A', 'supplierA@example.com', 'http://companyA.com'),
    ('0987654321', 'Company B', 'Supplier B', 'Description B', '234-567-8901', 'Address B', 'supplierB@example.com', 'http://companyB.com'),
    ('1122334455', 'Company C', 'Supplier C', 'Description C', '345-678-9012', 'Address C', 'supplierC@example.com', 'http://companyC.com'),
    ('2233445566', 'Company D', 'Supplier D', 'Description D', '456-789-0123', 'Address D', 'supplierD@example.com', 'http://companyD.com'),
    ('3344556677', 'Company E', 'Supplier E', 'Description E', '567-890-1234', 'Address E', 'supplierE@example.com', 'http://companyE.com'),
    ('4455667788', 'Company F', 'Supplier F', 'Description F', '678-901-2345', 'Address F', 'supplierF@example.com', 'http://companyF.com'),
    ('5566778899', 'Company G', 'Supplier G', 'Description G', '789-012-3456', 'Address G', 'supplierG@example.com', 'http://companyG.com'),
    ('6677889900', 'Company H', 'Supplier H', 'Description H', '890-123-4567', 'Address H', 'supplierH@example.com', 'http://companyH.com'),
    ('7788990011', 'Company I', 'Supplier I', 'Description I', '901-234-5678', 'Address I', 'supplierI@example.com', 'http://companyI.com'),
    ('8899001122', 'Company J', 'Supplier J', 'Description J', '012-345-6789', 'Address J', 'supplierJ@example.com', 'http://companyJ.com');

INSERT INTO product (name, description, sale_price, purchase_price, amount, minimum_stock, maximum_stock, created_at, updated_at, brand_id)
SELECT
    'product' || i AS name,
    'description for product' || i AS description,
    (random() * 100 + 1)::decimal AS sale_price,
    (random() * 50 + 1)::decimal AS purchase_price,
    (random() * 100 + 1)::smallint AS amount,
    (random() * 10 + 1)::smallint AS minimum_stock,
    (random() * 200 + 1)::smallint AS maximum_stock,
    CURRENT_TIMESTAMP AS created_at,
    CURRENT_TIMESTAMP AS updated_at,
    (random() * 10 + 1)::int AS brand_id
FROM generate_series(1, 1000) AS s(i);

WITH temp_product_supplier AS (
    SELECT DISTINCT
        (random() * 999 + 1)::int AS product_id,
        (random() * 9 + 1)::int AS supplier_id
    FROM generate_series(1, 10000) AS s(i)
)
INSERT INTO product_supplier (product_id, supplier_id)
SELECT product_id, supplier_id
FROM temp_product_supplier
LIMIT 2000;