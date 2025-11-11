CREATE TABLE app_type_lookup
(
    code    VARCHAR(20) UNIQUE PRIMARY KEY,
    name_en VARCHAR(50),
    name_ar VARCHAR(50)
);

INSERT INTO app_type_lookup (code, name_en, name_ar)
VALUES ('ADMIN_CONSOLE', 'ADMIN CONSOLE APP', 'تطبيق وحدة التحكم الإدارية'),
       ('REWARD_APP', 'REWARD APP', 'تطبيق المكافأة');

CREATE TABLE band_level_lookup
(
    code       VARCHAR(100) UNIQUE PRIMARY KEY,
    level      VARCHAR(254) UNIQUE       NOT NULL,
    is_deleted BOOLEAN     DEFAULT FALSE,
    is_active  BOOLEAN     DEFAULT TRUE,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL
);

INSERT INTO band_level_lookup (code, level)
VALUES ('BAND 1', '1'),
       ('BAND 2', '2'),
       ('BAND 3', '3'),
       ('BAND 4', '4'),
       ('BAND 5', '5'),
       ('BAND 6', '6'),
       ('BAND 7', '7'),
       ('BAND 8', '8'),
       ('BAND 9', '9'),
       ('BAND 10', '10'),
       ('BAND 11', '11'),
       ('BAND 12', '12');

CREATE TABLE role
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code       VARCHAR(50)                    NOT NULL,
    name       VARCHAR(100)                   NOT NULL,
    scope      VARCHAR(20)                    NOT NULL,
    created_at TIMESTAMPTZ      DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ      DEFAULT now() NOT NULL,
    CONSTRAINT unique_role_code_scope UNIQUE (code, scope)
);

INSERT INTO role (code, name, scope, created_at, updated_at)
VALUES ('SUPER_ADMIN', 'Super Administrator of the Platform', 'ALL', now(), now()),
       ('ADMIN', 'Administrator', 'ALL', now(), now()),
       ('COMMITTEE_MEMBER', 'COMMITTEE MEMBER', 'ALL', now(), now()),
       ('COMMITTEE_HEAD', 'COMMITTEE HEAD', 'ALL', now(), now()),
       ('EMPLOYEE', 'Standard Employee Access', 'REWARD_APP', now(), now());

CREATE TABLE department
(
    id         UUID        DEFAULT gen_random_uuid() PRIMARY KEY,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    name_en    VARCHAR(50)               NOT NULL,
    name_ar    VARCHAR(50)               NOT NULL,
    is_deleted BOOLEAN     DEFAULT FALSE,
    is_active  BOOLEAN     DEFAULT TRUE,
    CONSTRAINT department_unique_name_en UNIQUE (name_en),
    CONSTRAINT department_unique_name_ar UNIQUE (name_ar)
);

CREATE TABLE job
(
    id                     UUID        DEFAULT gen_random_uuid() PRIMARY KEY,
    created_at             TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at             TIMESTAMPTZ DEFAULT now() NOT NULL,
    title_en               VARCHAR(50)               NOT NULL,
    title_ar               VARCHAR(50)               NOT NULL,
    is_deleted             BOOLEAN     DEFAULT FALSE,
    is_active              BOOLEAN     DEFAULT TRUE,
    band_level_lookup_code VARCHAR(100)              NOT NULL
        CONSTRAINT job_band_level_lookup_code_fk
            REFERENCES band_level_lookup (code)
            ON DELETE CASCADE,
    CONSTRAINT job_unique_title_en UNIQUE (title_en),
    CONSTRAINT job_unique_title_ar UNIQUE (title_ar)
);

CREATE TABLE location
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    name_en    VARCHAR(50)               NOT NULL,
    name_ar    VARCHAR(50)               NOT NULL,
    is_deleted BOOLEAN     DEFAULT FALSE,
    is_active  BOOLEAN     DEFAULT TRUE,
    CONSTRAINT location_unique_name_en UNIQUE (name_en),
    CONSTRAINT location_unique_name_ar UNIQUE (name_ar)
);

CREATE SEQUENCE user_code_seq START 1 INCREMENT 1;

CREATE TABLE app_user
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email      VARCHAR(254) NOT NULL,
    user_code  VARCHAR(20),
    app_code   VARCHAR(20)  NOT NULL,
    password   TEXT         NOT NULL,
    is_active  BOOLEAN          DEFAULT TRUE,
    is_deleted BOOLEAN          DEFAULT FALSE,
    created_at TIMESTAMPTZ      DEFAULT now(),
    updated_at TIMESTAMPTZ      DEFAULT now(),
    status     VARCHAR(20)      DEFAULT 'PENDING',
    CONSTRAINT chk_status CHECK (status IN ('ACTIVATED', 'DEACTIVATED', 'PENDING', 'BLOCKED')),
    CONSTRAINT app_user_unique_email_per_app UNIQUE (app_code, email)
);

CREATE SEQUENCE employee_code_seq START 1 INCREMENT 1;

CREATE TABLE employee
(
    id                   UUID        DEFAULT gen_random_uuid() PRIMARY KEY,
    employee_code        VARCHAR(20)               NOT NULL,
    user_id              UUID,
    role_id              UUID                      NOT NULL
        CONSTRAINT employee_role_id_fk
            REFERENCES role (id)
            ON DELETE CASCADE,
    profile_picture_path TEXT,
    first_name           VARCHAR(50)               NOT NULL,
    last_name            VARCHAR(50),
    mobile_number        VARCHAR(20) UNIQUE        NOT NULL,
    email                VARCHAR(254) UNIQUE       NOT NULL,
    gender               SMALLINT                  NOT NULL,
    job_id               UUID                      NOT NULL
        CONSTRAINT employee_job_id_fk
            REFERENCES job (id)
            ON DELETE CASCADE,
    department_id        UUID                      NOT NULL
        CONSTRAINT employee_department_id_fk
            REFERENCES department (id)
            ON DELETE CASCADE,
    location_id          BIGINT                    NOT NULL
        CONSTRAINT employee_location_id_fk
            REFERENCES location (id)
            ON DELETE CASCADE,
    updated_at           TIMESTAMPTZ DEFAULT now() NOT NULL,
    created_at           TIMESTAMPTZ DEFAULT now() NOT NULL,
    last_login_at        TIMESTAMPTZ DEFAULT now() NOT NULL,
    is_deleted           BOOLEAN     DEFAULT FALSE,
    is_active            BOOLEAN     DEFAULT TRUE
);

CREATE TABLE invite
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_email   VARCHAR(254)                       NOT NULL,
    receiver_email VARCHAR(254)                       NOT NULL,
    app_type       VARCHAR(100)                       NOT NULL,
    reference_id   UUID,
    status         VARCHAR(50)      DEFAULT 'PENDING' NOT NULL,
    resend_count   INT              DEFAULT 0,
    is_active      BOOLEAN          DEFAULT TRUE,
    created_at     TIMESTAMPTZ      DEFAULT now()     NOT NULL,
    updated_at     TIMESTAMPTZ      DEFAULT now()     NOT NULL,
    expires_at     TIMESTAMPTZ
);
