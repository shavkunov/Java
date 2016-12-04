package ru.spbau.shavkunov;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class Database {
    /**
     * Необходимые поля для работы с БД.
     */
    private static Statement stmt;
    private static Connection c;

    /**
     * Основная логика программы.
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        initDatabase();

        Scanner input = new Scanner(System.in);
        if (!input.hasNextInt()) {
            System.out.println("Введено не целое число!");
        }

        int cmd = -1;
        while (cmd != 0) {
            cmd = input.nextInt();
            input.nextLine();

            switch (cmd) {
                case 0:
                    break;

                case 1: {
                    System.out.println("Введите имя пользователя телефоном");
                    String name = input.nextLine();
                    System.out.println("Введите номер телефона только цифрами");
                    long number = input.nextLong();
                    input.nextLine();

                    addEntry(name, number);
                    break;
                }

                case 2: {
                    System.out.println("Введите имя для поиска");
                    String name = input.nextLine();
                    ResultSet results = getNumbers(name);

                    System.out.println("Ваши результаты:");
                    while (results.next()) {
                        System.out.println(results.getInt("number"));
                    }

                    break;
                }

                case 3: {
                    System.out.println("Введите телефон для поиска");
                    long number = input.nextLong();
                    input.nextLine();
                    ResultSet results = getNames(number);

                    System.out.println("Ваши результаты:");
                    while (results.next()) {
                        System.out.println(results.getString("name"));
                    }

                    break;
                }

                case 4: {
                    System.out.println("Введите имя пользователя телефоном");
                    String name = input.nextLine();
                    System.out.println("Введите номер телефона только цифрами");
                    long number = input.nextLong();
                    deleteEntry(name, number);
                    System.out.println("Запись " + name + " " + number + " удалена.");

                    break;
                }

                case 5: {
                    System.out.println("Введите имя пользователя телефоном");
                    String name = input.nextLine();
                    System.out.println("Введите номер телефона только цифрами");
                    long number = input.nextLong();
                    input.nextLine();
                    System.out.println("Введите новое имя пользователя телефоном");
                    String newName = input.nextLine();
                    changeName(name, newName, number);

                    break;
                }

                case 6: {
                    System.out.println("Введите имя пользователя телефоном");
                    String name = input.nextLine();
                    System.out.println("Введите номер телефона только цифрами");
                    long number = input.nextLong();
                    input.nextLine();
                    System.out.println("Введите новый номер телефона только цифрами");
                    long newNumber = input.nextLong();
                    input.nextLine();
                    changeNumber(name, number, newNumber);

                    break;
                }

                case 7: {
                    String queryAllEntries = "SELECT Names.name, Numbers.number " +
                                             "FROM Name_to_number INNER JOIN Names ON Names.ID = name_ID " +
                                             "INNER JOIN Numbers ON Numbers.ID = number_ID";

                    ResultSet results = stmt.executeQuery(queryAllEntries);
                    while(results.next()) {
                        System.out.println(results.getString("name") + " " + results.getLong("number"));
                    }

                    break;
                }

                default:
                    break;
            }
        }

        stmt.close();
        c.commit();
        c.close();
    }

    /**
     * Изменение номера в паре имя - телефон.
     * @param name имя.
     * @param number старый телефонный номер.
     * @param newNumber новый телефонный номер.
     */
    private static void changeNumber(String name, long number, long newNumber) throws SQLException {
        Integer nameIndex = checkName(name);
        Integer numberIndex = checkNumber(number);

        String querуCountNamesOfNumber = "SELECT COUNT(*) AS total FROM Name_to_number WHERE number_ID = " + numberIndex;
        if (stmt.executeQuery(querуCountNamesOfNumber).getInt("total") == 1) {
            String queryUpdateNumber = "UPDATE Number SET number = " + newNumber + " WHERE ID = " + numberIndex;
            stmt.executeUpdate(queryUpdateNumber);
        } else {
            String queryInsertNewNumber = "INSERT INTO Numbers VALUES (" + newNumber + ")";
            stmt.executeUpdate(queryInsertNewNumber);
            String queryGettingNewIndex = "SELECT ID From Numbers WHERE number = " + newNumber;
            int newIndex = stmt.executeQuery(queryGettingNewIndex).getInt("ID");
            String queryUpdateRelation = "UPDATE Name_to_number SET number_ID = " + newIndex +
                                         " WHERE name_ID = " + nameIndex + " AND number_ID = " + numberIndex;

            stmt.executeUpdate(queryUpdateRelation);
        }
    }

    /**
     * Изменение имени в паре имя - телефон.
     * @param name старое имя.
     * @param newName новое имя
     * @param number телефонны номер.
     * @throws SQLException
     */
    private static void changeName(String name, String newName, long number) throws SQLException {
        Integer nameIndex = checkName(name);
        Integer numberIndex = checkNumber(number);

        String querуCountNumbersOfName = "SELECT COUNT(*) AS total FROM Name_to_number WHERE name_ID = " + nameIndex;
        if (stmt.executeQuery(querуCountNumbersOfName).getInt("total") == 1) {
            String queryUpdateName = "UPDATE Names SET name = '" + newName + "' WHERE ID = " + nameIndex;
            stmt.executeUpdate(queryUpdateName);
        } else {
            String queryInsertNewName = "INSERT INTO Names(name) VALUES ('" + newName + "')";
            stmt.executeUpdate(queryInsertNewName);
            String queryGettingNewIndex = "SELECT ID From Names WHERE name = '" + newName + "'";
            int newIndex = stmt.executeQuery(queryGettingNewIndex).getInt("ID");
            String queryUpdateRelation = "UPDATE Name_to_number SET name_ID = " + newIndex +
                                         " WHERE name_ID = " + nameIndex + " AND number_ID = " + numberIndex;

            stmt.executeUpdate(queryUpdateRelation);
        }
    }

    /**
     * Удаление записи имени и телефона из БД
     */
    private static void deleteEntry(String name, long number) throws SQLException {
        Integer nameIndex = checkName(name);
        Integer numberIndex = checkNumber(number);

        if (nameIndex == null || numberIndex == null) {
            return;
        }

        String queryDeleteRelation = "DELETE FROM Name_to_number " +
                                     "WHERE name_ID = " + nameIndex + " AND number_ID = " + numberIndex;

        stmt.executeUpdate(queryDeleteRelation);

        String queryDidNameHaveNumber = "SELECT COUNT(*) AS total FROM Name_to_number WHERE name_ID = " + nameIndex;
        if (stmt.executeQuery(queryDidNameHaveNumber).getInt("total") == 0) {
            String queryDeleteName = "DELETE FROM Names WHERE name = '" + name + "'";
            stmt.executeUpdate(queryDeleteName);
        }

        String queryDidNumberHaveName = "SELECT COUNT(*) AS total FROM Name_to_number WHERE number_ID = " + numberIndex;
        if (stmt.executeQuery(queryDidNumberHaveName).getInt("total") == 0) {
            String queryDeleteNumber = "DELETE FROM Numbers WHERE number = " + number;
            stmt.executeUpdate(queryDeleteNumber);
        }
    }

    /**
     * Получение курсора всех номеров по заданному имени.
     */
    private static ResultSet getNumbers(String name) throws SQLException {
        String queryFindNumbersByName = "SELECT number from Numbers WHERE ID = " +
                                        "(SELECT number_ID FROM Name_to_number " +
                                        "INNER JOIN Names ON name = '" + name + "')";
        return stmt.executeQuery(queryFindNumbersByName);
    }

    /**
     * Получение курсора всех имен по заданному номеру.
     */
    private static ResultSet getNames(long number) throws SQLException {
        String queryFindNamesByNumber = "SELECT name from Names WHERE ID = " +
                                        "(SELECT name_ID FROM Name_to_number " +
                                        "INNER JOIN Numbers ON number = " + number + ")";
        return stmt.executeQuery(queryFindNamesByNumber);
    }

    /**
     * Проверка на существование имени в таблице имен.
     * @param name существование этого имени хотим проверить.
     * @return null, если имени не существует, иначе индекс его строки в БД.
     */
    @Nullable
    private static Integer checkName(String name) throws SQLException {
        String queryCheckName = "SELECT ID FROM Names WHERE name = '" + name + "'";
        ResultSet cursor = stmt.executeQuery(queryCheckName);
        if (cursor.next()) {
            return cursor.getInt("ID");
        }

        return null;
    }

    /**
     * Проверка на существование номера в таблице номеров.
     * @param number существование этого номера хотим проверить.
     * @return null, если номера не существует, иначе индекс его строки в БД.
     */
    @Nullable
    private static Integer checkNumber(Long number) throws SQLException {
        String queryCheckNumber = "SELECT ID FROM Numbers WHERE number = " + number;
        ResultSet cursor = stmt.executeQuery(queryCheckNumber);
        if (cursor.next()) {
            return cursor.getInt("ID");
        }

        return null;
    }

    /**
     * Добавление в БД пары имени и телефона.
     */
    private static void addEntry(String name, long number) throws SQLException {
        Integer nameIndex = checkName(name);
        if (nameIndex == null) {
            String queryAddName = "INSERT INTO Names(name) VALUES ('" + name + "')";
            stmt.executeUpdate(queryAddName);
            String findNewIndex = "SELECT ID FROM Names WHERE name = '" + name + "'";
            nameIndex = stmt.executeQuery(findNewIndex).getInt("ID");
        }

        Integer numberIndex = checkNumber(number);
        if (numberIndex == null) {
            String queryAddNumber = "INSERT INTO Numbers(number) VALUES (" + number + ")";
            stmt.executeUpdate(queryAddNumber);
            String findNewIndex = "SELECT ID FROM Numbers WHERE number = " + number;
            numberIndex = stmt.executeQuery(findNewIndex).getInt("ID");
        }

        String queryCheckRelation = "SELECT name_ID, number_ID FROM Name_to_number " +
                                    "WHERE name_ID = " + nameIndex +
                                    " AND number_ID = " + numberIndex;

        ResultSet cursor = stmt.executeQuery(queryCheckRelation);
        if (cursor.next()) {
            System.out.println("Такая запись уже существует!");
        } else {
            String queryAddRelation = "INSERT INTO Name_to_number(name_ID, number_ID) " +
                                      "VALUES (" + nameIndex + ", " + numberIndex + ")";
            stmt.executeUpdate(queryAddRelation);
            System.out.println("Запись добавлена!");
        }
    }

    /**
     * Инициализация БД. Создание трех пустых таблиц. Название БД - content.db.
     */
    private static void initDatabase() throws SQLException {
        File db = new File("content.db");
        if (db.exists()) {
            db.delete();
        }


        c = DriverManager.getConnection("jdbc:sqlite:content.db");
        c.createStatement().execute("PRAGMA foreign_keys = ON");
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String queryCreateTableNumbers = "CREATE TABLE Numbers (" +
                                         "ID INTEGER PRIMARY KEY NOT NULL, " +
                                         "number INTEGER NOT NULL)";

        stmt.executeUpdate(queryCreateTableNumbers);

        String queryCreateTableNames = "CREATE TABLE Names (" +
                                       "ID INTEGER PRIMARY KEY NOT NULL, " +
                                       "name TEXT NOT NULL)";

        stmt.executeUpdate(queryCreateTableNames);

        String queryCreateTableNameToNumber = "CREATE TABLE Name_to_number (" +
                                              "name_ID INTEGER, " +
                                              "number_ID INTEGER, " +
                                              "FOREIGN KEY(name_ID) REFERENCES Names(ID), " +
                                              "FOREIGN KEY(number_ID) REFERENCES Numbers(ID))";

        stmt.executeUpdate(queryCreateTableNameToNumber);
    }
}
