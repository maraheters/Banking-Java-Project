package db.repeatable;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class R__Insert_sample_data extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var managerIds = insertSupervisors(context, 3, null, new String[]{"MANAGER"});
        var clientIds = insertClients(context, 100);
        var accountIds = insertAccounts(context, clientIds);
    }

    private List<Integer> insertAccounts(Context context, List<Integer> clientIds) throws SQLException {
        String sql = """
            INSERT INTO account (iban, status, type, balance, date_created, holder_id)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

        List<Integer> ids = new ArrayList<>();
        Connection connection = context.getConnection();

        try (PreparedStatement statement = context.getConnection().prepareStatement(sql)) {

            for (int i = 1; i <= clientIds.size(); i++) {
                statement.setString(1, "BY1234567" + i);    // iban
                statement.setString(2, "ACTIVE");           // status
                statement.setString(3, "PERSONAL");         // type
                statement.setBigDecimal(4, BigDecimal.ZERO);   // balance
                statement.setObject(5, LocalDate.now());
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

    private List<Integer> insertSupervisors(Context context, int amount, Integer companyId, String[] roleNames) throws SQLException {
        String sql = """
            WITH inserted_user AS (
                INSERT INTO public.user(name, email, password_hash)
                VALUES (?, ?, ?)
                RETURNING id
            ),
            inserted_supervisor AS (
                INSERT INTO public.supervisor(user_id, company_id)
                VALUES ((SELECT id FROM inserted_user), ?)
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
                statement.setString(3, "hashed_password_" + i);  // password_hash
                statement.setObject(4, companyId);  // company_id

                // Convert roles to an SQL array (PostgreSQL specific)
                statement.setArray(5, connection.createArrayOf("VARCHAR", roleNames));

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
                INSERT INTO public.client (user_id, phone_number, passport_number, identification_number)
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
                statement.setString(3, "hashed_password_" + i);  // password_hash
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
