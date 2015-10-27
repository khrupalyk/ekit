package com.saigak;

import java.io.*;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;


/**
 * Created by root on 24.10.15.
 */
public class ReadFileUtils {

    private static EmailValidator validator = new EmailValidator();

    public static List<String> readEmails(File file) throws IOException, InvalidFormatException {
        if (file.getName().endsWith(".txt"))
            return readEmailsFromTXT(file);
        else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx"))
            return readEmailsFromXLSX(file);
        else
            return new ArrayList<>();
    }

    public static List<String> readEmailsFromTXT(File file) throws IOException {

        List<String> emails = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;
        EmailValidator validator = new EmailValidator();
        while ((line = bufferedReader.readLine()) != null) {
            if (validator.validate(line.trim()))
                emails.add(line.trim());
        }

        return emails;
    }

    public static List<String> readEmailsFromXLSX(File file) throws IOException, InvalidFormatException {

        List<String> emails = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook(file);

        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        String email = cell.getStringCellValue();
                        System.err.println(email);
                        if (validator.validate(email))
                            emails.add(email.trim());
                        break;
                }
            }
        }
        fileInputStream.close();

        return emails;
    }
}
