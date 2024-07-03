create table if not exists customers (
    id int auto_increment primary key,
    name varchar(100) not null,
    email varchar(100) not null,
    mobile_number varchar(20) not null,
    created_at date not null,
    created_by varchar(30) not null,
    updated_at date default null,
    updated_by varchar(30) default null
);

create table if not exists accounts (
    id int auto_increment primary key,
    customer_id int not null,
    type varchar(50) not null,
    branch_address varchar(200) not null,
    communication_status boolean,
    created_at date not null,
    created_by varchar(30) not null,
    updated_at date default null,
    updated_by varchar(30) default null
);
