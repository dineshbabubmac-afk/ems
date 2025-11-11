CREATE TABLE certificate_status
(
    code         VARCHAR(10) PRIMARY KEY,
    name_english VARCHAR(100) UNIQUE NOT NULL,
    name_arabic  VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO certificate_status (code, name_english, name_arabic)
VALUES ('PENDING', 'Pending', 'قيد الانتظار'),
       ('APPROVED', 'Approved', 'موافق عليه'),
       ('REJECTED', 'Rejected', 'مرفوض'),
       ('PUBLISHED', 'Published', 'منشور');


CREATE SEQUENCE certificate_template_code_seq START 1 INCREMENT 1;

CREATE TABLE certificate_template
(
    id          uuid        default gen_random_uuid() not null
        constraint certificate_template_pk primary key,
    code        varchar(20)                           not null,
    name        varchar(254)                          not null,
    description TEXT                                  not null,
    path        TEXT                                  not null,
    is_deleted  boolean     DEFAULT FALSE,
    is_active   BOOLEAN     DEFAULT true,
    created_at  timestamptz default now()             not null,
    updated_at  timestamptz default now()             not null
);

CREATE TABLE certificate_category
(
    id           uuid        default gen_random_uuid() not null
        constraint certificate_category_pk primary key,
    name_english VARCHAR(100) UNIQUE                   NOT NULL,
    name_arabic  VARCHAR(100) UNIQUE                   NOT NULL,
    is_deleted   boolean     DEFAULT FALSE,
    is_active    BOOLEAN     DEFAULT FALSE,
    created_at   timestamptz default now(),
    updated_at   timestamptz default now()
);

CREATE SEQUENCE certificate_code_seq START 1 INCREMENT 1;

CREATE TABLE certificate
(
    id                      uuid                  default gen_random_uuid() not null
        constraint certificate_pk primary key,
    certificate_category_id uuid         NOT NULL,
    code                    varchar(20)  not null,
    name                    varchar(254) not null,
    description             TEXT         not null,
    eligibility_required    BOOLEAN      not null default false,
    approval_required       BOOLEAN      not null default false,
    criteria_required       BOOLEAN      not null default false,
    certificate_template_id uuid         not null,
    created_at              timestamptz           default now() not null,
    updated_at              timestamptz           default now() not null,
    is_deleted              boolean               DEFAULT FALSE,
    is_active               BOOLEAN               DEFAULT TRUE,
    CONSTRAINT fk_certificate_template_id
        FOREIGN KEY (certificate_template_id) REFERENCES certificate_template (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_certificate_category_id
        FOREIGN KEY (certificate_category_id) REFERENCES certificate_category (id)
            ON DELETE CASCADE
);

CREATE TABLE criteria
(
    id             uuid        default gen_random_uuid() not null
        constraint criteria_pk primary key,
    certificate_id uuid                                  not null,
    name           VARCHAR(100)                          NOT NULL,
    percentage     INT                                   NOT NULL,
    description    TEXT,
    is_deleted     boolean     DEFAULT FALSE,
    is_active      BOOLEAN     DEFAULT TRUE,
    created_at     timestamptz default now()             not null,
    updated_at     timestamptz default now()             not null,
    CONSTRAINT fk_criteria_certificate_id
        FOREIGN KEY (certificate_id) REFERENCES certificate (id)
            ON DELETE CASCADE
);

CREATE TABLE certificate_eligible_band
(
    id              uuid        default gen_random_uuid() not null
        constraint certificate_eligible_band_pk primary key,
    certificate_id  uuid                                  not null,
    band_level_code VARCHAR(100)                          NOT NULL,
    created_at      timestamptz default now()             not null,
    updated_at      timestamptz default now()             not null,
    CONSTRAINT fk_band_level_code
        FOREIGN KEY (band_level_code) REFERENCES band_level_lookup (code)
            ON DELETE CASCADE,
    CONSTRAINT fk_eligible_band_certificate_id
        FOREIGN KEY (certificate_id) REFERENCES certificate (id)
            ON DELETE CASCADE
);

CREATE TABLE member_role
(
    code         VARCHAR(100) PRIMARY KEY,
    name_english VARCHAR(100) UNIQUE NOT NULL,
    name_arabic  VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO member_role (code, name_english, name_arabic)
VALUES ('HOC', 'Head of Committee', 'رئيس اللجنة'),
       ('COMMITTEE_MEMBER', 'Committee Member', 'عضو اللجنة');

CREATE TABLE certificate_approval_member
(
    id               uuid        default gen_random_uuid() not null
        constraint certificate_approval_member_pk primary key,
    member_id        uuid                                  NOT NULL,
    member_role_code VARCHAR(50)                           NOT NULL,
    created_at       timestamptz default now()             not null,
    updated_at       timestamptz default now()             not null,
    CONSTRAINT fk_certificate_approval_member_member_role_code
        FOREIGN KEY (member_role_code) REFERENCES member_role (code)
            ON DELETE CASCADE,
    CONSTRAINT fk_certificate_approval_member_member_id
        FOREIGN KEY (member_id) REFERENCES employee (id)
            ON DELETE CASCADE
);

CREATE SEQUENCE certificate_request_code_seq START 1 INCREMENT 1;

CREATE TABLE certificate_request
(
    id             uuid        default gen_random_uuid() not null
        constraint certificate_request_pk primary key,
    code           varchar(20)                           not null,
    certificate_id uuid                                  NOT NULL,
    nominated_to   uuid                                  NOT NULL,
    nominated_by   uuid                                  NOT NULL,
    remarks        TEXT,
    status_code    VARCHAR(20)                           NOT NULL,
    actioned_at    timestamptz                           not null,
    created_at     timestamptz default now()             not null,
    updated_at     timestamptz default now()             not null,
    is_deleted     boolean     DEFAULT FALSE,
    is_active      BOOLEAN     DEFAULT TRUE,
    CONSTRAINT fk_certificate_request_status_code
        FOREIGN KEY (status_code) REFERENCES certificate_status (code)
            ON DELETE CASCADE,
    CONSTRAINT fk_certificate_request_nominated_to
        FOREIGN KEY (nominated_to) REFERENCES employee (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_certificate_request_nominated_by
        FOREIGN KEY (nominated_by) REFERENCES employee (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_certificate_request_certificate_id
        FOREIGN KEY (certificate_id) REFERENCES certificate (id)
            ON DELETE CASCADE
);

CREATE TABLE certificate_request_approval
(
    id                     uuid        default gen_random_uuid() not null
        constraint certificate_request_approval_member_pk primary key,
    member_id              uuid                                  not null,
    certificate_request_id uuid                                  not null,
    status_code            VARCHAR(20)                           NOT NULL,
    email_sent           boolean     DEFAULT FALSE,
    reason                 TEXT,
    created_at             timestamptz default now()             not null,
    updated_at             timestamptz default now()             not null,
    CONSTRAINT fk_certificate_request_approval_member_id
        FOREIGN KEY (member_id) REFERENCES employee (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_certificate_request_approval_certificate_request_id
        FOREIGN KEY (certificate_request_id) REFERENCES certificate_request (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_certificate_request_approval_status_code
        FOREIGN KEY (status_code) REFERENCES certificate_status (code)
            ON DELETE CASCADE
);

CREATE TABLE certificate_employee
(
    id             uuid        default gen_random_uuid() not null
        constraint certificate_employee_pk primary key,
    employee_id    uuid                                  not null,
    certificate_id uuid                                  not null,
    created_at     timestamptz default now()             not null,
    updated_at     timestamptz default now()             not null,
    CONSTRAINT fk_certificate_employee_employee_id
        FOREIGN KEY (employee_id) REFERENCES employee (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_certificate_employee_certificate_id
        FOREIGN KEY (certificate_id) REFERENCES certificate (id)
            ON DELETE CASCADE
);
