DROP TABLE IF EXISTS public.employees;

CREATE TABLE public.employees (
    id numeric primary key,
    type varchar(32) not null,
    livingtime integer not null,
    panesize integer not null,
    x real not null,
    y real not null
) TABLESPACE pg_default;

-- ALTER TABLE IF EXISTS public.managers
--     OWNER to postgres;	