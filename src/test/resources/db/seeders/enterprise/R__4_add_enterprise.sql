INSERT INTO enterprise (type, legal_name, unp, bank_id, legal_address)
VALUES ('type1', 'legal_name1', 'unp1', 1, 'legal_address1')
RETURNING id;

INSERT INTO enterprise (type, legal_name, unp, bank_id, legal_address)
VALUES ('type2', 'legal_name2', 'unp2', 1, 'legal_address2')
RETURNING id