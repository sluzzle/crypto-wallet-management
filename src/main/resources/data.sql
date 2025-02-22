DELETE FROM assets;
DELETE FROM wallets;

INSERT INTO wallets (email, token) VALUES ('user1@example.com', '2198442d-c783-4419-aecf-54aec303d7a0');
INSERT INTO wallets (email, token) VALUES ('user2@example.com', UUID());

-- INSERT INTO assets (wallet_id, symbol, quantity, price) VALUES ( 3, 'BTC', 19826968.0000000000000000, 98372.2836812654536840), ( 3, 'ETH', 120561583.8838996900000000, 2740.9516829683028931);