package db.repeatable;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class R__Insert_sample_data extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var clientIds = insertClients(context, 100);
        var managerIds = insertSupervisors(context, 3, new String[]{"MANAGER"});
        var bankIds = insertBanks(context, 3);
        var accountIds = insertPersonalAccounts(context, clientIds, bankIds);
        var enterpriseIds = insertEnterprise(context, bankIds.getFirst(), 3);
    }

    private List<Integer> insertEnterprise(Context context, Integer bankId, int amount) throws SQLException {
        String sql = """
            INSERT INTO enterprise (type, legal_name, unp, bank_id, legal_address)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
        """;

        List<Integer> ids = new ArrayList<>();
        Connection connection = context.getConnection();

        try (PreparedStatement statement = context.getConnection().prepareStatement(sql)) {

            for (int i = 1; i <= amount; i++) {
                statement.setString(1, "Type " + i);        // type
                statement.setString(2, "Enterprise " + i);  // legal_name
                statement.setString(                              // unp
                        3,
                        new java.util.Random().ints(20, 0, 10)
                                .mapToObj(Integer::toString)
                                .collect(java.util.stream.Collectors.joining())
                );
                statement.setInt(4, bankId);                   // bank_id
                statement.setString(5, "Street " + i);      // legal_address

                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ids.add(rs.getInt(1));
                    }
                }
            }
        }

        return ids;
    }

    private List<Integer> insertBanks(Context context, int amount) throws SQLException {
        String sql = """
            INSERT INTO public.bank(name, bic, address)
            VALUES (?, ?, ?)
            RETURNING id
        """;

        List<Integer> ids = new ArrayList<>();
        Connection connection = context.getConnection();

        try (PreparedStatement statement = context.getConnection().prepareStatement(sql)) {

            for (int i = 1; i <= amount; i++) {
                statement.setString(1, "Bank " + i);        // name
                statement.setString(2, "ABCDBYCC" + i);      // bic
                statement.setString(3, "Street " + i);         // address

                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ids.add(rs.getInt(1));
                    }
                }
            }
        }

        return ids;
    }

    private List<Integer> insertPersonalAccounts(Context context, List<Integer> clientIds, List<Integer> bankIds) throws SQLException {
        String sql = """
            WITH inserted_account AS (
                INSERT INTO account (iban, status, balance, created_at, bank_id)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
            )
            INSERT INTO personal_account(id, holder_id)
            VALUES ( (SELECT id FROM inserted_account), ? )
            RETURNING id;
        """;

        List<Integer> ids = new ArrayList<>();
        Connection connection = context.getConnection();

        try (PreparedStatement statement = context.getConnection().prepareStatement(sql)) {

            for (int i = 1; i <= clientIds.size(); i++) {
                statement.setString(1, "BY1234567" + i);    // iban
                statement.setString(2, "ACTIVE");           // status
                statement.setBigDecimal(3, BigDecimal.ZERO);   // balance
                statement.setObject(4, LocalDateTime.now());       // created_at
                statement.setLong(5, bankIds.get((i - 1) % bankIds.size()));    // bank_id
                statement.setLong(6, clientIds.get(i - 1));

                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ids.add(rs.getInt(1));
                    }
                }
            }
        }

        return ids;
    }

    private List<Integer> insertSupervisors(Context context, int amount, String[] roleNames) throws SQLException {
        String sql = """
            WITH inserted_user AS (
                INSERT INTO public.user(name, email, password_hash)
                VALUES (?, ?, ?)
                RETURNING id
            ),
            inserted_supervisor AS (
                INSERT INTO public.supervisor(id)
                VALUES ((SELECT id FROM inserted_user))
                RETURNING id
            ),
            role_ids AS (
                SELECT id
                FROM public.supervisor_role
                WHERE name = ANY (?)
            ),
            id AS (
                INSERT INTO public.supervisor_role_supervisor(role_id, supervisor_id)
                SELECT id, (SELECT id FROM inserted_supervisor)
                FROM role_ids
                RETURNING supervisor_id
            )
            SELECT * FROM id LIMIT 1;
        """;

        List<Integer> ids = new ArrayList<>();
        Connection connection = context.getConnection();

        try (PreparedStatement statement = context.getConnection().prepareStatement(sql)) {

            for (int i = 1; i <= amount; i++) {
                statement.setString(1, "Supervisor " + i);  // name
                statement.setString(2, "supervisor" + i + "@example.com");  // email
                statement.setString(3, "password" + i);  // password_hash

                // Convert roles to an SQL array (PostgreSQL specific)
                statement.setArray(4, connection.createArrayOf("VARCHAR", roleNames));

                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ids.add(rs.getInt(1));
                    }
                }
            }
        }

        return ids;
    }

    private List<Integer> insertClients(Context context, int amount) throws SQLException {
        String sql = """
            WITH inserted_user AS (
                INSERT INTO public.user(name, email, password_hash)
                VALUES (?, ?, ?)
                RETURNING id
            ),
            inserted_client AS (
                INSERT INTO public.client (id, phone_number, passport_number, identification_number)
                VALUES ((SELECT id FROM inserted_user), ?, ?, ?)
                RETURNING id
            ),
            role_ids AS (
                SELECT id
                FROM public.client_role
                WHERE name = ANY (?)
            ),
            id AS (
                INSERT INTO public.client_role_client(role_id, client_id)
                SELECT id, (SELECT id FROM inserted_client)
                FROM role_ids
                RETURNING client_id
            )
            SELECT * FROM id LIMIT 1;
        """;

        List<Integer> ids = new ArrayList<>();
        Connection connection = context.getConnection();

        try (PreparedStatement statement = context.getConnection().prepareStatement(sql)) {

            for (int i = 1; i <= amount; i++) {
                statement.setString(1, "User " + i);  // name
                statement.setString(2, "user" + i + "@example.com");  // email
                statement.setString(3, "password" + i);  // password_hash
                statement.setString(4, "123456789" + i);  // phone_number
                statement.setString(5, "AB" + i);  // passport_number
                statement.setString(6, "ID" + i);  // identification_number

                // Convert roles to an SQL array (PostgreSQL specific)
                String[] roleNames = {"BASIC"}; // Ensure at least one role
                statement.setArray(7, connection.createArrayOf("VARCHAR", roleNames));

                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ids.add(rs.getInt(1));
                    }
                }
            }
        }

        return ids;
    }
}
