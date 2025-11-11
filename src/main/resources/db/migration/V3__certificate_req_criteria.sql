CREATE TABLE certificate_request_criteria
(
    id                        uuid   DEFAULT gen_random_uuid() PRIMARY KEY,
    certificate_request_id    uuid   NOT NULL,
    criteria_id               uuid   NOT NULL,
    remarks                   TEXT NOT NULL,
    user_percentage           INT,
    is_met                    BOOLEAN     DEFAULT FALSE,
    created_at                timestamptz DEFAULT now() NOT NULL,
    updated_at                timestamptz DEFAULT now() NOT NULL,

    CONSTRAINT fk_certificate_request_criteria_request_id
        FOREIGN KEY (certificate_request_id) REFERENCES certificate_request (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_certificate_request_criteria_criteria_id
        FOREIGN KEY (criteria_id) REFERENCES criteria (id)
            ON DELETE CASCADE
);


create table folder
(
    id uuid default gen_random_uuid() not null constraint folder_pk primary key,
    path text not null,
    reference_type varchar(254)  not null,
    reference_id uuid  not null,
    is_deleted boolean DEFAULT FALSE,
    is_active BOOLEAN DEFAULT true,
    created_at   timestamptz default now(),
    updated_at   timestamptz default now()
);

create table attachment
(
    id             uuid default gen_random_uuid() not null constraint attachment_pk primary key,
    size           BIGINT, --BYTES
    content_type   TEXT,
    reference_id   uuid                           not null,
    reference_type varchar(50)                    not null,
    uploaded_by    uuid                           not null,
    folder_id      uuid                           not null  constraint attachment_folder_id_fk references folder,
    file_name      TEXT                           not null,
    file_url       TEXT                           not null,
    uploaded_at    timestamptz                    not null,
    is_deleted boolean DEFAULT FALSE,
    is_active BOOLEAN DEFAULT true,
    created_at   timestamptz default now(),
    updated_at   timestamptz default now()
);
