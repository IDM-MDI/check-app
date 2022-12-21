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