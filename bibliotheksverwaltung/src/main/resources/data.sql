INSERT INTO public.application_user
(version, id, username, name, hashed_password, profile_picture, email,
 profil_profil_id, rollen_id)
VALUES
    (1, 1, 'user', 'John Normal',
     '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe',
     NULL, NULL, 3, 1),

    (1, 2, 'admin', 'Emma Executive',
     '$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV.',
     NULL, NULL, 4, 2)
    ON CONFLICT (id) DO NOTHING;
INSERT INTO rollen (rollenname, beschreibung)
VALUES
    ('USER', 'Standardbenutzer'),
    ('ADMIN', 'Administrator'),
    ('AUTHOR', 'Autor')
ON CONFLICT (rollenname) DO NOTHING;
