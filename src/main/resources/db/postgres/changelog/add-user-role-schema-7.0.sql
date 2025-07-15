CREATE TABLE user_roles_mapping (
                                role_id BIGINT NOT NULL,
                                user_id BIGINT NOT NULL,
                                CONSTRAINT pk_user_roles_mapping PRIMARY KEY (role_id, user_id)
);

ALTER TABLE user_roles_mapping ADD CONSTRAINT fk_role_user_roles FOREIGN KEY (role_id) REFERENCES user_roles (id) ON DELETE CASCADE;

ALTER TABLE user_roles_mapping ADD CONSTRAINT fk_user_user_roles FOREIGN KEY (user_id) REFERENCES user_details (id) ON DELETE CASCADE;
