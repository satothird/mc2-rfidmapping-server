/* Core model implementation */

create table tag_read_logs(
	log_id int not null auto_increment,
	tag_id varchar(100) not null,
	rssi double not null,
	reader_x double not null,
	reader_y double not null,
	reader_z double not null,
	reader_alpha double not null,
	reader_delta double not null,
	read_date datetime not null,
	reading_session varchar(100) not null,
	primary key(log_id)
);

create table current_tag_location(
	tag_id varchar(100) not null unique,
	x double,
	y double,
	z double,
	primary key(tag_id)
);

create table cross_points(
	tag_id varchar(100) not null,
	x double not null,
	y double not null,
	z double not null,
	weight double not null
);

create table reference_tags(
	tag_id varchar(100) not null unique,
	x double not null,
	y double not null,
	z double not null,
	range_to_apply double not null,
	primary key(tag_id)
);

/* webapp */

create table reader_location(
	reader_id varchar(100) not null unique,
	x double not null,
	y double not null,
	z double not null,
	alpha double not null,
	delta double not null,
	primary key(reader_id)
);
