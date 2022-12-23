create sequence discount_card_id_seq;
create sequence product_id_seq;

create table if not exists discount_card
(
    id bigint default nextval('discount_card_id_seq'::regclass) not null
        primary key,
    discount_number integer not null constraint unique_number unique,
    discount  integer not null

);

create table if not exists product
(
    id bigint default nextval('product_id_seq'::regclass) not null primary key,
    name varchar(255) not null constraint unique_name unique,
    price decimal not null,
    is_on_offer boolean not null default false
);
INSERT INTO discount_card(discount_number, discount)
VALUES
    (1500,15),
    (2552,25),
    (1953,13),
    (1002,5),
    (6832,92),
    (9903,65),
    (1717,11),
    (1818,55),
    (9567,35),
    (6666,66),
    (6786,33);
INSERT INTO product(name, price, is_on_offer)
VALUES
    ('Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops',109.95,true),
    ('Mens Casual Premium Slim Fit T-Shirts',22.3,false),
    ('Mens Cotton Jacket',55.99,true),
    ('White Gold Plated Princess',9.99,false),
    ('Pierced Owl Rose Gold Plated Stainless Steel Double',10.99,true),
    ('WD 2TB Elements Portable External Hard Drive - USB 3.0',64,false),
    ('DANVOUY Womens T Shirt Casual Cotton Short',12.99,true),
    ('Silicon Power 256GB SSD 3D NAND A55 SLC Cache Performance Boost SATA III 2.5',109.95,false),
    ('Mens Casual Slim Fit',15.99,true),
    ('John Hardy Women''s Legends Naga Gold & Silver Dragon Station Chain Bracelet',695,false),
    ('Solid Gold Petite Micropave',168,true),
    ('SanDisk SSD PLUS 1TB Internal SSD - SATA III 6 Gb/s',109,false),
    ('WD 4TB Gaming Drive Works with Playstation 4 Portable External Hard Drive',114,true),
    ('Acer SB220Q bi 21.5 inches Full HD (1920 x 1080) IPS Ultra-Thin',599,false),
    ('Samsung 49-Inch CHG90 144Hz Curved Gaming Monitor (LC49HG90DMNXZA) – Super Ultrawide Screen QLED',999.99,false),
    ('BIYLACLESEN Women''s 3-in-1 Snowboard Jacket Winter Coats',56.99,true),
    ('Lock and Love Women''s Removable Hooded Faux Leather Moto Biker Jacket',29.95,true),
    ('Rain Jacket Women Windbreaker Striped Climbing Raincoats',39.99,false),
    ('MBJ Women''s Solid Short Sleeve Boat Neck V',9.85,false);