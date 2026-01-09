package com.monolux.authorization.oauth2;

import com.monolux.authorization.configs.DataSourceConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Slf4j
@Service(JdbcTokenStore.BEAN_NAME_JDBC_TOKEN_STORE)
public class JdbcTokenStore extends org.springframework.security.oauth2.provider.token.store.JdbcTokenStore {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public final static String BEAN_NAME_JDBC_TOKEN_STORE = "jdbcTokenStoreCustom";

    public static final String DRIVER_CLASS_NAME_MSSQL = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static final String DRIVER_CLASS_NAME_ORACLE = "oracle.jdbc.OracleDriver";

    public static final String DRIVER_CLASS_NAME_MYSQL1 = "com.mysql.jdbc.Driver";

    public static final String DRIVER_CLASS_NAME_MYSQL2 = "com.mysql.cj.jdbc.Driver";

    public static final String DRIVER_CLASS_NAME_MARIADB = "org.mariadb.jdbc.Driver";

    public static final String DRIVER_CLASS_NAME_POSTGRES = "org.postgresql.Driver";

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    public JdbcTokenStore(@Qualifier(DataSourceConfig.BEAN_NAME_DATASOURCE_AUTHORIZATION) final DataSource dataSource) {
        super(dataSource);
        this.applyInsertAccessTokenSql(dataSource);
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    private void applyInsertAccessTokenSql(final DataSource dataSource) {
        if (!(dataSource instanceof HikariDataSource hikariDataSource)) {
            return;
        }

        String driverClassName = hikariDataSource.getDriverClassName();

        if (driverClassName.equalsIgnoreCase(JdbcTokenStore.DRIVER_CLASS_NAME_MSSQL)) {
            String sql = "MERGE INTO security.dbo.oauth_access_token AS A " +
                         "USING (SELECT ? AS token_id, " +
                                       "? AS token, " +
                                       "? AS authentication_id, " +
                                       "? AS user_name, " +
                                       "? AS client_id, " +
                                       "? AS authentication, " +
                                       "? AS refresh_token) AS B ON A.authentication_id = B.authentication_id " +
                         "WHEN NOT MATCHED THEN INSERT (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) " +
                         "VALUES (B.token_id, B.token, B.authentication_id, B.user_name, B.client_id, B.authentication, B.refresh_token ) " +
                         "WHEN MATCHED THEN UPDATE SET A.token_id = B.token_id, " +
                                                      "A.token = B.token, " +
                                                      "A.user_name = B.user_name, " +
                                                      "A.client_id = B.client_id, " +
                                                      "A.authentication = B.authentication, " +
                                                      "A.refresh_token = B.refresh_token;";
            this.setInsertAccessTokenSql(sql);
        } else if (driverClassName.equalsIgnoreCase(JdbcTokenStore.DRIVER_CLASS_NAME_ORACLE)) {
            String sql = "MERGE INTO security.dbo.oauth_access_token AS A " +
                         "USING (SELECT ? AS token_id, " +
                                       "? AS token, " +
                                       "? AS authentication_id, " +
                                       "? AS user_name, " +
                                       "? AS client_id, " +
                                       "? AS authentication, " +
                                       "? AS refresh_token " +
                                "FROM DUAL) AS B ON A.authentication_id = B.authentication_id " +
                         "WHEN NOT MATCHED THEN INSERT (A.token_id, A.token, A.authentication_id, A.user_name, A.client_id, A.authentication, A.refresh_token) " +
                         "VALUES (B.token_id, B.token, B.authentication_id, B.user_name, B.client_id, B.authentication, B.refresh_token ) " +
                         "WHEN MATCHED THEN UPDATE SET A.token_id = B.token_id, " +
                                                      "A.token = B.token, " +
                                                      "A.user_name = B.user_name, " +
                                                      "A.client_id = B.client_id, " +
                                                      "A.authentication = B.authentication, " +
                                                      "A.refresh_token = B.refresh_token;";
            this.setInsertAccessTokenSql(sql);
        } else if (driverClassName.equalsIgnoreCase(JdbcTokenStore.DRIVER_CLASS_NAME_MYSQL1) ||
                driverClassName.equalsIgnoreCase(JdbcTokenStore.DRIVER_CLASS_NAME_MYSQL2) ||
                driverClassName.equalsIgnoreCase(JdbcTokenStore.DRIVER_CLASS_NAME_MARIADB)) {
            String sql = "INSERT INTO security.oauth_access_token (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE token_id = VALUES(token_id), " +
                                                 "token = VALUES(token), " +
                                                 "user_name = VALUES(user_name), " +
                                                 "client_id = VALUES(client_id), " +
                                                 "authentication = VALUES(authentication)," +
                                                 "refresh_token = VALUES(refresh_token);";
            this.setInsertAccessTokenSql(sql);
        } else if (driverClassName.equalsIgnoreCase(JdbcTokenStore.DRIVER_CLASS_NAME_POSTGRES)) {
            String sql = "INSERT INTO security.oauth_access_token (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                         "ON CONFLICT (authentication_id) DO UPDATE SET token_id = EXCLUDED.token_id, " +
                                                                       "token = EXCLUDED.token, " +
                                                                       "user_name = EXCLUDED.user_name, " +
                                                                       "client_id = EXCLUDED.client_id, " +
                                                                       "authentication = EXCLUDED.authentication," +
                                                                       "refresh_token = EXCLUDED.refresh_token;";
            this.setInsertAccessTokenSql(sql);
        }
    }

    // endregion
}