package model;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Year;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FileParsingTest {
    ClassLoader cl = FileParsingTest.class.getClassLoader();
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void verifyZipFilesContentTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("resource.zip");
             ZipInputStream zis = new ZipInputStream(InputStream.nullInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".pdf")) {
                    PDF pdf = new PDF(zis);
                    assertEquals(1, pdf.numberOfPages);
                    assertEquals("Kate Koroleva", pdf.author);
                    assertTrue(pdf.text.contains("Hi, QA Guru!"));
                    System.out.println("Successful check pdf file.");
                } else if (entry.getName().contains(".xlsx")) {
                    XLS xls = new XLS(zis);
                    Assertions.assertEquals("ООО СтройМаркет",
                            xls.excel.getSheetAt(0)
                                    .getRow(33)
                                    .getCell(0)
                                    .getStringCellValue());

                    System.out.println("Successful check xlsx file.");
                } else if (entry.getName().contains(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = csvReader.readAll();
                    Assertions.assertEquals(2, content.size());
                    final String[] firstRow = content.get(0);
                    Assertions.assertArrayEquals(new String[]{"CN001", " 131", " Ivanova"}, firstRow);
                    System.out.println("Successful check csv file.");
                }
            }
        }
    }

    @Test
    void verifyJsonContentTest() throws Exception {
        try (
                InputStream is = cl.getResourceAsStream("test.json");
                InputStreamReader reader = new InputStreamReader(is)
        ) {
            Shampoo shampoo = mapper.readValue(reader, Shampoo.class);
            assertThat(shampoo.getBrand()).isEqualTo("Shampoo");
            assertThat(shampoo.getName()).isNotEmpty();
            int currentYear = Year.now().getValue();
            assertThat(shampoo.getReleasedYear()).isBetween(1900, currentYear);
            assertEquals(shampoo.getNotes().get(3), "grass");
            System.out.println("Successful check json file.");
        }
    }


}