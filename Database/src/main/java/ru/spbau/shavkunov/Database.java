package ru.spbau.shavkunov;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class Database {
    private static Statement stmt;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        File db = new File("content.db");
        if (db.exists()) {
            db.delete();
        }

        Connection c = DriverManager.getConnection("jdbc:sqlite:content.db");
        c.createStatement().execute("PRAGMA foreign_keys = ON");
        c.setAutoCommit(false);
        stmt = c.createStatement();

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
                        System.out.println(results.getInt(results.findColumn("number")));
                    }

                    break;
                }

                case 3: {
                    System.out.println("Введите телефон для поиска");
                    long number = input.nextLong();
                    ResultSet results = getNames(number);

                    System.out.println("Ваши результаты:");
                    while (results.next()) {
                        System.out.println(results.getString(results.findColumn("name")));
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
                    System.out.println("Введите новый номер телефона только цифрами");
                    long newNumber = input.nextLong();
                    changeNumber(name, number, newNumber);

                    break;
                }

                case 7: {
                    String queryAllEntries = "SELECT Names.name, Numbers.number " +
                                             "FROM Name_to_number INNER JOIN Names ON Names.ID = name_ID " +
                                             "INNER JOIN Numbers ON Numbers.ID = number_ID";

                    ResultSet results = stmt.executeQuery(queryAllEntries);
                    while(results.next()) {
                        System.out.println(results.findColumn("name") + " " + results.findColumn("number"));
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

    private static void changeNumber(String name, long number, long newNumber) throws SQLException {
        Integer nameIndex = checkName(name);
        Integer numberIndex = checkNumber(number);

        String querуCountNamesOfNumber = "SELECT COUNT(*) FROM Name_to_number WHERE number_ID = " + numberIndex;
        if (stmt.executeQuery(querуCountNamesOfNumber).getInt("total") == 1) {
            String queryUpdateNumber = "UPDATE Number SET number = " + newNumber + " WHERE ID = " + numberIndex;
            stmt.executeUpdate(queryUpdateNumber);
        } else {
            String queryInsertNewNumber = "INSERT INTO Numbers VALUES (" + newNumber + ")";
            stmt.executeUpdate(queryInsertNewNumber);
            String queryGettingNewIndex = "SELECT ID From Numbers WHERE number = " + newNumber;
            int newIndex = stmt.executeQuery(queryGettingNewIndex).findColumn("ID");
            String queryUpdateRelation = "UPDATE Name_to_number SET number_ID = " + newIndex +
                                         " WHERE name_ID = " + nameIndex + " AND number_ID = " + numberIndex;

            stmt.executeUpdate(queryUpdateRelation);
        }
    }

    private static void changeName(String name, String newName, long number) throws SQLException {
        Integer nameIndex = checkName(name);
        Integer numberIndex = checkNumber(number);

        String querуCountNumbersOfName = "SELECT COUNT(*) FROM Name_to_number WHERE name_ID = " + nameIndex;
        if (stmt.executeQuery(querуCountNumbersOfName).getInt("total") == 1) {
            String queryUpdateName = "UPDATE Names SET name = '" + newName + "' WHERE ID = " + nameIndex;
            stmt.executeUpdate(queryUpdateName);
        } else {
            String queryInsertNewName = "INSERT INTO Names VALUES ('" + newName + "')";
            stmt.executeQuery(queryInsertNewName);
            String queryGettingNewIndex = "SELECT ID From Names WHERE name = '" + newName + "'";
            int newIndex = stmt.executeQuery(queryGettingNewIndex).findColumn("ID");
            String queryUpdateRelation = "UPDATE Name_to_number SET name_ID = " + newIndex +
                                         " WHERE name_ID = " + nameIndex + " AND number_ID = " + numberIndex;

            stmt.executeUpdate(queryUpdateRelation);
        }
    }

    private static void deleteEntry(String name, long number) throws SQLException {
        Integer nameIndex = checkName(name);
        Integer numberIndex = checkNumber(number);

        if (nameIndex == null || numberIndex == null) {
            return;
        }

        String queryDeleteRelation = "DELETE name_ID, number_ID FROM Name_to_number " +
                                     "WHERE name_ID = " + nameIndex + " AND number_ID = " + numberIndex;

        stmt.executeQuery(queryDeleteRelation);

        String queryDidNameHaveNumber = "SELECT COUNT(*) FROM Name_to_number WHERE name_ID = " + nameIndex;
        if (stmt.executeQuery(queryDidNameHaveNumber).getInt("total") == 0) {
            String queryDeleteName = "DELETE ID, name FROM Names WHERE name = '" + name + "'";
            stmt.executeQuery(queryDeleteName);
        }

        String queryDidNumberHaveName = "SELECT COUNT(*) FROM Name_to_number WHERE number_ID = " + numberIndex;
        if (stmt.executeQuery(queryDidNumberHaveName).getInt("total") == 0) {
            String queryDeleteNumber = "DELETE ID, number FROM Numbers WHERE number = " + number;
            stmt.executeQuery(queryDeleteNumber);
        }
    }

    private static ResultSet getNumbers(String name) throws SQLException {
        String queryFindNumbersByName = "SELECT number from Numbers WHERE ID = " +
                                        "(SELECT number_ID FROM Name_to_number " +
                                        "INNER JOIN Names ON name = '" + name + "')";
        return stmt.executeQuery(queryFindNumbersByName);
    }

    private static ResultSet getNames(long number) throws SQLException {
        String queryFindNamesByNumber = "SELECT name from Names WHERE ID = " +
                                        "(SELECT name_ID FROM Name_to_number " +
                                        "INNER JOIN Numbers ON number = " + number + ")";
        return stmt.executeQuery(queryFindNamesByNumber);
    }

    @Nullable
    private static Integer checkName(String name) throws SQLException {
        String queryCheckName = "SELECT EXISTS(SELECT ID FROM Names WHERE name = '" + name + "')";
        ResultSet cursor = stmt.executeQuery(queryCheckName);
        if (!cursor.next()) {
            return cursor.getInt(cursor.findColumn("ID"));
        }

        return null;
    }

    @Nullable
    private static Integer checkNumber(Long number) throws SQLException {
        String queryCheckNumber = "SELECT ID FROM Numbers WHERE number = " + number;
        ResultSet cursor = stmt.executeQuery(queryCheckNumber);
        if (cursor.next()) {
            return cursor.getInt(cursor.findColumn("ID"));
        }

        return null;
    }

    private static void addEntry(String name, long number) throws SQLException {
        Integer nameIndex = checkName(name);
        if (nameIndex == null) {
            String queryAddName = "INSERT INTO Names(name) VALUES ('" + name + "')";
            nameIndex = stmt.executeUpdate(queryAddName);
        }

        Integer numberIndex = checkNumber(number);
        if (numberIndex == null) {
            String queryAddNumber = "INSERT INTO Numbers(number) VALUES (" + number + ")";
            numberIndex = stmt.executeUpdate(queryAddNumber);
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

    private static void initDatabase() throws SQLException {
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
