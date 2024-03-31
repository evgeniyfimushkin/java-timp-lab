CREATE TABLE public.employees (
    id numeric primary key,
    type varchar(32) not null,
    livingtime integer not null,
    panesize integer not null,
    x integer not null,
    y integer not null,
    birthTime timestamp without time zone
) TABLESPACE pg_default;